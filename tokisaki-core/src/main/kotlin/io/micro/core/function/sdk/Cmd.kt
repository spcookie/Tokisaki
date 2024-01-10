package io.micro.core.function.sdk

/**
 *@author Augenstern
 *@since 2023/10/10
 */
enum class Cmd(val code: String, val auth: String) {
    Girl("g", "FUN:GIRL"),
    Mj("mj", "FUN:MJ"),
    Chat("c", "FUN:CHAT"),
    Anime("ac", "FUN:ANIME"),
    Emoji("e", "FUN:EMOJI");

    companion object {

        fun byCode(code: String?): Cmd? {
            return Cmd.entries.filter { it.code == code }.firstOrNull()
        }

        fun byAuth(auth: String?): Cmd? {
            return Cmd.entries.filter { it.auth == auth }.firstOrNull()
        }

    }

}