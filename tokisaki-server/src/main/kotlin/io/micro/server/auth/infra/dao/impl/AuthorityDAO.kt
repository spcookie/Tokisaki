package io.micro.server.auth.infra.dao.impl

import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.support.hibernate.reactive.extension.createQuery
import io.micro.server.auth.infra.dao.IAuthorityDAO
import io.micro.server.auth.infra.po.AuthorityEntity
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class AuthorityDAO : IAuthorityDAO {

    val jpqlRenderContext = JpqlRenderContext()

    fun switchAuthorityById(id: Long): Uni<Long> {
        return findById(id)
            .map { authority ->
                if (authority != null) {
                    authority.enabled = !authority.enabled
                    flush()
                    1
                } else {
                    0
                }
            }
    }

    fun findAuthorityByExample(authorityEntity: AuthorityEntity): Uni<List<AuthorityEntity>> {
        val jpql = jpql {
            val predicates = buildList {
                authorityEntity.id?.let { add(path(AuthorityEntity::id).eq(it)) }
                authorityEntity.value?.let { add(path(AuthorityEntity::value).eq(it)) }
                authorityEntity.remark?.let { add(path(AuthorityEntity::remark).eq(it)) }
                authorityEntity.enabled.let { add(path(AuthorityEntity::enabled).eq(it)) }
            }.toTypedArray()
            select(entity(AuthorityEntity::class))
                .from(entity(AuthorityEntity::class))
                .whereAnd(*predicates)
        }
        return getSession().flatMap { session ->
            session.createQuery(jpql, jpqlRenderContext).resultList
        }
    }

}