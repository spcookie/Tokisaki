package io.micro.server.robot.infra.dao.impl

import io.micro.server.robot.infra.dao.IRobotDAO
import io.micro.server.robot.infra.po.Robot
import io.micro.server.robot.infra.po.Robot_
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.hibernate.reactive.mutiny.Mutiny

@ApplicationScoped
class RobotDAO : IRobotDAO {

    @Inject
    lateinit var sf: Mutiny.SessionFactory

    override fun existByAccount(account: String): Uni<Boolean> {
        return this.count("account = ?1", account).map { it > 0 }
    }

    override fun findByExample(robot: Robot, page: Page): Uni<List<Robot>> {
        return sf.withSession { session ->
            val cb = session.factory.criteriaBuilder
            val cq = cb.createQuery(Robot::class.java)
            val root = cq.from(Robot::class.java)
            val example = buildList {
                robot.name?.also { add(cb.equal(root.get(Robot_.name), it)) }
                robot.account?.also { add(cb.equal(root.get(Robot_.account), it)) }
                robot.type?.also { add(cb.equal(root.get(Robot_.type), it)) }
                robot.state?.also {
                    if (it != Robot.State.ALL) {
                        add(cb.equal(root.get(Robot_.state), it))
                    }
                }
                robot.remark?.also { add(cb.equal(root.get(Robot_.remark), it)) }
            }
            val andUserId = mutableListOf(cb.or(*example.toTypedArray()))
            robot.user?.id?.let { andUserId.add(cb.equal(root.get(Robot_.id), it)) }
            session.createQuery(cq.select(root).where(*andUserId.toTypedArray()))
                .setFirstResult((page.index - 1) * page.size)
                .setMaxResults(page.index * page.size)
                .resultList
        }
    }
}