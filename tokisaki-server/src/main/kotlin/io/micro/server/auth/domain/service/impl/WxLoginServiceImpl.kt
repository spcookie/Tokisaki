package io.micro.server.auth.domain.service.impl

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.micro.core.exception.fail
import io.micro.core.exception.requireNonNull
import io.micro.server.auth.domain.model.entity.WXLoginUserDO
import io.micro.server.auth.domain.model.valobj.WXMessage
import io.micro.server.auth.domain.repository.IAuthRepository
import io.micro.server.auth.domain.service.WxLoginService
import io.micro.server.dict.domain.service.SystemDictService
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.replaceWithUnit
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.sse.OutboundSseEvent
import jakarta.ws.rs.sse.Sse
import jakarta.ws.rs.sse.SseEventSink

/**
 *@author Augenstern
 *@since 2023/11/25
 */
@ApplicationScoped
class WxLoginServiceImpl(
    private val authRepository: IAuthRepository,
    private val systemDictService: SystemDictService,
    private val objectMapper: ObjectMapper
) : WxLoginService {

    companion object {
        private const val DEFAULT_FUN = "DEFAULT_FUN"
        private const val EMPTY_LIST = "[]"
    }

    override fun loginSubscript(driveId: String, sse: Sse, sink: SseEventSink): Multi<OutboundSseEvent> {
        return WXLoginUserDO.loginStart(driveId, sse, sink)
    }

    @WithTransaction
    override fun login(wxMessage: WXMessage, sse: Sse): Uni<WXLoginUserDO> {
        val code = wxMessage.content
        val openId = wxMessage.fromUserName
        return if (WXLoginUserDO.containsCode(code)) {
            loginUser(openId).invoke { user -> WXLoginUserDO.loginEnd(code, user.token, sse) }
        } else {
            Uni.createFrom().nullItem()
        }
    }

    override fun refreshCode(driveId: String, sse: Sse): Uni<Unit> {
        return Uni.createFrom()
            .item(WXLoginUserDO.refreshCode(driveId, sse))
            .replaceWithUnit()
    }

    private fun loginUser(openId: String): Uni<WXLoginUserDO> {
        return authRepository.findWXLoginUserByOpenid(openId)
            .flatMap { user ->
                if (user == null) {
                    registryUser(openId)
                } else {
                    Uni.createFrom().item(user)
                }
            }
            .map {
                it.apply {
                    token = it.generateToken()
                }
            }
    }

    private fun registryUser(openId: String): Uni<WXLoginUserDO> =
        systemDictService.findDictByKey(DEFAULT_FUN).flatMap { dict ->
            val value = dict.value ?: EMPTY_LIST
            val dictEnum = dict.type
            requireNonNull(dictEnum)
            try {
                val config = objectMapper.readValue(value, dictEnum.type.java)
                if (config is List<*>) {
                    val authorities = config.map {
                        if (it is String) {
                            it
                        } else {
                            failCastAuthoritiesDict()
                        }
                    }.toMutableSet()
                    val defaultUser = WXLoginUserDO.defaultUser(openId, authorities)
                    val invokes = buildList {
                        defaultUser.authorities.forEach { auth ->
                            val invoke = authRepository.findAuthorityByExample(auth)
                                .map { it.first() }
                                .map { it.id }
                                .invoke { id -> auth.id = id }
                            add(invoke)
                        }
                    }
                    recursChain(invokes.toMutableList()).flatMap {
                        authRepository.registerWXLoginUser(defaultUser)
                    }
                } else {
                    failCastAuthoritiesDict()
                }
            } catch (e: JsonProcessingException) {
                failCastAuthoritiesDict()
            } catch (e: JsonMappingException) {
                failCastAuthoritiesDict()
            }
        }

    private fun recursChain(invokes: MutableList<Uni<Long?>>): Uni<MutableList<Long?>> {
        if (invokes.isEmpty()) {
            return Uni.createFrom().item(mutableListOf())
        }
        return invokes.removeFirst().flatMap {
            recursChain(invokes).invoke { ids ->
                ids.add(it)
            }
        }
    }

    private fun failCastAuthoritiesDict(): Nothing {
        fail("读取用户默认机器人功能权限失败")
    }

}