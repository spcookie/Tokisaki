package io.micro.function.domain.text.service

import io.micro.core.annotation.CallCount
import io.micro.core.context.AuthContext
import io.micro.core.fundto.Cmd
import io.micro.core.fundto.MessageChain
import io.micro.core.funsdk.CommandService
import io.micro.function.domain.text.adapter.TextAdapter
import io.micro.function.domain.text.model.entity.Chat
import io.micro.function.domain.text.model.valobj.ChatConfig
import io.micro.function.domain.text.repository.TextRepository
import io.net.spcokie.common.exception.CmdException
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.faulttolerance.api.RateLimit
import io.smallrye.faulttolerance.api.RateLimitException
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.faulttolerance.Fallback
import java.time.temporal.ChronoUnit
import java.util.function.Supplier

/**
 *@author Augenstern
 *@since 2023/10/20
 */
@ApplicationScoped
class ChatServiceImpl(
    private val textRepository: TextRepository,
    private val textAdapter: TextAdapter,
    private val chatConfig: ChatConfig
) : CommandService {
    override fun cmd(): Cmd {
        return Chat.identify()
    }

    override fun describe(): Uni<String> {
        return textRepository.findCallStatistic(cmd().code).flatMap {
            Chat.describe(it)
        }
    }

    @CallCount
    @WithTransaction
    @Fallback(fallbackMethod = "fallback", applyOn = [RateLimitException::class])
    @RateLimit(value = 9, windowUnit = ChronoUnit.MINUTES, minSpacing = 8)
    override fun invoke(args: MutableList<String>): Uni<MessageChain> {
        val tenant = AuthContext.tenant
        return textRepository.requireTenantLock(tenant)
            .flatMap { lock ->
                if (lock) {
                    textRepository.findTenantMessage(tenant)
                } else {
                    Uni.createFrom().failure(CmdException.failForUni("同一时间只允许一条对话"))
                }
            }.onItem().ifNull().continueWith(mutableListOf())
            .map { history ->
                Chat.create(chatConfig, args, history)
            }
            .flatMap { chat ->
                val request = chat.prepareRequest()
                textAdapter.processRequest(request).flatMap { result ->
                    val contentResult = result.choices?.get(0)?.message?.content!!
                    val usage = result.usage!!
                    chat.attachResultMessage(contentResult, usage)
                    textRepository.saveTenantMessage(tenant, chat.messages).map { chat }
                }
            }
            .onTermination().call(Supplier { textRepository.releaseTenantLock(tenant) })
            .map { MessageChain.text(it.content) }
    }

    fun fallback(args: MutableList<String>, rateLimitException: RateLimitException): Uni<MessageChain> {
        return Uni.createFrom().failure(CmdException.failForUni("命令“c”已达到速率限制"))
    }
}