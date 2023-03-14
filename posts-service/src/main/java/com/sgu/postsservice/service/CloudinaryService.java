package com.sgu.postsservice.service;

import com.sgu.postsservice.exception.InternalServerException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CloudinaryService {
    public Map<?, ?> upload(MultipartFile multipartFile,String folder) throws InternalServerException;
}
