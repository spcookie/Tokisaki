package io.micro.core.robot.qq

import io.micro.core.exception.fail
import io.micro.core.rest.CommonCode
import io.micro.core.robot.Contacts
import io.micro.core.robot.Robot
import io.quarkus.logging.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.event.events.GroupMessageEvent

/**
 * QQ机器人适配器，适配Mirai机器人
 *@author Augenstern
 *@since 2023/11/21
 */
class QQRobot(private val id: Long, private val account: String) : Robot, Robot.LifeCycle {

    companion object {
        // 登录超时时间 2m
        private const val LOGIN_TIMEOUT = 1000 * 60 * 1L
    }

    /**
     * 协程登录锁
     */
    private val mutex = Mutex()

    /**
     * 一个机器人开启一个协程
     */
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + CoroutineName("qq-robot-$id"))

    /**
     * Mirai机器人
     */
    lateinit var bot: Bot

    /**
     * 机器人状态
     */
    @Volatile
    lateinit var state: Robot.State

    /**
     * 收到群消息回调
     */
    var onGroupMessage: (suspend (GroupMessageEvent) -> Unit)? = null

    private var onStateChangeList: MutableList<suspend (Robot.Event) -> Unit> = mutableListOf()

    /**
     * 二维码获取开始事件
     */
    open class QRCodeStartEvent(val qr: ByteArray) : Robot.Event

    /**
     * 登录成功事件
     */
    class LoginSuccessEvent : Robot.Event

    /**
     * 登录失败事件
     */
    class LoginFailEvent(val ex: Exception) : Robot.Event

    /**
     * 登录超时事件
     */
    class LoginTimeoutEvent : Robot.Event

    /**
     * 收到在线群消息事件
     */
    class OnlineGroupMessageEvent(val event: GroupMessageEvent) : Robot.Event

    override fun id(): Long {
        return id
    }

    override fun state(): Robot.State {
        return state
    }

    override fun account(): String {
        return account
    }

    override fun login() {
        val robot = this@QQRobot
        if (canLogin()) {
            scope.launch {
                // 同一时间只允许一个协程开启登录
                mutex.withLock {
                    if (canLogin()) {
                        robot.state = Robot.State.LoggingIn
                        try {
                            // 登录
                            withTimeOutCloseLogin()
                            loggingInListener(LoginSuccessEvent())
                        } catch (ex: Exception) {
                            loggingInListener(LoginFailEvent(ex))
                        }
                    } else {
                        fail(code = CommonCode.DUPLICATE)
                    }
                }
            }
        } else {
            fail(code = CommonCode.DUPLICATE)
        }
    }

    override fun close() {
        bot.close()
    }

    override fun addStateChangeListener(block: suspend (event: Robot.Event) -> Unit) {
        onStateChangeList += block
    }

    override fun loadContacts(): Contacts {
        val friends = buildMap {
            bot.friends.forEach { put(it.id, it.nick) }
        }
        val groups = buildMap {
            bot.groups.forEach { put(it.id, it.members.map(Member::id)) }
        }
        val groupName = buildMap {
            bot.groups.forEach { put(it.id, it.name) }
        }
        return Contacts(groupName, groups, friends)
    }

    /**
     * 根据状态判断是否能登录
     */
    private fun canLogin() = state != Robot.State.LoggingIn || state != Robot.State.Online

    override fun loggingInListener(event: Robot.Event) {
        scope.launch {
            onStateChangeList.forEach { handle ->
                when (event) {
                    is QRCodeStartEvent -> {
                        try {
                            handle(event)
                        } catch (ex: Exception) {
                            this@QQRobot.state = Robot.State.LoggingFail
                            loggingInListener(LoginFailEvent(ex))
                        }
                    }

                    is LoginSuccessEvent -> {
                        try {
                            handle(event)
                        } catch (ex: Exception) {
                            Log.error(ex)
                        }
                        state = Robot.State.Online
                    }

                    is LoginFailEvent -> {
                        try {
                            handle(event)
                        } catch (ex: Exception) {
                            Log.error(ex)
                        }
                        state = Robot.State.LoggingFail
                    }

                    is LoginTimeoutEvent -> {
                        state = Robot.State.Closed
                        bot.close()
                        try {
                            handle(event)
                        } catch (ex: Exception) {
                            Log.error(ex)
                        }
                    }
                }
            }
        }
    }

    override fun onlineListener(event: Robot.Event) {
        when (event) {
            is OnlineGroupMessageEvent -> {
                val e = event.event
                val handle = onGroupMessage
                if (handle != null) {
                    scope.launch {
                        handle(e)
                    }
                }
            }
        }
    }

    /**
     * 登录，超时发布登录超时事件
     */
    private suspend inline fun withTimeOutCloseLogin() {
        // 开启一个协程延时发布超时事件
        val job = scope.launch {
            delay(LOGIN_TIMEOUT)
            loggingInListener(LoginTimeoutEvent())
        }
        // QQ机器人登录
        bot.login()
        // 登录成功后取消发布超时事件
        job.cancel()
    }
}