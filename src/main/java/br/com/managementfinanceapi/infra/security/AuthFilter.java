package br.com.managementfinanceapi.infra.security;

import br.com.managementfinanceapi.adapter.in.dto.error.ResponseErrorV0;
import br.com.managementfinanceapi.application.port.out.security.jwt.TokenReaderPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

  private final TokenReaderPort tokenReader;
  private final ObjectMapper mapper;
  private final UserDetailsService userDetails;

  public AuthFilter(
      TokenReaderPort tokenReader,
      ObjectMapper mapper,
      UserDetailsService userDetails
  ) {
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
        ResponseErrorV0<String> responseError = ResponseErrorV0.forbidden("Authorization Error", "Token Expirado!");
        this.errorResponseFilter(response, responseError);
        return;
      }
      String username = this.tokenReader.getSubject(token);
      if(username == null ||username.isEmpty()) {
        ResponseErrorV0<String> responseError = ResponseErrorV0.forbidden("Authorization Error", "Token inválido!");
        this.errorResponseFilter(response, responseError);
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
        ResponseErrorV0<String> responseError = ResponseErrorV0.unauthorized("Authorization Error", error.getMessage());
        this.errorResponseFilter(response, responseError);
      }
    }

    filterChain.doFilter(request, response);
  }

  private void errorResponseFilter(
      HttpServletResponse response,
      ResponseErrorV0<?> responseErrorV0
  ) throws IOException{
    ServletOutputStream out = response.getOutputStream();
    byte[] errorParseToJson = this.mapper.writeValueAsBytes(responseErrorV0);
    out.write(errorParseToJson);
    response.setStatus(responseErrorV0.status());
    response.setHeader("Content-Type", "application/json");
  }
}