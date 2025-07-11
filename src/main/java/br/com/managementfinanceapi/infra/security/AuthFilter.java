package br.com.managementfinanceapi.infra.security;

import br.com.managementfinanceapi.adapter.in.dto.error.ErrorV0;
import br.com.managementfinanceapi.application.port.out.security.jwt.TokenReaderPort;
import br.com.managementfinanceapi.adapter.in.dto.ResponseV0;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthFilter extends OncePerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);
  private final TokenReaderPort tokenReader;
  private final ObjectMapper mapper;
  private final UserDetailsService userDetails;

  public AuthFilter(TokenReaderPort tokenReader, ObjectMapper mapper, UserDetailsService userDetails) {
    this.tokenReader = tokenReader;
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
      if (this.tokenReader.isExpired(token)) {
        this.errorResponseFilter(response, "Token Expirado!", 403);
        return;
      }
      String username = this.tokenReader.getSubject(token);
      if(username == null ||username.isEmpty()) {
        this.errorResponseFilter(response, "Token inválido!", 403);
        return;
      }
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