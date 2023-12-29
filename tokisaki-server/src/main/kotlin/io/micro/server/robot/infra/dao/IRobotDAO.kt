package io.micro.server.robot.infra.dao

import io.micro.server.robot.infra.po.Robot
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Uni

interface IRobotDAO : PanacheRepository<Robot> {
    fun existByAccount(account: String): Uni<Boolean>
    fun findByExample(robot: Robot, page: Page): Uni<List<Robot>>
}