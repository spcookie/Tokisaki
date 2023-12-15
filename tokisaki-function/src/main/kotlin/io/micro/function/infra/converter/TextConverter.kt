package io.net.spcokie.infra.converter

import io.micro.function.domain.text.model.valobj.ChatCompletionRequest
import io.micro.function.domain.text.model.valobj.ChatCompletionResult
import io.micro.function.domain.text.model.valobj.ChatMessage
import io.micro.function.infra.adapter.dto.ChatCompletionRequestDTO
import io.micro.function.infra.adapter.dto.ChatCompletionResultDTO
import io.micro.function.infra.adapter.dto.ChatMessageDTO
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants

/**
 *@author Augenstern
 *@since 2023/10/21
 */
@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
interface TextConverter {
    fun toChatCompletionRequestDTO(chatCompletionRequest: ChatCompletionRequest): ChatCompletionRequestDTO

    fun toChatCompletionResultDO(chatCompletionRequest: ChatCompletionResultDTO): ChatCompletionResult

    fun toChatPairMessageDTOList(list: List<Pair<ChatMessage, Long>>): MutableList<Pair<ChatMessageDTO, Long>>

    fun toChatPairMessageDOList(list: List<Pair<ChatMessageDTO, Long>>): MutableList<Pair<ChatMessage, Long>>
}