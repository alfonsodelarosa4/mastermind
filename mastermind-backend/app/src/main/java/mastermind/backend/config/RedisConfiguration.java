package mastermind.backend.config;

import mastermind.backend.model.GameRoster;
import mastermind.backend.model.GameSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class RedisConfiguration {

    @Bean
    public RedisTemplate<?, ?> template(RedisConnectionFactory connectionFactory) {
        RedisTemplate<?, ?> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    public RedisTemplate<String, GameSession> gameSessionTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, GameSession> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    public RedisTemplate<String, GameRoster> gameRosterTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, GameRoster> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}