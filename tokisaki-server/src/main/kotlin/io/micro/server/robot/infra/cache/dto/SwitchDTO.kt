package io.micro.server.robot.infra.cache.dto

import kotlinx.serialization.Serializable

@Serializable
class SwitchDTO {

    var enableGroups: MutableSet<Long> = mutableSetOf()

    var disableGroups: MutableSet<Long> = mutableSetOf()

    var enableMembers: MutableSet<Long> = mutableSetOf()

    var disableMembers: MutableSet<Long> = mutableSetOf()

}