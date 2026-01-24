package com.jonatas.finance.infra.http.filter;

import com.jonatas.finance.util.Base62;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Component
public class RequestIdFilter extends OncePerRequestFilter {

    private static final String TRACE_ID = "traceId";
    private static final String X_HEADER_REQUEST_ID = "X-Request-Id";

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String requestId  = Optional.ofNullable(request.getHeader(X_HEADER_REQUEST_ID))
                    .orElse(this.shortUUIDWithBase62(UUID.randomUUID()));

            response.setHeader(X_HEADER_REQUEST_ID, requestId);

            if (MDC.get(TRACE_ID) == null) {
                MDC.put(TRACE_ID, requestId);
            }
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    private String shortUUIDWithBase62(UUID uuid) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return Base62.encode(buffer.array());
    }

}
