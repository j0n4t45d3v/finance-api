package br.com.managementfinanceapi.infra.security;

import br.com.managementfinanceapi.application.core.usecase.user.GetUserDetailsUseCase;
import br.com.managementfinanceapi.infra.http.dto.ErrorV0;
import br.com.managementfinanceapi.infra.http.dto.ResponseV0;
import br.com.managementfinanceapi.adapter.in.jwt.JWTUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthFilter extends OncePerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);
  private final JWTUtils jwt;
  private final ObjectMapper mapper;
  private final GetUserDetailsUseCase userDetails;

  public AuthFilter(JWTUtils jwt, ObjectMapper mapper, GetUserDetailsUseCase userDetails) {
    this.jwt = jwt;
    this.mapper = mapper;
    this.userDetails = userDetails;
  }

  @Override
  protected void doFilterInternal(
      @Nonnull HttpServletRequest request,
      @Nonnull HttpServletResponse response,
      @Nonnull FilterChain filterChain
  ) throws ServletException, IOException {
    String authorization = request.getHeader("Authorization");
    if (authorization != null && authorization.startsWith("Bearer ")) {
      String token = authorization.substring(7);
      if (!this.jwt.tokenIsValid(token)) {
        log.info("Token inválido: {}", token);
        this.errorResponseFilter(response, "Token inválido!", 403);
        return;
      }
      String username = this.jwt.getSubject(token);
      try {
        UserDetails user = this.userDetails.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken userAuthenticate =
            new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities()
            );

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
          SecurityContextHolder.getContext().setAuthentication(userAuthenticate);
        }
      } catch (UsernameNotFoundException error) {
        log.info("Falha na autenticação: {}", error.getMessage());
        this.errorResponseFilter(response, error.getMessage(), 404);
      }
    }

    filterChain.doFilter(request, response);
  }

  private void errorResponseFilter(
      HttpServletResponse response,
      String message,
      int status
  ) throws IOException{
    ServletOutputStream out = response.getOutputStream();
    ErrorV0<?> error = ErrorV0.of(message);
    ResponseV0<ErrorV0<?>> errorResponse = ResponseV0.error(status, error);
    byte[] errorParseToJson = this.mapper.writeValueAsBytes(errorResponse);
    out.write(errorParseToJson);
    response.setStatus(status);
    response.setHeader("Content-Type", "application/json");
  }
}