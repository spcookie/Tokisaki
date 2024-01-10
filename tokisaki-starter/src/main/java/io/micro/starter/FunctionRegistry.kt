package io.micro.starter

import io.micro.core.function.sdk.Cmd
import io.micro.server.auth.infra.po.AuthorityEntity
import io.micro.server.robot.infra.po.FunctionEntity
import io.quarkus.runtime.StartupEvent
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import jakarta.inject.Inject
import org.hibernate.reactive.mutiny.Mutiny.SessionFactory
import java.util.function.Supplier

@ApplicationScoped
class FunctionRegistry {

    @Inject
    lateinit var sessionFactory: SessionFactory

    fun registry(@Observes ev: StartupEvent) {
        sessionFactory.withSession { session ->
            session.createQuery<FunctionEntity>("from FunctionEntity").resultList
                .flatMap { item ->
                    session.createQuery<AuthorityEntity>("from AuthorityEntity").resultList.map { item to it }
                }
                .flatMap {
                    val functions = it.first.map { it.code }
                    val authorities = it.second.map { it.value }
                    val newFunctionCode = Cmd.entries.filter { !functions.contains(it.auth) }
                    val newAuthorityValue = Cmd.entries.filter { !authorities.contains(it.auth) }

                    val functionEntities = buildList {
                        newFunctionCode.forEach { cmd ->
                            add(FunctionEntity().apply {
                                name = cmd.name
                                code = cmd.auth
                            })
                        }
                    }.toTypedArray()

                    val authorityEntities = buildList {
                        newAuthorityValue.forEach { cmd -> add(AuthorityEntity().apply { value = cmd.auth }) }
                    }.toTypedArray()

                    Uni.combine().all()
                        .unis(session.persistAll(*functionEntities), session.persistAll(*authorityEntities)).asTuple()
                        .call(Supplier { session.flush() })
                }
        }.await().indefinitely()
    }

}