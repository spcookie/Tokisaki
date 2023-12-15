package io.micro.function.domain.strategy

import io.micro.core.context.AuthContext
import io.micro.core.fundto.Cmd
import io.micro.core.fundto.Message
import io.micro.core.fundto.MessageChain
import io.micro.core.funsdk.CommandService
import io.micro.function.domain.image.service.ImageTask
import io.net.spcokie.common.exception.CmdException
import io.quarkus.arc.All
import io.smallrye.mutiny.Uni
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@ApplicationScoped
class CallContextImpl(
    @All private val commandServices: MutableList<CommandService>
) : CallService {

    private val allocationCommandServices: MutableMap<String, CommandService> = mutableMapOf()

    private val allocationImageTask: MutableMap<Cmd, ImageTask> = mutableMapOf()

    @PostConstruct
    fun allocateCommandServices() {
        for (commandService in commandServices) {
            allocationCommandServices[commandService.cmd().code] = commandService
            if (commandService is ImageTask) {
                allocationImageTask[commandService.cmd()] = commandService
            }
        }
    }

    override fun call(cmd: String, args: MutableList<String>): Uni<MessageChain> {
        return if (AuthContext.hasRole(cmd)) {
            ArgsLengthCheck.check(cmd, args)
            with(allocationCommandServices[cmd.lowercase()]) {
                this?.invoke(args) ?: CmdException.nonImplemented()
            }
        } else {
            menu()
        }
    }

    override fun menu(): Uni<MessageChain> {
        val unis = buildList {
            commandServices.forEach {
                if (AuthContext.hasRole(it.cmd().code)) {
                    add(it.describe())
                }
            }
        }
        return Uni.join().all(unis).andFailFast()
            .map { list ->
                val str = buildString {
                    appendLine()
                    list.forEach {
                        appendLine(it)
                    }
                    appendLine(
                        """
                 ／l、
              （ﾟ､ 。 ７
              　l、 ~ヽ
              　じしf_, )ノ
                            """
                    )
                    append(
                        """
                            ∷ ${
                            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
                                .format(LocalDateTime.now())
                        }
                            ∷ v8.8.8
                            ∷ Mirai&Spring&Quarkus
                        """.trimIndent()
                    )
                }
                MessageChain().apply {
                    messages = mutableListOf(Message(msg = str))
                }
            }
    }

    override fun downloadImage(cmd: Cmd): Uni<Void> {
        return with(allocationImageTask[cmd]) {
            this?.download() ?: CmdException.nonImplemented()
        }
    }

    override fun removeImage(cmd: Cmd): Uni<Void> {
        return with(allocationImageTask[cmd]) {
            this?.remove() ?: CmdException.nonImplemented()
        }
    }
}