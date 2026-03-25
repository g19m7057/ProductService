package com.example.ProductService.common.filter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter implements WebFilter {


    @Value("${rate-limit.auth.capacity}")
    private long authCapacity;

    @Value("${rate-limit.auth.refill-amount}")
    private long authRefillAmount;

    @Value("${rate-limit.auth.refill-duration-minutes}")
    private long authRefillDurationMinutes;

    @Value("${rate-limit.general.capacity}")
    private long generalCapacity;

    @Value("${rate-limit.general.refill-amount}")
    private long generalRefillAmount;

    @Value("${rate-limit.general.refill-duration-minutes}")
    private long generalRefillDurationMinutes;


    private static final String RATE_LIMIT_VERSION_HEADER = "X-Rate-Limit-Version";
    private static final String VERSION = "1.0";

    private final Map<String, Bucket> authBuckets = new ConcurrentHashMap<>();
    private final Map<String, Bucket> generalBuckets = new ConcurrentHashMap<>();
    private final Logger logger = LoggerFactory.getLogger(RateLimitingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String path = exchange.getRequest().getPath().value();
        String clientIp = getClientIp(exchange);

        Bucket bucket;
        if (isAuthEndpoint(path)) {
            bucket = authBuckets.computeIfAbsent(clientIp, k -> createAuthBucket());
        } else {
            bucket = generalBuckets.computeIfAbsent(clientIp, k -> createGeneralBucket());
        }

        exchange.getResponse().getHeaders().set(RATE_LIMIT_VERSION_HEADER, VERSION);

        return Mono.defer(() -> {
            if (bucket.tryConsume(1)) {
                return chain.filter(exchange);
            } else {
                logger.warn("Rate limit exceeded - path={} correlationId={}",
                        path,
                        MDC.get("correlationId")
                );
                exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap("{\"error\": \"Too many requests\", \"message\": \"Rate limit exceeded\"}".getBytes());
                return exchange.getResponse().writeWith(Mono.just(buffer));
            }
        });
    }

    private boolean isAuthEndpoint(String path) {
        return path.equals("/auth/login") || path.equals("/auth/register");
    }

    private Bucket createAuthBucket() {
        Bandwidth limit = Bandwidth.classic(authCapacity, Refill.intervally(authRefillAmount, Duration.ofMinutes(authRefillDurationMinutes)));
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    private Bucket createGeneralBucket() {
        Bandwidth limit = Bandwidth.classic(generalCapacity, Refill.intervally(generalRefillAmount, Duration.ofMinutes(generalRefillDurationMinutes)));
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    private String getClientIp(ServerWebExchange exchange) {
        String xfHeader = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        if (xfHeader == null) {
            InetSocketAddress remoteAddress = exchange.getRequest().getRemoteAddress();
            return remoteAddress != null ? remoteAddress.getAddress().getHostAddress() : "unknown";
        }
        return xfHeader.split(",")[0].trim();
    }
}
