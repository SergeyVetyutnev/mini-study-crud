package dev.vetyutnev.ministudycrud.ratelimiter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "app.rate-limiter.redis")
public class RateLimiterRedisProperties {

    @NotBlank(message = "Redis URL не может быть пустым")
    private String url;

    @NotNull(message = "Время жизни (expiration) должно быть указано")
    private Duration expirationDuration;
}
