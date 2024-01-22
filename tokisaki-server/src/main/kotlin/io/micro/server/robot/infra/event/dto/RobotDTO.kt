package io.micro.server.robot.infra.event.dto

class RobotDTO {

    var id: Long? = null

    var userId: Long? = null

    var name: String? = null

    var account: String? = null

    var password: String? = null

    var type: Int? = null

    var state: Int? = null

    var remark: String? = null

    val functions: MutableList<FeatureFunctionDTO> = mutableListOf()

    var cmdPrefix: String? = null

}