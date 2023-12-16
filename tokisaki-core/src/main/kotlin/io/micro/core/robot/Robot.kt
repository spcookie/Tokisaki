package io.micro.core.robot

/**
 *@author Augenstern
 *@since 2023/11/21
 */
interface Robot {

    /**
     * 机器人状态
     */
    fun state(): State

    /**
     * 机器人标识
     */
    fun identify(): String

    /**
     * 登录
     */
    fun login()

    /**
     * 下线并关闭机器人
     */
    fun close()

    interface LifeCycle {

        /**
         * 登录时监听器
         */
        fun loggingInListener(event: Event) {}

        /**
         * 在线时监听器
         */
        fun onlineListener(event: Event) {}

    }

    interface Event {
        operator fun <K, V> get(key: K): V? = throw IllegalCallerException()
        operator fun <K, V> set(key: K, value: V) {}
    }

    enum class State {
        Create,
        LoggingIn,
        LoggingFail,
        Online,
        Offline,
        Closed
    }
}