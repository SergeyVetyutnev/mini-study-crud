package dev.vetyutnev.ministudycrud.ratelimiter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private final ProxyManager<byte[]> proxyManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String clientIp = request.getRemoteAddr();

        Bandwidth limit = Bandwidth.builder()
                .capacity(10)
                .refillGreedy(10, Duration.ofMinutes(1))
                .build();

        BucketConfiguration configuration = BucketConfiguration.builder()
                .addLimit(limit)
                .build();


        var bucket = proxyManager.builder().build(clientIp.getBytes(), configuration);

        if (bucket.tryConsume(1)) {
            return true;
        }

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write("Слишком много запросов. Пожалуйста, подождите.");
        return false;

    }
}
