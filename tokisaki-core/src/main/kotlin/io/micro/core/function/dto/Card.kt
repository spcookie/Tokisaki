package io.micro.core.function.dto


/**
 *@author Augenstern
 *@since 2023/10/8
 */
data class Card(var id: CardID = CardID.None, var content: Map<String, String> = mapOf())
