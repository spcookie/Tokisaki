package io.micro.server.robot.infra.dao.impl

import io.micro.server.robot.infra.dao.IUseFunctionDAO
import io.micro.server.robot.infra.po.UseFunctionEntity
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class UseFunctionDAO: IUseFunctionDAO {
    override fun selectUseFunctionByRobotId(id: Long): Uni<List<UseFunctionEntity>> {
        return list("robot.id = ?1", id)
    }
}