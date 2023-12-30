package io.micro.server.robot.infra.dao.impl

import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.support.hibernate.reactive.extension.createQuery
import io.micro.server.auth.infra.po.User
import io.micro.server.robot.infra.dao.IRobotDAO
import io.micro.server.robot.infra.po.RobotEntity
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class RobotDAO : IRobotDAO {

    val jpqlRenderContext = JpqlRenderContext()

    override fun existByAccount(account: String): Uni<Boolean> {
        return this.count("account = ?1", account).map { it > 0 }
    }

    override fun findByExample(robotPO: RobotEntity, page: Page): Uni<List<RobotEntity>> {
        return getSession().flatMap { session ->
            val jpql = jpql {
                val and = buildList {
                    if (robotPO.name != null) {
                        add(path(RobotEntity::name).eq(robotPO.name))
                    }
                    if (robotPO.account != null) {
                        add(path(RobotEntity::account).eq(robotPO.account))
                    }
                    if (robotPO.type != null) {
                        add(path(RobotEntity::type).eq(robotPO.type))
                    }
                    if (robotPO.state != null && robotPO.state != RobotEntity.State.ALL) {
                        add(path(RobotEntity::state).eq(robotPO.state))
                    }
                    if (robotPO.remark != null) {
                        add(path(RobotEntity::remark).like("%${robotPO.remark}%"))
                    }
                }.toTypedArray()
                var predicate = and(*and)
                if (robotPO.user?.id == null) {
                    predicate = predicate.or(path(RobotEntity::user)(User::id).eq(robotPO.user?.id))
                }
                select(entity(RobotEntity::class))
                    .from(entity(RobotEntity::class))
                    .where(predicate)
            }
            session.createQuery(jpql, jpqlRenderContext)
                .setFirstResult((page.index - 1) * page.size)
                .setMaxResults(page.index * page.size)
                .resultList
        }
    }
}