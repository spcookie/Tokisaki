package io.micro.function.domain.strategy

import io.micro.core.exception.CmdException
import io.micro.core.function.ConfigHint
import io.micro.core.function.dto.Message
import io.micro.core.function.dto.MessageChain
import io.micro.core.function.sdk.Cmd
import io.micro.core.function.sdk.CommandService
import io.micro.function.domain.image.service.ImageTask
import io.quarkus.arc.All
import io.smallrye.mutiny.Uni
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@ApplicationScoped
class FunctionContextImpl(
    @All private val commandServices: MutableList<CommandService>
) : FunctionContext {

    private val allocationCommandServices: MutableMap<String, CommandService> = mutableMapOf()

    private val allocationImageTask: MutableMap<Cmd, ImageTask> = mutableMapOf()

    @PostConstruct
    fun allocateCommandServices() {
        for (commandService in commandServices) {
            allocationCommandServices[commandService.cmd().cmd] = commandService
            if (commandService is ImageTask) {
                allocationImageTask[commandService.cmd()] = commandService
            }
        }
    }

    override fun call(cmd: Cmd, args: MutableList<String>, config: Map<String, *>): Uni<MessageChain> {
        val cmd = cmd.cmd
        ArgsLengthCheck.check(cmd, args)
        return with(allocationCommandServices[cmd.lowercase()]) {
            this?.invoke(args, config) ?: CmdException.nonImplemented()
        }
    }

    override fun config(cmd: Cmd): ConfigHint? {
        return with(allocationCommandServices[cmd.cmd.lowercase()]) { this?.configHint() }
    }

    override fun description(cmds: List<Cmd>): Uni<MessageChain> {
        val unis = buildList {
            commandServices.forEach {
                if (cmds.contains(it.cmd())) {
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
                            ∷ Mirai&Quarkus
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