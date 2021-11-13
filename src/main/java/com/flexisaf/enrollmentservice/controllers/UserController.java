package com.flexisaf.enrollmentservice.controllers;

import com.flexisaf.enrollmentservice.dto.responses.ErrorResponse;
import com.flexisaf.enrollmentservice.models.User;
import com.flexisaf.enrollmentservice.security.JwtTokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final AuthenticationManager authenticationManager;

    public UserController(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid Map<String, String> loginDetails){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDetails.get("username"), loginDetails.get("password")));
            User user = (User) authentication.getPrincipal();

            JwtTokenProvider tokenProvider = new JwtTokenProvider();
            String token = tokenProvider.generateToken(authentication);
            Map<String,Object> map = new HashMap<>();
            map.put("status", "success");
            map.put("token", token);
            map.put("username", loginDetails.get("username"));
            map.put("id", user.getId());
            map.put("roles", user.getAuthorities());


            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .body(map);
        }catch(BadCredentialsException | UnsupportedEncodingException ex){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setCode(HttpStatus.UNAUTHORIZED.value());
            errorResponse.setMessage(ex.getMessage());
            errorResponse.setStatus(HttpStatus.UNAUTHORIZED.toString());
            errorResponse.setTimestamp(LocalDateTime.now().toString());
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

}
