package io.micro.server.robot.infra.dao.impl

import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.support.hibernate.reactive.extension.createQuery
import io.micro.server.auth.infra.po.UserEntity
import io.micro.server.robot.infra.dao.IRobotDAO
import io.micro.server.robot.infra.po.RobotEntity
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class RobotDAO : IRobotDAO {

    val jpqlRenderContext = JpqlRenderContext()

    override fun existByAccount(account: String, id: Long): Uni<Boolean> {
        return this.count("account = ?1 and user.id = ?2", account, id).map { it > 0 }
    }

    override fun selectByExample(robotEntity: RobotEntity, page: Page): Uni<List<RobotEntity>> {
        return getSession().flatMap { session ->
            val jpql = jpql {
                val predicate = buildList {
                    if (robotEntity.id != null) {
                        add(path(RobotEntity::id).eq(robotEntity.id))
                    }
                    if (robotEntity.name != null) {
                        add(path(RobotEntity::name).like("%${robotEntity.name}%"))
                    }
                    if (robotEntity.account != null) {
                        add(path(RobotEntity::account).like("%${robotEntity.account}%"))
                    }
                    if (robotEntity.type != null) {
                        add(path(RobotEntity::type).eq(robotEntity.type))
                    }
                    if (robotEntity.state != null && robotEntity.state != RobotEntity.State.ALL) {
                        add(path(RobotEntity::state).eq(robotEntity.state))
                    }
                    if (robotEntity.remark != null) {
                        add(path(RobotEntity::remark).like("%${robotEntity.remark}%"))
                    }
                    if (robotEntity.user?.id != null) {
                        add(path(RobotEntity::user)(UserEntity::id).eq(robotEntity.user?.id))
                    }
                }.toTypedArray()
                select(entity(RobotEntity::class))
                    .from(entity(RobotEntity::class), leftFetchJoin(RobotEntity::functions))
                    .where(and(*predicate))
            }
            session.createQuery(jpql, jpqlRenderContext)
                .setFirstResult((page.index - 1) * page.size)
                .setMaxResults(page.index * page.size)
                .resultList
        }
    }

    override fun countByExample(robotEntity: RobotEntity, page: Page): Uni<Long> {
        return getSession().flatMap { session ->
            val jpql = jpql {
                val predicate = buildList {
                    if (robotEntity.id != null) {
                        add(path(RobotEntity::id).eq(robotEntity.id))
                    }
                    if (robotEntity.name != null) {
                        add(path(RobotEntity::name).like("%${robotEntity.name}%"))
                    }
                    if (robotEntity.account != null) {
                        add(path(RobotEntity::account).like("%${robotEntity.account}%"))
                    }
                    if (robotEntity.type != null) {
                        add(path(RobotEntity::type).eq(robotEntity.type))
                    }
                    if (robotEntity.state != null && robotEntity.state != RobotEntity.State.ALL) {
                        add(path(RobotEntity::state).eq(robotEntity.state))
                    }
                    if (robotEntity.remark != null) {
                        add(path(RobotEntity::remark).like("%${robotEntity.remark}%"))
                    }
                    if (robotEntity.user?.id != null) {
                        add(path(RobotEntity::user)(UserEntity::id).eq(robotEntity.user?.id))
                    }
                }.toTypedArray()
                select(count(expression("*")))
                    .from(entity(RobotEntity::class))
                    .where(and(*predicate))
            }
            session.createQuery(jpql, jpqlRenderContext).singleResult
        }
    }

}