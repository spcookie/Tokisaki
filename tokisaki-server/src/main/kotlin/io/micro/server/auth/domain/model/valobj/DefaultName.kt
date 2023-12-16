package io.micro.server.auth.domain.model.valobj

object DefaultName {
    private val adjectives = arrayOf(
        "飞扬的", "悠闲的", "神秘的", "狂野的", "迷幻的",
        "璀璨的", "奇幻的", "无敌的", "闪耀的", "魔幻的"
    )
    private val nouns = arrayOf(
        "星辰", "幻境", "宇宙", "仙境", "梦想",
        "奇迹", "传说", "天堂", "灵感", "奇遇"
    )
    private val actions = arrayOf(
        "舞动", "徜徉", "翱翔", "启程", "探索",
        "绽放", "追逐", "狂欢", "飞跃", "超越"
    )

    fun generate(): String {
        val randomAdjective = adjectives.random()
        val randomNoun = nouns.random()
        val randomAction = actions.random()
        return "$randomAdjective$randomNoun$randomAction"
    }

}