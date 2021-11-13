package com.flexisaf.enrollmentservice.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flexisaf.enrollmentservice.dto.responses.ErrorResponse;
import com.flexisaf.enrollmentservice.models.User;
import com.flexisaf.enrollmentservice.security.SecurityConstants;
import com.flexisaf.enrollmentservice.services.UserService;
import com.flexisaf.enrollmentservice.utilities.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import springfox.documentation.spi.service.contexts.SecurityContext;

import javax.persistence.Access;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {


  private UserService userService;

  public AuthorizationFilter(UserService userService) {
    this.userService = userService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    String bearerToken = request.getHeader(Constants.AUTHORIZATION_HEADER_NAME);

    if (!StringUtils.hasText(bearerToken) || !bearerToken.startsWith(Constants.TOKEN_PREFIX)) {
      chain.doFilter(request, response);
      return;
    }

    String token = bearerToken.replace(Constants.TOKEN_PREFIX, "");
    Optional<String> optionalUserId = getUserIdFromJWT(token, response);
            if (optionalUserId.isEmpty())
              return ;
            Long userId = Long.valueOf(optionalUserId.get());

    request.setAttribute("user-id", userId);
    User user = userService.findById(userId);
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(request, response);
  }

  private Optional<String> getUserIdFromJWT(String token, HttpServletResponse response) {

    ObjectMapper objectMapper = new ObjectMapper();
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setStatus(HttpStatus.FORBIDDEN.toString());
    errorResponse.setCode(HttpStatus.FORBIDDEN.value());
    errorResponse.setTimestamp(LocalDateTime.now().toString());

    if (StringUtils.hasText(token)) {
      try {
        String userId =
            JWT.require(Algorithm.HMAC512(SecurityConstants.JWT_SECRET)).build().verify(token).getSubject();
        return Optional.ofNullable(userId);
      } catch (SignatureVerificationException s) {
        errorResponse.setMessage(s.getMessage());
      } catch (AlgorithmMismatchException a) {
        errorResponse.setMessage(a.getMessage());
      } catch (InvalidClaimException i) {
        errorResponse.setMessage(i.getMessage());
      } catch (TokenExpiredException t) {
        errorResponse.setMessage(t.getMessage());
      } catch (Exception e) {
        errorResponse.setMessage(e.getMessage());
      }
    } else {
      errorResponse.setMessage("Request must be authenticated");
    }

    try {
      response.setStatus(HttpStatus.FORBIDDEN.value());
      objectMapper.writeValue(response.getWriter(), errorResponse);
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
    return Optional.empty();
  }
}
