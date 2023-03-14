package com.sgu.userservice.service.impl;

import com.sgu.userservice.constant.Constant;
import com.sgu.userservice.constant.Role;
import com.sgu.userservice.dto.request.DeleteRequest;
import com.sgu.userservice.dto.request.UserRequest;
import com.sgu.userservice.dto.response.HttpResponseEntity;
import com.sgu.userservice.exception.BadRequestException;
import com.sgu.userservice.exception.ForbiddenException;
import com.sgu.userservice.exception.NotFoundException;
import com.sgu.userservice.exception.UserNotFoundException;
import com.sgu.userservice.model.Account;
import com.sgu.userservice.model.Person;
import com.sgu.userservice.repository.AccountRepository;
import com.sgu.userservice.repository.PersonRepository;
import com.sgu.userservice.service.UserService;
import com.sgu.userservice.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public HttpResponseEntity register(UserRequest userRequest) {
        if(accountRepository.findByUsername(userRequest.getUsername()).isPresent()){
            throw new ForbiddenException(
                    String.format("Mssv=%s đã được đăng ký tài khoản",userRequest.getUsername()));
        }

        if(personRepository.getByPhone(userRequest.getPhone()).isPresent()){
            throw new ForbiddenException(
                    String.format("Phone=%s đã được đăng ký tài khoản",userRequest.getPhone()));
        }


        List<Person> personList = personRepository.findAll();
        personList.sort((p1, p2) -> {
            if (p1.getId() == p2.getId()) return 0;
            return p1.getId() > p2.getId() ? 1 : -1;
        });

        Long newId = 1L;
        try{
            newId = personList.get(personList.size() - 1).getId() + 1;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


        String now = DateUtils.getNow();

        Person newPerson = Person.builder()
                .id(newId)
                .name(userRequest.getName())
                .phone(userRequest.getPhone())
                .address(userRequest.getAddress())
                .birthday(userRequest.getBirthday())
                .gender(userRequest.getGender())
                .createdAt(now)
                .updatedAt(now)
                .build();

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


        Account newAccount = Account.builder()
                .personId(newId)
                .username(userRequest.getUsername())
                .password(bCryptPasswordEncoder.encode(userRequest.getPassword()))
                .role(Role.USER)
                .avatar("")
                .isBlock(false)
                .isActive(false)
                .otpCode("")
                .refreshToken("")
                .vnpayURL("")
                .otpCreatedAt("")
                .createdAt(now)
                .updatedAt(now)
                .build();

        Person savePerson = personRepository.save(newPerson);
        Account saveAccount = accountRepository.save(newAccount);

        HttpResponseEntity httpResponseEntity = HttpResponseEntity.builder()
                .code(HttpStatus.CREATED.value())
                .message(Constant.SUCCESS)
                .data(Arrays.asList(saveAccount, savePerson))
                .build();
        return httpResponseEntity;
    }

    @Override
    public HttpResponseEntity delete(DeleteRequest deleteRequest) {
        Long id = deleteRequest.getId();
        Optional<Person> personOptional = personRepository.findById(id);
        Optional<Account> accountOptional = accountRepository.findByPersonId(id);

        if(personOptional.isEmpty() || accountOptional.isEmpty()){
            throw new UserNotFoundException("Can't find account and person with id = " + id);
        }


        personRepository.delete(personOptional.get());
        accountRepository.delete(accountOptional.get());

        HttpResponseEntity httpResponseEntity = HttpResponseEntity.builder()
                .code(HttpStatus.OK.value())
                .message(Constant.SUCCESS)
                .data(Arrays.asList(personOptional.get(),accountOptional.get()))
                .build();
        return httpResponseEntity;

    }

}
