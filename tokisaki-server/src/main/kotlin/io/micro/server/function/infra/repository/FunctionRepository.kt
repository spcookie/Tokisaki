package io.micro.server.function.infra.repository

import io.micro.server.function.domain.model.entity.FunctionDO
import io.micro.server.function.domain.repository.IFunctionRepository
import io.micro.server.function.infra.converter.FunctionConverter
import io.micro.server.function.infra.dao.IFunctionDAO
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class FunctionRepository(
    private val functionDAO: IFunctionDAO,
    private val functionConverter: FunctionConverter
) : IFunctionRepository {

    override fun findAllFunctions(): Uni<List<FunctionDO>> {
        return functionDAO.findAll().list().map { functionEntities ->
            functionEntities.map {
                functionConverter.functionEntity2functionDO(it)
            }
        }
    }

    override fun findById(id: Long): Uni<FunctionDO> {
        return functionDAO.findById(id)
            .map(functionConverter::functionEntity2functionDO)
    }

}