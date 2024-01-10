package io.micro.core.function

import io.micro.core.function.sdk.Cmd
import kotlinx.serialization.Serializable

enum class ConfigHint(val cmd: Cmd, val hints: List<Hint>) {

    CHAT(Cmd.Chat, listOf(Hint.MODEL, Hint.MAX_TOKENS, Hint.API_KEY));

    @Serializable
    data class Hint(
        val key: String,
        val value: String,
        val formType: FormType,
        val candidateValue: List<CandidateValue> = listOf()
    ) {

        companion object {

            val MODEL = Hint("model", "模型", FormType.SELECT, listOf(CandidateValue.GPT35, CandidateValue.GPT35_16K))

            val MAX_TOKENS = Hint("maxTokens", "最大Token限制", FormType.NUMBER)

            val API_KEY = Hint("apiKey", "api密钥", FormType.TEXT)
        }

    }

    enum class FormType { SELECT, TEXT, NUMBER }

    @Serializable
    data class CandidateValue(val code: String, val description: String) {

        companion object {
            val GPT35 = CandidateValue("TK001", "gpt-3.5-turbo")

            val GPT35_16K = CandidateValue("TK002", "gpt-3.5-turbo-16k")
        }

    }

}