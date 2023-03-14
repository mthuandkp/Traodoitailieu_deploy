package com.sgu.userservice.service;

import com.sgu.userservice.dto.request.*;
import com.sgu.userservice.dto.response.HttpResponseEntity;
import com.sgu.userservice.model.ActiveAccountRequest;
import org.springframework.web.multipart.MultipartFile;

public interface AccountService {
    public HttpResponseEntity getAllPerson();

    public HttpResponseEntity getAllAccount();

    public HttpResponseEntity getAllAccountWithPagination(int page, int size);

    public HttpResponseEntity getAccoutByPersonId(Long personId);

    public HttpResponseEntity getAccoutByUsername(String username);

    public HttpResponseEntity sendOtpCode(SendOTPRequest sendOTPRequest);

    public HttpResponseEntity activeAccount(ActiveAccountRequest activeAccountRequest);

    public HttpResponseEntity blockAccount(BlockAccountRequest blockAccountRequest);

    public HttpResponseEntity unBlockAccount(UnBlockAccountRequest unBlockAccountRequest);

    public HttpResponseEntity changePassword(ChangePasswordRequest changePasswordRequest);

    public HttpResponseEntity uploadImage(String updateAvatarRequest, MultipartFile file);

    public HttpResponseEntity updateVnpay(String username, MultipartFile file);
}
