package io.micro.function.domain.text.model.valobj

data class ChatCompletionRequest(

    var key: String? = null,

    /**
     * ID of the model to use.
     */
    var model: String? = null,

    /**
     * The messages to generate chat completions for, in the [chat format](https://platform.openai.com/docs/guides/chat/introduction).<br></br>
     * see [ChatMessage]
     */
    var messages: List<ChatMessage>? = null,

    /**
     * What sampling temperature to use, between 0 and 2. Higher values like 0.8 will make the output more random, while lower
     * values like 0.2 will make it more focused and deterministic.<br></br>
     * We generally recommend altering this or top_p but not both.
     */
    var temperature: Double? = null,

    /**
     * An alternative to sampling with temperature, called nucleus sampling, where the model considers the results of the tokens
     * with top_p probability mass. So 0.1 means only the tokens comprising the top 10% probability mass are considered.<br></br>
     * We generally recommend altering this or temperature but not both.
     */
    var topP: Double? = null,

    /**
     * How many chat completion chatCompletionChoices to generate for each input message.
     */
    var n: Int? = null,

    /**
     * If set, partial message deltas will be sent, like in ChatGPT. Tokens will be sent as data-only [server-sent
     * events](https://developer.mozilla.org/en-US/docs/Web/API/Server-sent_events/Using_server-sent_events#Event_stream_format) as they become available, with the stream terminated by a data: [DONE] message.
     */
    var stream: Boolean? = null,

    /**
     * Up to 4 sequences where the API will stop generating further tokens.
     */
    var stop: List<String>? = null,

    /**
     * The maximum number of tokens allowed for the generated answer. By default, the number of tokens the model can return will
     * be (4096 - prompt tokens).
     */
    var maxTokens: Int? = null,

    /**
     * Number between -2.0 and 2.0. Positive values penalize new tokens based on whether they appear in the text so far,
     * increasing the model's likelihood to talk about new topics.
     */
    var presencePenalty: Double? = null,

    /**
     * Number between -2.0 and 2.0. Positive values penalize new tokens based on their existing frequency in the text so far,
     * decreasing the model's likelihood to repeat the same line verbatim.
     */
    var frequencyPenalty: Double? = null,

    /**
     * Accepts a json object that maps tokens (specified by their token ID in the tokenizer) to an associated bias value from -100
     * to 100. Mathematically, the bias is added to the logits generated by the model prior to sampling. The exact effect will
     * vary per model, but values between -1 and 1 should decrease or increase likelihood of selection; values like -100 or 100
     * should result in a ban or exclusive selection of the relevant token.
     */
    var logitBias: Map<String, Int>? = null,

    /**
     * A unique identifier representing your end-user, which will help OpenAI to monitor and detect abuse.
     */
    var user: String? = null,
)