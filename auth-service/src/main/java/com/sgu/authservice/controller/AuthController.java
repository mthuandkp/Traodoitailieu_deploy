package com.sgu.authservice.controller;

import com.netflix.discovery.converters.Auto;
import com.sgu.authservice.dto.request.RefreshTokenRequest;
import com.sgu.authservice.dto.response.HttpResponseObject;
import com.sgu.authservice.dto.request.LoginRequest;
import com.sgu.authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<HttpResponseObject> login(
        @RequestBody(required = false) LoginRequest loginRequest
    ){
        HttpResponseObject httpResponse = authService.login(loginRequest);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<HttpResponseObject> refreshToken(
            @RequestBody(required = false) RefreshTokenRequest refreshTokenRequest
    ){
        HttpResponseObject httpResponse = authService.refreshToken(refreshTokenRequest);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponse);
    }
}
