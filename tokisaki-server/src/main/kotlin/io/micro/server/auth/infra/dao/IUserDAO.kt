package io.micro.server.auth.infra.dao

import io.micro.server.auth.infra.po.User
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository
import io.smallrye.mutiny.Uni

interface IUserDAO : PanacheRepository<User> {
    fun selectUserByOpenid(openid: String): Uni<User>
}