package io.micro.server.auth.infra.dao

import io.micro.server.auth.infra.po.UserEntity
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository
import io.smallrye.mutiny.Uni

interface IUserDAO : PanacheRepository<UserEntity> {
    fun selectUserByOpenid(openid: String): Uni<UserEntity>
}