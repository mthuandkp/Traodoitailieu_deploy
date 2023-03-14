package com.sgu.authservice.service;

import com.sgu.authservice.dto.request.RefreshTokenRequest;
import com.sgu.authservice.dto.response.HttpResponseObject;
import com.sgu.authservice.dto.request.LoginRequest;

public interface AuthService {
    public HttpResponseObject login(LoginRequest loginRequest);

    public HttpResponseObject refreshToken(RefreshTokenRequest refreshToken);
}
