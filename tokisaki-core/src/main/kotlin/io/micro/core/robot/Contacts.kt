package io.micro.core.robot


data class Contacts(
    var groupName: Map<Long, String>,

    var groups: Map<Long, List<Long>>,

    var friends: Map<Long, String>
)