package io.micro.server.robot.infra.event.sdk

object RobotConstant {

    object Exchange {

        const val ROBOT = "robot"

        const val QQ_ROBOT = "qq-robot"

    }

    object RotingKey {

        const val ROBOT_LOGIN_SUCCESS = "robot.login.success"

        const val ROBOT_STATE_CHANGE = "robot.state.change"

    }

    object Queue {

        const val ROBOT_LOGIN_SUCCESS = "robot-login-success"

        const val ROBOT_STATE_CHANGE = "robot-state-change"

    }

}