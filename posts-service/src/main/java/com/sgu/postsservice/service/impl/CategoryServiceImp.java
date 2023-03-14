package com.sgu.postsservice.service.impl;

import com.sgu.postsservice.constant.Constant;
import com.sgu.postsservice.dto.request.CategoryRequest;
import com.sgu.postsservice.dto.request.DeleteCategory;
import com.sgu.postsservice.dto.response.HttpResponseEntity;
import com.sgu.postsservice.dto.response.Pagination;
import com.sgu.postsservice.exception.*;
import com.sgu.postsservice.model.Category;
import com.sgu.postsservice.model.Posts;
import com.sgu.postsservice.repository.CategoryRepository;
import com.sgu.postsservice.repository.PostsRepository;
import com.sgu.postsservice.service.CategoryService;
import com.sgu.postsservice.service.CloudinaryService;
import com.sgu.postsservice.utils.DateUtils;
import com.sgu.postsservice.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImp implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private PostsRepository postsRepository;
    @Override
    public HttpResponseEntity getAll() {
        try{
            List<Category> categoryList = categoryRepository.findAll();
            HttpResponseEntity responseEntity = convertToResponeEntity(
                    HttpStatus.OK.value(),
                    Constant.SUCCESS,
                    categoryList,
                    null
            );

            return responseEntity;
        }catch (Exception ex){
            throw new ServerInternalException(ex.getMessage());
        }

    }




    @Override
    public HttpResponseEntity getAllWithPagiantion(int page, int size) {
        try{
            Pageable pageable = PageRequest.of(page-1,size);
            Page<Category> categoryPage = categoryRepository.findAll(pageable);
            List<Category> categoryList = categoryPage.getContent();

            Pagination pagination = Pagination.builder()
                    .page(page)
                    .size(size)
                    .total_page(categoryPage.getTotalPages())
                    .total_size(categoryPage.getTotalElements())
                    .build();

            HttpResponseEntity httpResponseEntity = convertToResponeEntity(
                    HttpStatus.OK.value(),
                    Constant.SUCCESS,
                    categoryList,
                    pagination
            );

            return httpResponseEntity;
        }catch (Exception ex){
            throw new ServerInternalException(ex.getMessage());
        }
    }

    @Override
    public HttpResponseEntity getById(Long id) {
        try{
            Category category = categoryRepository.findById(id).orElseThrow(
                    ()-> new NotFoundException(String.format("Can't find category with id = %s",id))
            );
            List<Category> categoryList = Arrays.asList(category);
            HttpResponseEntity responseEntity = convertToResponeEntity(
                    HttpStatus.OK.value(),
                    Constant.SUCCESS,
                    categoryList,
                    null
            );
            return responseEntity;
        }catch (Exception ex){
            throw new ServerInternalException(ex.getMessage());
        }

    }

    @Override
    public HttpResponseEntity create(CategoryRequest categoryRequest) {
        try{
            //check category name Exists
            if(categoryRepository.findByName(categoryRequest.getName()).isPresent()){
                throw new CategoryExistsException(String
                        .format("Category with name = '%s' is exists",categoryRequest.getName()));
            }


            Category saveCategory = this.convertToEntity(categoryRequest);

            saveCategory = categoryRepository.save(saveCategory);
            List<Category> categoryList = Arrays.asList(saveCategory);
            HttpResponseEntity responseEntity = convertToResponeEntity(
                    HttpStatus.OK.value(),
                    Constant.SUCCESS,
                    categoryList,
                    null
            );

            return responseEntity;
        }catch (CategoryExistsException e) {
            throw new CategoryExistsException(e.getMessage());
        }
        catch (Exception ex){
            throw new ServerInternalException(ex.getMessage());
        }
    }



    @Override
    public HttpResponseEntity update(Long id, CategoryRequest categoryRequest) {
        try{

            Category categoryDb = categoryRepository.findById(id).orElseThrow(
                    ()->new NotFoundException(String.format("Không thể tìm danh mục có id = %s",id))
            );

            categoryDb.setName(categoryDb.getName());
            categoryDb.setUrl(categoryRequest.getUrl());
            categoryDb.setUpdatedAt(DateUtils.getNow());

            Category saveCategory = categoryRepository.save(categoryDb);

            HttpResponseEntity httpResponseEntity = HttpResponseEntity.builder()
                    .code(HttpStatus.OK.value())
                    .message(Constant.SUCCESS)
                    .data(Arrays.asList(saveCategory))
                    .build();

            return httpResponseEntity;
        }catch (Exception ex){
            throw new ServerInternalException(ex.getMessage());
        }
    }

    @Override
    public HttpResponseEntity delete(DeleteCategory deleteCategory) {
        try{
            Long id = deleteCategory.getId();
            Category categoryDb = categoryRepository.findById(id).orElseThrow(
                    ()->new NotFoundException(String
                            .format("Danh mục có id=%s không tồn tại",id))
            );
            List<Posts> postsList = postsRepository.getByCategoryId(id);
            String postsId = postsList.stream().map(Posts::getId).collect(Collectors.toList()).toString();

            if(postsList.size() != 0){
                throw new ForbiddenException(String.format("Danh mục đang được sử dụng bởi bài viết id=%s",postsId));
            }


            List<Category> categoryList = Arrays.asList(categoryDb);

           categoryRepository.delete(categoryDb);
            HttpResponseEntity httpResponseEntity = convertToResponeEntity(
                    HttpStatus.OK.value(),
                    Constant.SUCCESS,
                    categoryList,
                    null
            );

            return httpResponseEntity;
        }catch (Exception ex){
            throw new ServerInternalException(ex.getMessage());
        }
    }

    @Override
    public HttpResponseEntity uploadImage(MultipartFile file) {
        try{
            String contentType = file.getContentType();
            Predicate<String> checkFileExtension = s -> {
                return !contentType.equals("image/jpeg") && !contentType.equals("image/png");
            };

            if(checkFileExtension.test(contentType)){
                throw new BadRequestException(String.format("Chỉ chấp nhận file png,jpg,jpeg"));
            }

            Map<?,?> map = cloudinaryService.upload(file,"category/");

            List<?> urlList = Arrays.asList(map.get("url"));

            HttpResponseEntity httpResponseEntity = HttpResponseEntity.builder()
                    .code(HttpStatus.OK.value())
                    .message(Constant.SUCCESS)
                    .data(urlList)
                    .build();

            return httpResponseEntity;
        }catch (InternalServerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public HttpResponseEntity getBySlug(String slug) {
        try{
            Category category = categoryRepository.findBySlug(slug).orElseThrow(
                    ()-> new NotFoundException(String.format("Không thể tìm danh mục với slug = %s",slug))
            );
            List<Category> categoryList = Arrays.asList(category);
            HttpResponseEntity responseEntity = convertToResponeEntity(
                    HttpStatus.OK.value(),
                    Constant.SUCCESS,
                    categoryList,
                    null
            );
            return responseEntity;
        }catch (Exception ex){
            throw new ServerInternalException(ex.getMessage());
        }
    }

    private Category convertToEntity(CategoryRequest categoryRequest) {
        return Category.builder()
                .name(categoryRequest.getName())
                .url(categoryRequest.getUrl())
                .categorySlug(StringUtils.createSlug(categoryRequest.getName()))
                .build();
    }

    private HttpResponseEntity convertToResponeEntity(int code, String mesage, List<?> data,Pagination pagination) {
        return HttpResponseEntity.builder()
                .code(code)
                .message(mesage)
                .data(data)
                .pagination(pagination)
                .build();
    }

}
