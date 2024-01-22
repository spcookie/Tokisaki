package io.micro.server.robot.infra.event

import io.micro.server.robot.domain.event.IRobotEvent
import io.micro.server.robot.domain.model.entity.RobotDO
import io.micro.server.robot.infra.converter.RobotConverter
import io.micro.server.robot.infra.event.dto.RobotDTO
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
    @Channel(RobotConstant.Exchange.ROBOT_LOGIN) private val emitter: Emitter<RobotDTO>
) : IRobotEvent {

    override fun publishRobotLoginSuccess(robotDO: RobotDO): Uni<Unit> {
        val rabbitMQMetadata = OutgoingRabbitMQMetadata.builder()
            .withRoutingKey(RobotConstant.RotingKey.ROBOT_LOGIN_SUCCESS)
            .build()
        return Uni.createFrom().emitter {
            emitter.send(Message.of(robotConverter.robotDO2RobotDTO(robotDO), Metadata.of(rabbitMQMetadata), {
                it.complete(null)
                CompletableFuture.completedFuture(null)
            }, { reason ->
                it.fail(reason)
                CompletableFuture.completedFuture(null)
            }))
        }

    }

}