package mastermind.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    // for learning purposes
    // receives message from client via websocket
    @MessageMapping("/greet")
    public void handleGreeting(@Payload String message) {
        logger.info("WS /greet: {}", message);
    }    
}
