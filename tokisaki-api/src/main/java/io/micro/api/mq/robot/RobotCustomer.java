package io.micro.api.mq.robot;

import io.micro.server.robot.infra.event.dto.RobotDTO;
import io.micro.server.robot.infra.event.sdk.RobotConstant;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class RobotCustomer {

    @Incoming(RobotConstant.Queue.ROBOT_LOGIN_SUCCESS)
    public CompletionStage<Void> robotLoginSuccess(Message<JsonObject> msg) {
        JsonObject jsonObject = msg.getPayload();
        RobotDTO robot = jsonObject.mapTo(RobotDTO.class);
        System.out.println("==========");
        System.out.println(robot.getId());
        System.out.println("==========");
        return msg.ack();
    }

}
