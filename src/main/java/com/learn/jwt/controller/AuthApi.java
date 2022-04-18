package com.learn.jwt.controller;

import com.learn.jwt.config.JwtTokenUtil;
import com.learn.jwt.dto.AuthRequest;
import com.learn.jwt.entity.User;
import lombok.AllArgsConstructor;
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

@RestController
@RequestMapping("api/public")
@AllArgsConstructor
public class AuthApi {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<User> login(@Valid @RequestBody AuthRequest request){

        try{
            Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
            User user=(User) authentication.getPrincipal();
            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION,jwtTokenUtil.generateAccessToken(user)).body(user);
        }catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
