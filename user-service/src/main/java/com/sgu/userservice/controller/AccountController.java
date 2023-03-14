package com.sgu.userservice.controller;

import com.sgu.userservice.dto.request.*;
import com.sgu.userservice.dto.response.HttpResponseEntity;
import com.sgu.userservice.model.ActiveAccountRequest;
import com.sgu.userservice.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/get-all-account")
    public ResponseEntity<HttpResponseEntity> getAllAccount(){
        HttpResponseEntity httpResponseEntity = accountService.getAllAccount();

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseEntity);
    }

    @GetMapping("/get-all-account-with-pagination")
    public ResponseEntity<HttpResponseEntity> getAllAccountWithPagination(
            @RequestParam(name = "page", required = true) int page,
            @RequestParam(name = "size", required = true) int size
    ){
        HttpResponseEntity httpResponseEntity = accountService.getAllAccountWithPagination(page,size);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseEntity);
    }

    @GetMapping("/get-account-by-person-id/{id}")
    public ResponseEntity<HttpResponseEntity> getAccountByPersonId(
            @PathVariable(name = "id") Long personId
    ){
        HttpResponseEntity httpResponseEntity = accountService.getAccoutByPersonId(personId);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseEntity);
    }

    @GetMapping("/get-account-by-username/{username}")
    public ResponseEntity<HttpResponseEntity> getAccountByUsername(
            @PathVariable(name = "username") String username
    ){
        HttpResponseEntity httpResponseEntity = accountService.getAccoutByUsername(username);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseEntity);
    }

    @PostMapping("/send-otp-code")
    public ResponseEntity<HttpResponseEntity> sendOtpCode(
            @RequestBody @Valid SendOTPRequest sendOTPRequest
    ){
        HttpResponseEntity httpResponseEntity = accountService.sendOtpCode(sendOTPRequest);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseEntity);
    }

    @PostMapping("/active-account")
    public ResponseEntity<HttpResponseEntity> activeAccount(
            @RequestBody @Valid ActiveAccountRequest activeAccountRequest
    ){
        HttpResponseEntity httpResponseEntity = accountService.activeAccount(activeAccountRequest);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseEntity);
    }

    @PostMapping("/block-account")
    public ResponseEntity<HttpResponseEntity> blockAccount(
            @RequestBody @Valid BlockAccountRequest blockAccountRequest
    ){
        HttpResponseEntity httpResponseEntity = accountService.blockAccount(blockAccountRequest);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseEntity);
    }

    @PostMapping("/unblock-account")
    public ResponseEntity<HttpResponseEntity> unBlockAccount(
            @RequestBody @Valid UnBlockAccountRequest unBlockAccountRequest
    ){
        HttpResponseEntity httpResponseEntity = accountService.unBlockAccount(unBlockAccountRequest);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseEntity);
    }


    @PostMapping("/change-password")
    public ResponseEntity<HttpResponseEntity> changePassword(
            @RequestBody @Valid ChangePasswordRequest changePasswordRequest
    ){
        HttpResponseEntity httpResponseEntity = accountService.changePassword(changePasswordRequest);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseEntity);
    }

    @PostMapping("/upload-image/{username}")
    public ResponseEntity<?> updateAvatar(
            @RequestParam("file") MultipartFile file,
            @PathVariable(name = "username") String username
    ){

        HttpResponseEntity httpResponseEntity = accountService.uploadImage(username, file);
        return ResponseEntity.status(HttpStatus.OK).body(httpResponseEntity);
    }

    @PostMapping("/update-vnpay/{username}")
    public ResponseEntity<HttpResponseEntity> updateVnpay(
            @RequestParam("file") MultipartFile file,
            @PathVariable(name = "username") String username
    ){
        HttpResponseEntity httpResponseEntity = accountService.updateVnpay(username,file);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseEntity);
    }
}
