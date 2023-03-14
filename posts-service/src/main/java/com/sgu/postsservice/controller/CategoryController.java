package com.sgu.postsservice.controller;

import com.sgu.postsservice.dto.request.CategoryRequest;
import com.sgu.postsservice.dto.request.DeleteCategory;
import com.sgu.postsservice.dto.response.HttpResponseEntity;
import com.sgu.postsservice.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/get-all")
    public ResponseEntity<HttpResponseEntity> getAllAccount(){
        HttpResponseEntity httpResponseEntity = categoryService.getAll();

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseEntity);
    }

    @GetMapping("/get-all-with-pagination")
    public ResponseEntity<HttpResponseEntity> getAllAccountWithPagination(
            @RequestParam(name = "page", required = true) int page,
            @RequestParam(name = "size", required = true) int size
    ){
        HttpResponseEntity httpResponseEntity = categoryService.getAllWithPagiantion(page,size);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseEntity);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<HttpResponseEntity> getAllAccountWithPagination(
            @PathVariable(name = "id") Long id
    ){
        HttpResponseEntity httpResponseEntity = categoryService.getById(id);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseEntity);
    }

    @GetMapping("/get-by-slug/{slug}")
    public ResponseEntity<HttpResponseEntity> getBySlug(
            @PathVariable(name = "slug") String slug
    ){
        HttpResponseEntity httpResponseEntity = categoryService.getBySlug(slug);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseEntity);
    }

    @PostMapping("/create")
    public ResponseEntity<HttpResponseEntity> create(
            @RequestBody @Valid CategoryRequest categoryRequest
    ){
        HttpResponseEntity httpResponseEntity = categoryService.create(categoryRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(httpResponseEntity);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<HttpResponseEntity> updateById(
            @PathVariable(name = "id") Long id,
            @RequestBody @Valid CategoryRequest categoryRequest
    ){
        HttpResponseEntity httpResponseEntity = categoryService.update(id,categoryRequest);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseEntity);
    }

    @PostMapping("/upload-image")
    public ResponseEntity<HttpResponseEntity> updateById(
            @RequestParam(name = "file") MultipartFile file
            ){
        HttpResponseEntity httpResponseEntity = categoryService.uploadImage(file);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseEntity);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<HttpResponseEntity> create(
            @RequestBody @Valid DeleteCategory deleteCategory
    ){
        HttpResponseEntity httpResponseObject = categoryService.delete(deleteCategory);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseObject);
    }
}
