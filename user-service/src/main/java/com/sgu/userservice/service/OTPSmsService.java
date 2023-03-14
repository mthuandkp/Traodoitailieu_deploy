package com.sgu.userservice.service;

public interface OTPSmsService {
    public Boolean sendSMS(String otpCode, String phone);
}
