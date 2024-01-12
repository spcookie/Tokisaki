package io.micro.core.function.sdk

/**
 *@author Augenstern
 *@since 2023/10/10
 */
enum class Cmd(val cmd: String, val code: String) {
    Girl("g", "FUN:GIRL"),
    Mj("mj", "FUN:MJ"),
    Chat("c", "FUN:CHAT"),
    Anime("ac", "FUN:ANIME"),
    Emoji("e", "FUN:EMOJI");

    companion object {

        fun byCmd(cmd: String?): Cmd? {
            return Cmd.entries.filter { it.cmd == cmd }.firstOrNull()
        }

        fun byCode(code: String?): Cmd? {
            return Cmd.entries.filter { it.code == code }.firstOrNull()
        }

    }

}