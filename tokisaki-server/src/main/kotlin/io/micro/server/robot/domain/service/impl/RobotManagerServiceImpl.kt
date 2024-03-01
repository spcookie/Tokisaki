package io.micro.server.robot.domain.service.impl

import cn.hutool.core.util.IdUtil
import com.fasterxml.jackson.databind.ObjectMapper
import io.micro.core.context.AuthContext
import io.micro.core.exception.CmdException
import io.micro.core.exception.fail
import io.micro.core.exception.requireNonNull
import io.micro.core.exception.requireTure
import io.micro.core.function.dto.CardID
import io.micro.core.function.dto.MediaType
import io.micro.core.rest.*
import io.micro.core.robot.Credential
import io.micro.core.robot.Robot
import io.micro.core.robot.qq.QQRobot
import io.micro.core.robot.qq.QQRobotFactory
import io.micro.core.robot.qq.QQRobotManager
import io.micro.function.domain.strategy.FunctionContext
import io.micro.server.auth.domain.service.AuthService
import io.micro.server.robot.domain.model.entity.FeatureFunctionDO
import io.micro.server.robot.domain.model.entity.RobotDO
import io.micro.server.robot.domain.model.valobj.RobotContacts
import io.micro.server.robot.domain.model.valobj.Switch
import io.micro.server.robot.domain.repository.IRobotManagerRepository
import io.micro.server.robot.domain.service.FunctionService
import io.micro.server.robot.domain.service.RobotManagerService
import io.micro.server.robot.infra.event.RobotEvent
import io.quarkus.hibernate.reactive.panache.common.WithSession
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.quarkus.logging.Log
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.smallrye.mutiny.replaceWithUnit
import io.smallrye.mutiny.subscription.MultiEmitter
import io.vertx.mutiny.core.Context
import io.vertx.mutiny.core.Vertx
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.sse.OutboundSseEvent
import jakarta.ws.rs.sse.Sse
import jakarta.ws.rs.sse.SseEventSink
import net.mamoe.mirai.contact.Contact.Companion.uploadImage
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.MessageSource.Key.quote
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.buildMessageChain
import net.mamoe.mirai.message.data.findIsInstance
import org.hibernate.reactive.mutiny.Mutiny
import org.hibernate.reactive.mutiny.Mutiny.SessionFactory
import java.util.*

/**
 *@author Augenstern
 *@since 2023/11/23
 */
@ApplicationScoped
class RobotManagerServiceImpl(
    private val factory: QQRobotFactory,
    private val manager: QQRobotManager,
    private val robotManagerRepository: IRobotManagerRepository,
    private val functionService: FunctionService,
    private val functionContext: FunctionContext,
    private val sessionFactory: Mutiny.SessionFactory,
    private val objectMapper: ObjectMapper,
    private val authService: AuthService,
    private val robotEvent: RobotEvent
) : RobotManagerService {

    /**
     * QQ号与半长连接的映射
     */
    private val oldSse = mutableMapOf<Long, Pair<Sse, SseEventSink>>()

    override fun loginQQRobot(code: String, sse: Sse, sink: SseEventSink): Multi<OutboundSseEvent> {
        return sessionFactory.withSession { robotManagerRepository.getLoginRobotId(code) }
            .onItem().transformToMulti { id ->
                requireNonNull(id, "登录码已过期请刷新")
                sessionFactory.withSession { robotManagerRepository.findRobotById(id) }
                    .onItem().transformToMulti { robotManager ->
                        if (robotManager == null) {
                            Multi.createFrom().items(sse.newEvent("fail#${CommonCode.NOT_FOUND.code}"))
                        } else {
                            // 先移除旧的连接
                            oldSse.remove(id)?.also { it.second.close() }
                            // 再关联新的连接
                            sse.newBroadcaster().register(sink)
                            oldSse[id] = sse to sink
                            val robot = manager.getRobot(id)
                            if (robot != null) {
                                if (robot.state() != Robot.State.Online) {
                                    // 已存在机器人且未登录，则先注销机器人，可以使登录二维码失效
                                    manager.unregisterRobot(id)
                                    // 开始扫码登录
                                    qqRobotQRLoginStart(robotManager, sse, sink)
                                } else {
                                    // 若机器人已登录，则重置二维码登录
                                    Multi.createFrom().items(sse.newEvent("reset#"))
                                }
                            } else {
                                // 若不存在机器人，则直接开始登录
                                qqRobotQRLoginStart(robotManager, sse, sink)
                            }
                        }
                    }
            }

    }

    @WithTransaction
    @WithSession
    override fun closeRobot(id: Long): Uni<Unit> {
        return robotManagerRepository.findRobotById(id).map { robotManager ->
            requireNonNull(robotManager, CommonMsg.NOT_FOUND_ROBOT)
            requireTure(value = AuthContext.equalId(robotManager.userId), code = CommonCode.ILLEGAL_OPERATION)
            manager.unregisterRobot(id)
        }.replaceWithUnit()
    }

    @WithSession
    override fun createRobot(robotDO: RobotDO): Uni<RobotDO> {
        if (robotDO.state == null) {
            robotDO.state = RobotDO.State.Create
        }
        val userId = AuthContext.id.toLongOrNull()
        requireNonNull(userId, CommonMsg.NULL_USER_ID)
        robotDO.userId = userId
        robotDO.paramVerify()
        robotDO.ensureCmdPrefix()
        val existRobot = robotManagerRepository.existRobotByAccountAndUserId(robotDO.account!!, userId)
        return existRobot.flatMap { exist ->
            if (exist) {
                fail(code = CommonCode.DUPLICATE, detail = CommonMsg.SAME_ACCOUNT_ROBOT)
            } else {
                robotManagerRepository.saveRobotWithUser(robotDO)
            }
        }
    }

    @WithTransaction
    @WithSession
    override fun getRobotList(robotDO: RobotDO, pageable: Pageable): Uni<PageDO<RobotDO>> {
        robotDO.userId = AuthContext.id.toLongOrNull()
        val paged = Page.of(pageable.current, pageable.limit)
        return robotManagerRepository.findRobotByExample(robotDO, paged)
            .flatMap { robots ->
                robotManagerRepository.countRobotByExample(robotDO, paged)
                    .map { count -> PageDO.of(pageable, count, robots) }
            }
    }

    @WithTransaction
    @WithSession
    override fun modifyRobotInfo(robotDO: RobotDO): Uni<RobotDO> {
        return robotManagerRepository.findRobotById(robotDO.id!!).flatMap { robot ->
            requireNonNull(robot, detail = CommonMsg.NOT_FOUND_ROBOT)
            requireTure(AuthContext.equalId(robot.userId), code = CommonCode.ILLEGAL_OPERATION)
            val modifyRobotDO = RobotDO().apply {
                id = robotDO.id
                name = robotDO.name
                password = robotDO.password
                remark = robotDO.remark
                robotDO.cmdPrefix?.also { cmdPrefix = it }
            }
            robotManagerRepository.updateRobot(modifyRobotDO)
        }
    }

    @WithTransaction
    @WithSession
    override fun addFeatureFunction(robotId: Long, featureFunctionDO: FeatureFunctionDO): Uni<Unit> {
        val userId = AuthContext.id.toLongOrNull()
        requireNonNull(userId, code = CommonCode.ILLEGAL_OPERATION)
        return robotManagerRepository.existRobotByRobotIdAndUserId(robotId, userId).flatMap {
            requireTure(it, CommonMsg.ILLEGAL_OPERATE_ROBOT, CommonCode.ILLEGAL_OPERATION)
            functionService.getUserFunctions()
        }.map { functionDOs ->
            val functionDO = functionDOs.associateBy { it.id }[featureFunctionDO.refId]
            requireNonNull(functionDO, CommonMsg.ILLEGAL_ADD_ROBOT_FEATURE, CommonCode.ILLEGAL_OPERATION)
            val code = functionDO.code
            requireNonNull(code)
            featureFunctionDO.ensureCmdAlias(code)
            featureFunctionDO.ensureTitle(code)
        }.flatMap {
            robotManagerRepository.findFeatureFunctionsByRobotId(robotId).flatMap { featureFunctions ->
                if (featureFunctions.map { it.refId }.contains(featureFunctionDO.refId)) {
                    Log.info("重复添加机器人功能")
                    Uni.createFrom().item(Unit)
                } else {
                    robotManagerRepository.addFeatureFunctionById(robotId, featureFunctionDO)
                }
            }
        }
    }

    @WithTransaction
    @WithSession
    override fun modifyFeatureFunction(robotId: Long, featureFunctionDO: FeatureFunctionDO): Uni<Unit> {
        return robotManagerRepository.findRobotById(robotId).flatMap { robot ->
            requireNonNull(robot, CommonMsg.NOT_FOUND_ROBOT)
            requireTure(
                AuthContext.equalId(robot.userId),
                CommonMsg.ILLEGAL_OPERATE_ROBOT,
                CommonCode.ILLEGAL_OPERATION
            )
            robot.functions.associateBy { it.id }[featureFunctionDO.id].also {
                if (it != null) {
                    it.enabled = featureFunctionDO.enabled
                    it.config = featureFunctionDO.config ?: it.config
                    it.remark = featureFunctionDO.remark ?: it.remark
                    it.requireQuota = featureFunctionDO.requireQuota ?: it.requireQuota
                    it.cmdAlias = featureFunctionDO.cmdAlias ?: it.cmdAlias
                    it.title = featureFunctionDO.title ?: it.title
                }
            }
            robotManagerRepository.updateRobot(robot).replaceWithUnit()
        }
    }

    @WithTransaction
    @WithSession
    override fun addOrModifyFunctionSwitch(id: Long, switch: Switch): Uni<Switch> {
        return robotManagerRepository.findRobotByUseFunctionId(id).flatMap { robotDO ->
            requireNonNull(robotDO)
            requireTure(AuthContext.equalId(robotDO.userId), code = CommonCode.ILLEGAL_OPERATION)
            robotManagerRepository.saveOrUpdateSwitchByFunctionId(id, switch)
        }
    }

    @WithTransaction
    @WithSession
    override fun getFunctionSwitch(id: Long): Uni<Switch> {
        return robotManagerRepository.findRobotByUseFunctionId(id).flatMap { robotDO ->
            requireNonNull(robotDO)
            requireTure(AuthContext.equalId(robotDO.userId), code = CommonCode.ILLEGAL_OPERATION)
            robotManagerRepository.findSwitchCacheByUseFunctionId(id)
        }
    }

    override fun loadContacts(id: Long): Uni<RobotContacts> {
        requireNonNull(id)
        val robot = manager.getRobot(id)
        if (robot == null || robot.state() != Robot.State.Online) {
            fail(CommonMsg.BOT_NOT_LOGGED_IN)
        }
        return Uni.createFrom().item {
            robot.loadContacts().run { RobotContacts(groupName, groups, friends) }
        }
    }

    @WithSession
    override fun getLoginCode(id: Long): Uni<String> {
        return robotManagerRepository.findRobotById(id).flatMap { robot ->
            requireNonNull(robot)
            requireTure(AuthContext.equalId(robot.userId), code = CommonCode.ILLEGAL_OPERATION)
            val code = IdUtil.fastSimpleUUID()
            robotManagerRepository.addLoginCode(code, id).replaceWith(code)
        }
    }

    @WithTransaction
    override fun modifyRobotState(id: Long, state: RobotDO.State): Uni<Unit> {
        return robotManagerRepository.updateRobotStateById(state, id).replaceWithUnit()
    }

    /**
     * 通过半长连接开始QQ扫码登录
     * @param robotDO 机器人
     * @param sse 半长连接
     * @return 消息事件
     */
    private fun qqRobotQRLoginStart(robotDO: RobotDO, sse: Sse, sink: SseEventSink): Multi<OutboundSseEvent> {
        val robotId = robotDO.id
        // 创建QQ机器人
        val robot = factory.create(Credential(robotId!!, robotDO.account!!)) as QQRobot
        val vertxContext = Vertx.currentContext()
        bindRobotStateChangeEvent(robot, vertxContext)
        return Multi.createFrom().emitter { em ->
            // 绑定登录回调函数
            qqRobotEventEmitBind(robotDO, robot, em, sink)
            // 群消息处理
            onGroupMessage(robot, robotId, vertxContext)
            // 注册机器人并开始登录
            manager.registerRobot(robot)
        }.map { str ->
            // 发送消息事件
            sse.newEvent(str)
        }
    }

    private fun bindRobotStateChangeEvent(robot: QQRobot, context: Context) {
        robot.addRobotStateChangeListener { state ->
            val s = when (state) {
                Robot.State.Create -> RobotDO.State.Create
                Robot.State.LoggingIn -> RobotDO.State.LoggingIn
                Robot.State.LoggingFail -> RobotDO.State.LoggingFail
                Robot.State.Online -> RobotDO.State.Online
                Robot.State.Closed -> RobotDO.State.Closed
            }
            sessionFactory.withSession { modifyRobotState(robot.id(), s) }
                .runSubscriptionOn { context.runOnContext(it) }
                .awaitSuspending()
            robotEvent.publishRobotStateChange(robot.id(), s).awaitSuspending()
        }
    }

    private fun onGroupMessage(robot: QQRobot, robotId: Long, vertxContext: Context) {
        robot.addGroupMessageListener { event ->
            Log.info(event.message)

            val latestRobot =
                fetchDatabase(sessionFactory, vertxContext) { robotManagerRepository.findRobotCacheById(robotId) }
            requireNonNull(latestRobot)
            val featureFunction = getFeatureFunction(event, latestRobot)
            if (featureFunction != null) {
                if (featureFunction.isDescription || featureFunction.isUndefined) {
                    val cmds = latestRobot.functions.map { it.cmd }.filterNotNull()
                    cacheCmdException(event, { functionContext.description(cmds) }) {
                        val messageChain = it.awaitSuspending()
                        group.sendMessage(messageChain.messages[0].msg)
                    }
                } else {
                    val authority = fetchDatabase(sessionFactory, vertxContext) {
                        authService.getAuthorityCacheByCode(featureFunction.code!!)
                    }
                    if (authority != null && authority.enabled == true) {
                        val switch = sessionFactory.withSession {
                            robotManagerRepository.findSwitchCacheByUseFunctionId(featureFunction.id!!)
                        }.runSubscriptionOn { vertxContext.runOnContext(it) }.awaitSuspending()
                        val switched = featureFunction.also { it.switch = switch }.switched()
                        if (switched) {
                            cacheCmdException(event, {
                                functionContext.call(
                                    featureFunction.cmd!!,
                                    featureFunction.args,
                                    featureFunction.getConfigMap(objectMapper)
                                ).awaitSuspending()
                            }) { messageChain ->
                                val requireQuota = featureFunction.requireQuota
                                val results = buildMessageChain {
                                    if (requireQuota == true) {
                                        add(event.message.quote())
                                    }
                                    messageChain.messages.forEach { message ->
                                        if (message.msg.isNotBlank()) {
                                            add(PlainText(message.msg))
                                        }
                                        if (message.data.type == MediaType.Picture) {
                                            val image = message.data.bytes
                                                .inputStream()
                                                .use { event.sender.uploadImage(it) }
                                            add(image)
                                        }
                                        if (message.card.id == CardID.Music) {
                                            TODO()
                                        }
                                    }
                                }

                                val receipt = group.sendMessage(results)

                                if (messageChain.receipt.recall > 0) {
                                    receipt.recallIn(messageChain.receipt.recall.toLong())
                                }
                            }
                        }
                    } else {
                        event.group.sendMessage("命令已被禁用")
                    }
                }
            }
        }
    }

    private suspend fun <T> fetchDatabase(
        sessionFactory: SessionFactory,
        vertxContext: Context,
        block: (Mutiny.Session) -> Uni<T>
    ): T? {
        return sessionFactory.withSession { block(it) }
            .runSubscriptionOn { vertxContext.runOnContext(it) }
            .awaitSuspending()
    }

    private suspend inline fun <T, E> cacheCmdException(
        event: GroupMessageEvent,
        block: () -> E,
        success: GroupMessageEvent.(E) -> T
    ) {
        runCatching { block() }
            .onSuccess { success(event, it) }
            .onFailure {
                if (it is CmdException) {
                    event.group.sendMessage(it.message.toString())
                } else {
                    Log.error("function异常：", it)
                }
            }
    }

    private fun getFeatureFunction(event: GroupMessageEvent, latestRobot: RobotDO): FeatureFunctionDO? {
        val text = event.message.findIsInstance<PlainText>().toString()
        val at = event.message.findIsInstance<At>()
        var featureFunction: FeatureFunctionDO? = null
        if (at != null) {
            if (at.target == latestRobot.id) {
                featureFunction = FeatureFunctionDO().also { it.isDescription = true }
            }
        } else {
            featureFunction = latestRobot.resolveCommand(event.group.id, event.sender.id, text)
        }
        return featureFunction
    }

    /**
     * QQ机器人登录事件绑定
     * @param robot QQ机器人
     * @param em 事件触发器
     */
    private fun qqRobotEventEmitBind(
        robotDO: RobotDO,
        robot: QQRobot,
        em: MultiEmitter<in String>,
        sink: SseEventSink
    ) {
        val id = robot.id()
        robot.addLoginInStateChangeListener { event ->
            when (event) {
                is QQRobot.QRCodeStartEvent -> {
                    val qrCode = Base64.getEncoder().encodeToString(event.qr)
                    em.emit("qr#$qrCode")
                }

                is QQRobot.QRCodeWaitingConfirmEvent -> {
                    em.emit("waiting_confirm#")
                }

                is QQRobot.QRCodeConfirmedEvent -> {
                    em.emit("confirmed#")
                }

                is QQRobot.QRCodeCancelledEvent -> {
                    em.emit("cancelled#")
                }

                is QQRobot.QRCodeTimeoutEvent -> {
                    em.emit("qr_timeout#")
                }

                is QQRobot.LoginTimeoutEvent -> {
                    em.emit("login_timeout#")
                    em.complete()
                    manager.unregisterRobot(id)
                }

                is QQRobot.LoginSuccessEvent -> {
                    robotEvent.publishRobotLoginSuccess(robotDO).awaitSuspending()
                    em.emit("success#")
                    sink.close()
                }

                is QQRobot.LoginFailEvent -> {
                    em.emit("fail#${event.ex.message}")
                    sink.close()
                }
            }
        }
    }
}