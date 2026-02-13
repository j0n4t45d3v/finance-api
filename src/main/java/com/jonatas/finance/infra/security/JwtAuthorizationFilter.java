package com.jonatas.finance.infra.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonatas.finance.dto.Response;
import com.jonatas.finance.dto.Response.Status;
import com.jonatas.finance.infra.error.Error;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    private final AntPathMatcher antPathMatcher ;

    public JwtAuthorizationFilter(
        JwtService jwtService,
        UserDetailsService userDetailsService,
        ObjectMapper objectMapper,
        AntPathMatcher antPathMatcher
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
        this.antPathMatcher = antPathMatcher;
    }

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain) throws ServletException, IOException {

        if (this.antPathMatcher.match("/api/v1/auth/**", request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        var authentication = Optional
                .ofNullable(request.getHeader("Authorization"))
                .orElse("");
        if (authentication.isBlank() || !authentication.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authentication.substring(7).trim();
        if (token.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean isValidToken = this.jwtService.isValidAccessToken(token);
        if (!isValidToken) {
            Error<String> error = new Error<String>("INVALID_TOKEN", "invalid token");
            this.writeErrorMessage(response, error, 401);
            return;
        }

        String subject = this.jwtService.getSubjectAccessToken(token);
        Optional<UserDetails> userDetailsOptional = this.tryLoadUser(subject);
        if (userDetailsOptional.isEmpty()) {
            Error<String> error = new Error<String>("INVALID_TOKEN", "subject not found");
            this.writeErrorMessage(response, error, 401);
            return;
        }

        UserDetails userDetails = userDetailsOptional.get();
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

    private Optional<UserDetails> tryLoadUser(String subject) {
        try {
            return Optional.of(this.userDetailsService.loadUserByUsername(subject));
        } catch (UsernameNotFoundException error) {
            return Optional.empty();
        }
    }

    private <TError> void writeErrorMessage(
            HttpServletResponse response,
            Error<TError> error,
            int status) throws IOException {
        OutputStream out = response.getOutputStream();
        Response<Void, Error<TError>> data = Response.ofError(error, Status.BAD_REQUEST);
        response.setStatus(status);
        response.setHeader("Content-Type", "application/json");
        out.write(this.objectMapper.writeValueAsBytes(data));
        out.flush();
    }

}
