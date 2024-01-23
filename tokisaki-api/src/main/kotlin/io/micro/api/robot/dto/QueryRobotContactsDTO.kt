package io.micro.api.robot.dto

import kotlinx.serialization.Serializable

@Serializable
class QueryRobotContactsDTO {

    var groupName: Map<Long, String>? = null

    var groups: Map<Long, List<Long>>? = null

    var friends: Map<Long, String>? = null

}