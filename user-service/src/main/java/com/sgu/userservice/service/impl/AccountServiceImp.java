package com.sgu.userservice.service.impl;

import com.sgu.userservice.constant.Constant;
import com.sgu.userservice.dto.request.*;
import com.sgu.userservice.dto.response.HttpResponseEntity;
import com.sgu.userservice.exception.*;
import com.sgu.userservice.model.*;
import com.sgu.userservice.repository.AccountRepository;
import com.sgu.userservice.repository.PersonRepository;
import com.sgu.userservice.service.CloudinaryService;
import com.sgu.userservice.service.EmailService;
import com.sgu.userservice.service.AccountService;
import com.sgu.userservice.service.OTPSmsService;
import com.sgu.userservice.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AccountServiceImp implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private OTPSmsService otpSmsService;
    @Autowired
    private PersonRepository personRepository;

    @Override
    public HttpResponseEntity getAllPerson() {

        return null;
    }

    @Override
    public HttpResponseEntity getAllAccount() {
        List<Account> accountList = accountRepository.findAll();
        HttpResponseEntity httpResponseEntity = HttpResponseEntity.convertToResponeEntity(
                HttpStatus.OK.value(),
                Constant.SUCCESS,
                accountList,
                null
        );


        return httpResponseEntity;
    }

    @Override
    public HttpResponseEntity getAllAccountWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        Page<Account> accountPage = accountRepository.findAll(pageable);
        List<Account> accountList = accountPage.getContent();
        Pagination pagination = Pagination.builder()
                .page(page)
                .size(size)
                .total_page(accountPage.getTotalPages())
                .total_size(accountPage.getTotalElements())
                .build();

        HttpResponseEntity httpResponseEntity = HttpResponseEntity.convertToResponeEntity(
                HttpStatus.OK.value(),
                Constant.SUCCESS,
                accountList,
                pagination
        );


        return httpResponseEntity;
    }

    @Override
    public HttpResponseEntity getAccoutByPersonId(Long personId) {
        Account account = accountRepository.findByPersonId(personId).orElseThrow(
                ()->new NotFoundException(String.format("Không thể tìm tài khoản có id=%s",personId))
        );


        
        HttpResponseEntity httpResponseEntity = HttpResponseEntity.convertToResponeEntity(
                HttpStatus.OK.value(),
                Constant.SUCCESS,
                Arrays.asList(account),
                null
        );
        return httpResponseEntity;
    }

    @Override
    public HttpResponseEntity getAccoutByUsername(String username) {
        Account account = accountRepository.findByUsername(username).orElseThrow(
                ()->new NotFoundException(String.format("Không thể tìm tài khoản có username=%s",username))
        );

        HttpResponseEntity httpResponseEntity = HttpResponseEntity.builder()
                .code(HttpStatus.OK.value())
                .message(Constant.SUCCESS)
                .data(Arrays.asList(account))
                .build();
        return httpResponseEntity;
    }

    @Override
    public HttpResponseEntity sendOtpCode(SendOTPRequest sendOTPRequest) {
        Account account = accountRepository.findByUsername(sendOTPRequest.getUsername()).orElseThrow(
                ()->new NotFoundException(
                        String.format("Không thể tìm tài khoản có username=%s"
                                , sendOTPRequest.getUsername()))
        );
        Person person = personRepository.findById(account.getPersonId()).orElseThrow(
                ()->new NotFoundException(
                        String.format("Không thể tìm người dùng có id=%s"
                                , account.getPersonId()))
        );

        if(account.getIsBlock()){
            throw new ForbiddenException("Account has block: " + account.getReasonForBlock());
        }

        String otpRandomCode = this.createOTP();

        Boolean isSend = otpSmsService.sendSMS(otpRandomCode,person.getPhone());
        if(!isSend){
            throw new BadGateWayException("Send mail fail");
        }

        //update otp code
        String updatedAt = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime());

        account.setOtpCode(otpRandomCode);
        account.setOtpCreatedAt(updatedAt);

        accountRepository.save(account);


        HttpResponseEntity httpResponseEntity = HttpResponseEntity.builder()
                .code(HttpStatus.OK.value())
                .message(Constant.SUCCESS)
                .build();
        return httpResponseEntity;
    }

    private String createOTP() {
        Long min = 100000L,max = 999999L;
        Long otpCode = ThreadLocalRandom.current().nextLong(min, max + 1);

        return String.valueOf(otpCode);
    }

    @Override
    public HttpResponseEntity activeAccount(ActiveAccountRequest activeAccountRequest) {
        Account account = accountRepository.findByUsername(activeAccountRequest.getUsername()).orElseThrow(
                ()->new NotFoundException(
                        String.format("Không thể tìm tài khoản có username=%s"
                                ,activeAccountRequest.getUsername()))
        );
        if(account.getIsBlock()){
            throw new ForbiddenException("Account has block: " + account.getReasonForBlock());
        }

        if(account.getIsActive()){
            throw new ForbiddenException("Account already active: ");
        }

        if(activeAccountRequest.getCode() != Integer.valueOf(account.getOtpCode())){
            throw new ForbiddenException("Otp code not correct");
        }

        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        long otpCreatedTime = Long.valueOf(account.getOtpCreatedAt());
        long diff = currentTimestamp.getTime() - otpCreatedTime;

        //Overcome 15p
        if(diff/60000 > 15){
            throw new ForbiddenException("Otp has expired");
        }

        account.setIsActive(true);
        account.setUpdatedAt(DateUtils.getNow());
        accountRepository.save(account);
        HttpResponseEntity httpResponseEntity = HttpResponseEntity.convertToResponeEntity(
                HttpStatus.OK.value(),
                Constant.SUCCESS,
                Arrays.asList(account),
                null
        );
        return httpResponseEntity;
    }

    @Override
    public HttpResponseEntity blockAccount(BlockAccountRequest blockAccountRequest) {
        String username = blockAccountRequest.getUsername();

        Account account = accountRepository.findByUsername(username).orElseThrow(
                ()->new NotFoundException(String.format("Không thể tìm tài khoản có username=%s",username))
        );

        if(account.getIsBlock()){
            throw new ForbiddenException("Account has block before : " + account.getReasonForBlock());
        }

        account.setIsBlock(true);
        account.setReasonForBlock(blockAccountRequest.getReasonForBlocking());
        account.setUpdatedAt(DateUtils.getNow());

        accountRepository.save(account);




        HttpResponseEntity httpResponseEntity = HttpResponseEntity.convertToResponeEntity(
                HttpStatus.OK.value(),
                Constant.SUCCESS,
                Arrays.asList(account),
                null
        );

        return httpResponseEntity;
    }

    @Override
    public HttpResponseEntity unBlockAccount(UnBlockAccountRequest unBlockAccountRequest) {
        String username = unBlockAccountRequest.getUsername();
        Account account = accountRepository.findByUsername(username).orElseThrow(
                ()->new NotFoundException(String.format("Không thể tìm tài khoản có username=%s",username))
        );

        if(!account.getIsBlock()){
            throw new ForbiddenException("Account hasn't block before : " + account.getReasonForBlock());
        }

        account.setIsBlock(false);
        account.setReasonForBlock("");
        account.setUpdatedAt(DateUtils.getNow());

        accountRepository.save(account);

        HttpResponseEntity httpResponseEntity = HttpResponseEntity.convertToResponeEntity(
                HttpStatus.OK.value(),
                Constant.SUCCESS,
                Arrays.asList(account),
                null
        );

        return httpResponseEntity;
    }

    @Override
    public HttpResponseEntity changePassword(ChangePasswordRequest changePasswordRequest) {
        String username = changePasswordRequest.getUsername();
        Account account = accountRepository.findByUsername(username).orElseThrow(
                ()->new NotFoundException(String.format("Không thể tìm tài khoản có username=%s",username))
        );

        if(account.getIsBlock()){
            throw new ForbiddenException("Account has block before : " + account.getReasonForBlock());
        }

        if(!account.getOtpCode().equals(String.valueOf(changePasswordRequest.getOtpCode()))){
            throw new ForbiddenException("OTP code isn't correct");
        }
        String encodePassword = new BCryptPasswordEncoder().encode(changePasswordRequest.getNewPassword());

        account.setPassword(encodePassword);
        account.setUpdatedAt(DateUtils.getNow());

        accountRepository.save(account);

        HttpResponseEntity httpResponseEntity = HttpResponseEntity.builder()
                .code(HttpStatus.OK.value())
                .message(Constant.SUCCESS)
                .build();
        return httpResponseEntity;

    }

    @Override
    public HttpResponseEntity uploadImage(String username, MultipartFile file) {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        Account account = accountRepository.findByUsername(username).orElseThrow(
                ()->new NotFoundException(String.format("Không thể tìm tài khoản có username=%s",username))
        );

        if(account.getIsBlock()){
            throw new ForbiddenException("Account has block: " + account.getReasonForBlock());
        }

        if(!account.getIsActive()){
            throw new ForbiddenException("Account hasn't active: ");
        }

        String contentType = file.getContentType();
        if(!contentType.equals("image/jpeg") && !contentType.equals("image/png")){
            throw new BadRequestException("Image only support 'jpg','jpeg' and 'png'");
        }

         //Upload
        try{
            Map<?,?> map = cloudinaryService.upload(file);
            String url = (String) map.get("url");

            account.setAvatar(url);

            accountRepository.save(account);
        }catch (InternalServerException e) {
            throw new RuntimeException(e);
        }


        HttpResponseEntity httpResponseEntity = HttpResponseEntity.builder()
                .code(HttpStatus.OK.value())
                .message(Constant.SUCCESS)
                .build();
        return httpResponseEntity;
    }

    @Override
    public HttpResponseEntity updateVnpay(String username, MultipartFile file) {
        Account account = accountRepository.findByUsername(username).orElseThrow(
                ()->new NotFoundException(String.format("Không thể tìm tài khoản có username=%s",username))
        );



        if(account.getIsBlock()){
            throw new ForbiddenException("Account has block: " + account.getReasonForBlock());
        }

        if(!account.getIsActive()){
            throw new ForbiddenException("Account hasn't active: ");
        }

        String contentType = file.getContentType();
        if(!contentType.equals("image/jpeg") && !contentType.equals("image/png")){
            throw new BadRequestException("Image only support 'jpg','jpeg' and 'png'");
        }

        //Upload
        try{
            Map<?,?> map = cloudinaryService.upload(file);
            String url = (String) map.get("url");

            account.setVnpayURL(url);

            accountRepository.save(account);
        }catch (InternalServerException e) {
            throw new RuntimeException(e);
        }


        HttpResponseEntity httpResponseEntity = HttpResponseEntity.builder()
                .code(HttpStatus.OK.value())
                .message(Constant.SUCCESS)
                .build();
        return httpResponseEntity;
    }
}
