package com.example.ProductService.common.filter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {


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
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();
        String clientIp = getClientIp(request);
        
        Bucket bucket;
        if (isAuthEndpoint(path)) {
            bucket = authBuckets.computeIfAbsent(clientIp, k -> createAuthBucket());
        } else {
            bucket = generalBuckets.computeIfAbsent(clientIp, k -> createGeneralBucket());
        }

        response.setHeader(RATE_LIMIT_VERSION_HEADER, VERSION);

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            logger.warn("Rate limit exceeded - ip={} path={} correlationId={}",
                    request.getRemoteAddr(),
                    path,
                    MDC.get("correlationId")
            );
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Too many requests\", \"message\": \"Rate limit exceeded\"}");
        }
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

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
