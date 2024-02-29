package io.micro.server.robot.infra.event

import io.micro.server.robot.domain.event.IRobotEvent
import io.micro.server.robot.domain.model.entity.RobotDO
import io.micro.server.robot.infra.converter.RobotConverter
import io.micro.server.robot.infra.event.dto.RobotDTO
import io.micro.server.robot.infra.event.dto.RobotStateChangeDTO
import io.micro.server.robot.infra.event.sdk.RobotConstant
import io.smallrye.mutiny.Uni
import io.smallrye.reactive.messaging.rabbitmq.OutgoingRabbitMQMetadata
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import org.eclipse.microprofile.reactive.messaging.Message
import org.eclipse.microprofile.reactive.messaging.Metadata
import java.util.concurrent.CompletableFuture

@ApplicationScoped
class RobotEvent(
    private val robotConverter: RobotConverter,
    @Channel(RobotConstant.Exchange.QQ_ROBOT) private val qqRobotEmitter: Emitter<RobotDTO>,
    @Channel(RobotConstant.Exchange.ROBOT) private val robotEmitter: Emitter<RobotStateChangeDTO>,
) : IRobotEvent {

    override fun publishRobotLoginSuccess(robotDO: RobotDO): Uni<Unit> {
        val rabbitMQMetadata = OutgoingRabbitMQMetadata.builder()
            .withRoutingKey(RobotConstant.RotingKey.ROBOT_LOGIN_SUCCESS)
            .build()
        return Uni.createFrom().emitter {
            qqRobotEmitter.send(Message.of(robotConverter.robotDO2RobotDTO(robotDO), Metadata.of(rabbitMQMetadata), {
                it.complete(null)
                CompletableFuture.completedFuture(null)
            }, { reason ->
                it.fail(reason)
                CompletableFuture.completedFuture(null)
            }))
        }
    }

    override fun publishRobotStateChange(id: Long, state: RobotDO.State): Uni<Unit> {
        val rabbitMQMetadata = OutgoingRabbitMQMetadata.builder()
            .withRoutingKey(RobotConstant.RotingKey.ROBOT_STATE_CHANGE)
            .build()
        val changeDTO = RobotStateChangeDTO().also {
            it.id = id
            it.state = robotConverter.stateDO2stateDTO(state)
        }
        return Uni.createFrom().emitter {
            robotEmitter.send(Message.of(changeDTO, Metadata.of(rabbitMQMetadata), {
                it.complete(null)
                CompletableFuture.completedFuture(null)
            }, { reason ->
                it.fail(reason)
                CompletableFuture.completedFuture(null)
            }))
        }
    }

}