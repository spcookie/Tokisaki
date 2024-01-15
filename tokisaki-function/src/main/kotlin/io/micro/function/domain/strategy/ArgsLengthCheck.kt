package io.micro.function.domain.strategy

import com.vdurmont.emoji.EmojiParser
import io.micro.core.exception.CmdException
import io.micro.core.function.sdk.Cmd
import io.quarkus.arc.Arc
import io.quarkus.arc.Unremovable
import jakarta.enterprise.context.ApplicationScoped
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Valid
import jakarta.validation.constraints.Size

/**
 *@author Augenstern
 *@since 2023/10/19
 */
@Unremovable
@ApplicationScoped
class ArgsLengthCheck {
    companion object {
        fun check(cmd: String, args: List<String>) {
            val instance = Arc.container().instance(ArgsLengthCheck::class.java)
            if (instance.isAvailable) {
                val inspector = instance.get()
                try {
                    when {
                        cmd.lowercase() == Cmd.Mj.cmd -> inspector.mj(args)
                        cmd.lowercase() == Cmd.Chat.cmd -> inspector.chat(args)
                        cmd.lowercase() == Cmd.Emoji.cmd -> inspector.emoji(args)
                    }
                } catch (ex: ConstraintViolationException) {
                    CmdException.fail(ex)
                }
            }
        }
    }

    fun mj(@Valid @Size(message = "请输入提示词", min = 1) args: List<String>) {}

    fun chat(@Valid @Size(message = "请输入聊天内容", min = 1) args: List<String>) {}

    fun emoji(@Valid @Size(message = "请输入emoji表情", min = 1) args: List<String>) {
        if (EmojiParser.extractEmojis(args[0]).size < 2) {
            CmdException.fail("需要两个emoji表情")
        }
    }
}