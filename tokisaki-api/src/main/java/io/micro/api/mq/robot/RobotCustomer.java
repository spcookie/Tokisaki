package io.micro.api.mq.robot;

import io.micro.api.robot.converter.RobotManagerConverter;
import io.micro.server.robot.domain.service.RobotManagerService;
import io.micro.server.robot.infra.event.dto.RobotDTO;
import io.micro.server.robot.infra.event.dto.RobotStateChangeDTO;
import io.micro.server.robot.infra.event.sdk.RobotConstant;
import io.quarkus.logging.Log;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class RobotCustomer {

    @Inject
    public RobotManagerService robotManagerService;

    @Inject
    public RobotManagerConverter robotManagerConverter;


    @Incoming(RobotConstant.Queue.ROBOT_LOGIN_SUCCESS)
    public CompletionStage<Void> robotLoginSuccess(Message<JsonObject> msg) {
        RobotDTO robot = msg.getPayload().mapTo(RobotDTO.class);
        Log.info("mq消费: robotLoginSuccess -> id: " + robot.getId());
        return msg.ack();
    }

    @Incoming(RobotConstant.Queue.ROBOT_STATE_CHANGE)
    public CompletionStage<Void> robotStateChange(Message<JsonObject> msg) {
        RobotStateChangeDTO stateChange = msg.getPayload().mapTo(RobotStateChangeDTO.class);
        Long id = stateChange.getId();
        RobotDTO.State state = stateChange.getState();
        Log.info("mq消费: robotStateChange -> id: " + id + " state: " + state);
//        if (id != null && state != null) {
//            robotManagerService.modifyRobotState(id, robotManagerConverter.stateDTO2stateDO(state))
//        }
        return msg.ack();
    }

}
