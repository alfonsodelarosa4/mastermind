package mastermind.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class RandomApiService {

    private final WebClient randomGeneratorClient;

    private static final Logger logger = LoggerFactory.getLogger(RandomApiService.class);
    
    public RandomApiService(WebClient.Builder webClientBuilder) {
        this.randomGeneratorClient = webClientBuilder.baseUrl("https://www.random.org").build();
    }

    // get random generated integer with REST API call to random.org
    public String getRandomInteger(int num, int min, int max) {
        // makes rest api call to generate random number
        logger.info("getRandomInteger(): make rest api call to random.org");
        String response = randomGeneratorClient.get()
            .uri(builder -> builder
                .path("/integers/")
                .queryParam("num", num)
                .queryParam("min", min)
                .queryParam("max", max)
                .queryParam("col", 1)
                .queryParam("base", 10)
                .queryParam("format", "plain")
                .queryParam("rnd", "new")
                .build())
            .retrieve()
            .bodyToMono(String.class)
            .block();
        
        // remove new lines
        String answer = response.replaceAll("\\s+","");
        logger.info("getRandomInteger(): generated {}", answer);
        return answer;
    }
}
