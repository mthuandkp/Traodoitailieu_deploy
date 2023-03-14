package com.sgu.userservice.service;

import com.sgu.userservice.model.EmailDetails;

public interface EmailService {


        // Method
        // To send a simple email
        public Boolean sendSimpleMail(EmailDetails details);

}

