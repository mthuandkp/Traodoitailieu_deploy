package com.sgu.userservice.service;

import com.sgu.userservice.exception.InternalServerException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CloudinaryService {
    public Map<?, ?> upload(MultipartFile multipartFile) throws InternalServerException;
}
