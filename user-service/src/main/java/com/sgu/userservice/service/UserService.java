package com.sgu.userservice.service;

import com.sgu.userservice.dto.request.DeleteRequest;
import com.sgu.userservice.dto.request.UserRequest;
import com.sgu.userservice.dto.response.HttpResponseEntity;

public interface UserService {
    public HttpResponseEntity register(UserRequest userRequest);

    public HttpResponseEntity delete(DeleteRequest deleteRequest);
}
