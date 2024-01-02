package io.micro.server.robot.infra.dao

import io.micro.server.robot.infra.po.RobotEntity
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Uni

interface IRobotDAO : PanacheRepository<RobotEntity> {

    fun existByAccount(account: String, id: Long): Uni<Boolean>

    fun selectByExample(robotEntity: RobotEntity, page: Page): Uni<List<RobotEntity>>

}