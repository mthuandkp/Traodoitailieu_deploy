package com.sgu.userservice.service.impl;

import com.sgu.userservice.exception.ServerInternalException;
import com.sgu.userservice.service.OTPSmsService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OTPSmsServiceImp implements OTPSmsService {

    @Value("${twilio.account_sid}")
    private String ACCOUNT_SID;
    @Value("${twilio.auth_token}")
    private String AUTH_TOKEN;
    @Override
    public Boolean sendSMS(String otpCode, String phone) {
        try{
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            String formatPhone = "+84" + phone.substring(1);

            Message message = Message.creator(
                            new com.twilio.type.PhoneNumber(formatPhone),
                            //new com.twilio.type.PhoneNumber(formatPhone),
                            "MGec6eb1c3a05ac5f6e977d0ab5c011a9a",
                            "Mã OTP website Trao đổi tài liệu SGU của bạn là : " + otpCode)
                    .create();

            return true;
        }catch (Exception ex){
            throw new ServerInternalException("Không thể gửi OTP " + ex.getMessage());
        }

    }
}
