package com.sgu.userservice.controller;


import com.sgu.userservice.dto.request.DeleteRequest;
import com.sgu.userservice.dto.request.UserRequest;
import com.sgu.userservice.dto.response.HttpResponseEntity;
import com.sgu.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public ResponseEntity<HttpResponseEntity> register(@RequestBody @Valid UserRequest userRequest){
        HttpResponseEntity httpResponseEntity = userService.register(userRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(httpResponseEntity);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<HttpResponseEntity> delete(@RequestBody @Valid DeleteRequest deleteRequest){
        HttpResponseEntity httpResponseEntity = userService.delete(deleteRequest);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseEntity);
    }

}
