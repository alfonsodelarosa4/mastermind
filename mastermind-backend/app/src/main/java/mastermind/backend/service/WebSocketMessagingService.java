package mastermind.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketMessagingService {

    @Autowired
    private final SimpMessagingTemplate template;

    private static final Logger logger = LoggerFactory.getLogger(WebSocketMessagingService.class);

    public WebSocketMessagingService(
        SimpMessagingTemplate template
    ) {
        this.template = template;
    }

    public void sendMessageToGameSession(Object message, String gameSessionId) {
        logger.info("sendMessageToGameSession: message: {} path={}", message, gameSessionId);
        template.convertAndSend("/game-session/" + gameSessionId, message);
    }    
}
