package io.micro.core.funsdk

/**
 *@author Augenstern
 *@since 2023/10/10
 */
enum class Cmd(val code: String, auth: String) {
    Girl("g", "FUN:GIRL"),
    Mj("mj", "FUN:MJ"),
    Chat("c", "FUN:CHAT"),
    Anime("ac", "FUN:ANIME"),
    Emoji("e", "FUN:EMOJI")
}