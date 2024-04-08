package mastermind.backend.controller;

import java.util.Collections;
import java.util.List;
import mastermind.backend.model.GameEvent;
import mastermind.backend.service.GameEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class GraphQLController {
    
    @Autowired
    public final GameEventService gameEventService;

    private static final Logger logger = LoggerFactory.getLogger(GraphQLController.class);

    public GraphQLController(GameEventService gameEventService) {
        this.gameEventService = gameEventService;
    }

    // retrieve game event by game session id
    @QueryMapping
    public List<GameEvent> getGameEventByGameSessionId(@Argument String gameSessionId) {
        try{
            // retrieve game event list
            List<GameEvent> gameEventList = gameEventService.getGameEventByGameSessionId(gameSessionId);
            return gameEventList;
        } catch(Exception e) {
            logger.error("Not able to retrieve game events of game session: {}", gameSessionId);
            return Collections.emptyList();
        }
    }    
}
