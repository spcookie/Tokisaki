package io.micro.server.robot.domain.model.valobj

data class Switch(

    var enableGroups: MutableSet<Long> = mutableSetOf(),

    var disableGroups: MutableSet<Long> = mutableSetOf(),

    var enableMembers: MutableSet<Long> = mutableSetOf(),

    var disableMembers: MutableSet<Long> = mutableSetOf()

)
