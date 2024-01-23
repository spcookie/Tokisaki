package io.micro.server.robot.domain.model.valobj


data class RobotContacts(

    var groupName: Map<Long, String>,

    var groups: Map<Long, List<Long>>,

    var friends: Map<Long, String>

)