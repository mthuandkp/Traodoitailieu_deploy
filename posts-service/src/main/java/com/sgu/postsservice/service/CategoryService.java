package com.sgu.postsservice.service;

import com.sgu.postsservice.dto.request.CategoryRequest;
import com.sgu.postsservice.dto.request.DeleteCategory;
import com.sgu.postsservice.dto.response.HttpResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface CategoryService {
    public HttpResponseEntity getAll();

    public HttpResponseEntity getAllWithPagiantion(int page, int size);

    public HttpResponseEntity getById(Long id);

    public HttpResponseEntity create(CategoryRequest categoryRequest);

    public HttpResponseEntity update(Long id, CategoryRequest categoryRequest);

    public HttpResponseEntity delete(DeleteCategory deleteCategory);

    public HttpResponseEntity uploadImage(MultipartFile file);

    public HttpResponseEntity getBySlug(String slug);
}
