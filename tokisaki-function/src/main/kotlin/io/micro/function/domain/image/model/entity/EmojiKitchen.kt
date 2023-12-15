package io.micro.function.domain.image.model.entity

import com.vdurmont.emoji.EmojiParser
import io.micro.core.fundto.Cmd
import io.micro.core.funsdk.AbstractCmd
import io.micro.core.funsdk.ArgsMergeStrategy
import io.micro.function.domain.image.model.Image
import io.micro.function.domain.image.model.valobj.EmojiCombination
import io.micro.function.domain.image.model.valobj.EmojiData
import io.micro.function.domain.image.model.valobj.EmojiMetadata
import io.micro.function.domain.image.model.valobj.GoogleRequestParam
import io.net.spcokie.common.exception.CmdException
import io.net.spcokie.types.QQFace
import io.smallrye.mutiny.Uni

/**
 *@author Augenstern
 *@since 2023/10/23
 */
class EmojiKitchen : Image() {

    lateinit var googleRequestParam: GoogleRequestParam
        private set

    companion object : AbstractCmd() {

        lateinit var emojiMetadata: EmojiMetadata

        const val EMOJI_METADATA = "metadata.json"

        @JvmStatic
        override fun identify() = Cmd.Emoji

        @JvmStatic
        override fun describe(call: Long): Uni<String> {
            return Uni.createFrom().item {
                """
                ${QQFace.SHUANG_YU}Emoji合成 ◟$call◞
                    ╰e(emoji)
                      └emoji-要合成的2个emoji
                """.trimIndent()
            }
        }

        @JvmStatic
        override fun args(): ArgsMergeStrategy {
            return ArgsMergeStrategy.Connect
        }

        fun build(args: MutableList<String>): EmojiKitchen {
            val newArgs = args().strategy(args)
            val (left, right) = parserEmojis(newArgs[0])
            val leftEmojiCodepoint = getCodePoint(left)
            val rightEmojiCodepoint = getCodePoint(right)
            if (isSupportedEmoji(leftEmojiCodepoint) && isSupportedEmoji(rightEmojiCodepoint)) {
                val emojiCombination =
                    findValidEmojiCombo(leftEmojiCodepoint, rightEmojiCodepoint) ?: CmdException.fail("没有合成表情")
                return EmojiKitchen().apply {
                    googleRequestParam = getGoogleRequestParam(emojiCombination)
                }
            } else {
                CmdException.fail("不支持的Emoji")
            }
        }

        private fun parserEmojis(str: String): List<String> {
            return EmojiParser.extractEmojis(str)
        }

        private fun getEmojiData(emojiCodepoint: String): EmojiData {
            return emojiMetadata.data.getValue(emojiCodepoint)
        }

        private fun isSupportedEmoji(emojiCodepoint: String): Boolean {
            return emojiMetadata.knownSupportedEmoji.contains(emojiCodepoint)
        }

        private fun findValidEmojiCombo(leftEmojiCodepoint: String, rightEmojiCodepoint: String): EmojiCombination? {
            val combinations = getEmojiData(leftEmojiCodepoint).combinations
            var result = combinations
                .filter { combination ->
                    combination.leftEmojiCodepoint == leftEmojiCodepoint && combination.rightEmojiCodepoint == rightEmojiCodepoint
                }
                .maxByOrNull { combination ->
                    combination.date
                }
            if (result == null) {
                result = combinations
                    .filter { combination ->
                        combination.leftEmojiCodepoint == rightEmojiCodepoint && combination.rightEmojiCodepoint == leftEmojiCodepoint
                    }
                    .maxByOrNull { combination ->
                        combination.date
                    }
            }
            return result
        }

        private fun getGoogleRequestParam(combo: EmojiCombination): GoogleRequestParam {
            return GoogleRequestParam(combo.leftEmojiCodepoint, combo.rightEmojiCodepoint, combo.date).unicode()
        }

        private fun getCodePoint(emoji: String): String {
            return emoji.codePoints()
                .mapToObj { it.toString(16) }
                .reduce { left, right -> "$left-$right" }
                .get()
        }

    }

}