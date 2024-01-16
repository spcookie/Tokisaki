package io.micro.server.auth.infra.dao

import io.micro.server.auth.infra.po.AuthorityEntity
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository

interface IAuthorityDAO : PanacheRepository<AuthorityEntity> {
}