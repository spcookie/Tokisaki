package io.micro.api.mq.robot;

import io.micro.server.robot.infra.event.dto.RobotDTO;
import io.micro.server.robot.infra.event.sdk.RobotConstant;
import io.quarkus.logging.Log;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class RobotCustomer {

    @Incoming(RobotConstant.Queue.ROBOT_LOGIN_SUCCESS)
    public CompletionStage<Void> robotLoginSuccess(Message<JsonObject> msg) {
        RobotDTO robot = msg.getPayload().mapTo(RobotDTO.class);
        Log.info("mq消费: " + robot.getId());
        return msg.ack();
    }

}