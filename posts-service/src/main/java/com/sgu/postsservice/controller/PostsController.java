package com.sgu.postsservice.controller;

import com.sgu.postsservice.dto.request.PostRequest;
import com.sgu.postsservice.dto.request.PostUpdateRequest;
import com.sgu.postsservice.dto.request.PostsDeleteRequest;
import com.sgu.postsservice.dto.request.PostsHiddenRequest;
import com.sgu.postsservice.dto.response.HttpResponseEntity;
import com.sgu.postsservice.service.PostsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/posts")
public class PostsController {
    @Autowired
    private PostsService postsService;
    @GetMapping("/get-all")
    public ResponseEntity<HttpResponseEntity> getAllPosts(){
        HttpResponseEntity httpResponseObject = postsService.getAll();

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseObject);
    }

    @GetMapping("/get-all-with-pagination")
    public ResponseEntity<HttpResponseEntity> getAllPostsWithPagination(
            @RequestParam(name = "page", required = true) int page,
            @RequestParam(name = "size", required = true) int size
    ){
        HttpResponseEntity httpResponseObject = postsService.getAllWithPagination(page,size);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseObject);
    }

    @GetMapping("/get-all-available")
    public ResponseEntity<HttpResponseEntity> getAllAvailablePosts(){
        HttpResponseEntity httpResponseObject = postsService.getAllAvailablePosts();

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseObject);
    }

    @GetMapping("/get-all-available-with-pagination")
    public ResponseEntity<HttpResponseEntity> getAllAvailablePostsWithPagination(
            @RequestParam(name = "page", required = true) int page,
            @RequestParam(name = "size", required = true) int size
    ){
        HttpResponseEntity httpResponseObject = postsService.getAllAvailablePostsWithPagination(page,size);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseObject);
    }

    @GetMapping("/search-by-keyword/{keyword}")
    public ResponseEntity<HttpResponseEntity> searchByKeyword(
            @PathVariable("keyword") String keyword
    ){
        HttpResponseEntity httpResponseObject = postsService.searchByKeyWord(keyword);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseObject);
    }

    @GetMapping("/search")
    public ResponseEntity<HttpResponseEntity> searchMultipleCondition(
            @RequestParam(name = "categoryId",required = false) Long categoryId,
            @RequestParam(name = "isNewest",required = true) Boolean isNewest,
            @RequestParam(name = "keyword",required = false) String keyword,
            @RequestParam(name = "page",required = true) Integer page,
            @RequestParam(name = "size",required = true) Integer size
    ){
        HttpResponseEntity httpResponseObject = postsService.searchMultipleCondition(
                categoryId,isNewest,keyword,page,size);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseObject);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<HttpResponseEntity> getById(
            @PathVariable("id") Long id
    ){
        HttpResponseEntity httpResponseObject = postsService.getById(id);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseObject);
    }

    @GetMapping("/get-by-posts-slug/{slug}")
    public ResponseEntity<HttpResponseEntity> getBySlug(
            @PathVariable("slug") String slug
    ){
        HttpResponseEntity httpResponseObject = postsService.getBySlug(slug);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseObject);
    }

    @GetMapping("/get-by-category-slug/{slug}")
    public ResponseEntity<HttpResponseEntity> getBySlug(
            @PathVariable("slug") String slug,
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size
    ){
        HttpResponseEntity httpResponseObject = postsService.getByCategorySlug(slug,page,size);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseObject);
    }

    @GetMapping("/get-by-account-id/{id}")
    public ResponseEntity<HttpResponseEntity> getByAccountId(
            @PathVariable("id") Long id
    ){
        HttpResponseEntity httpResponseObject = postsService.getByAccountId(id);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseObject);
    }

    @GetMapping("/get-by-category-id/{id}")
    public ResponseEntity<HttpResponseEntity> getByCategoryId(
            @PathVariable("id") Long id
    ){
        HttpResponseEntity httpResponseObject = postsService.getByCategoryId(id);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseObject);
    }

    @PostMapping("/create-posts")
    public ResponseEntity<HttpResponseEntity> createPost(
            @RequestBody @Valid PostRequest postRequest
    ){
        HttpResponseEntity httpResponseEntity = postsService.createPosts(postRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(httpResponseEntity);
    }

    @PostMapping("/update-post/{id}")
    public ResponseEntity<HttpResponseEntity> updatePost(
            @PathVariable("id") Long id,
            @RequestBody @Valid PostUpdateRequest postUpdateRequest
    ){
        HttpResponseEntity httpResponseEntity = postsService.updatePosts(id,postUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseEntity);
    }

    @PostMapping("/upload-multiple-posts-image")
    public ResponseEntity<HttpResponseEntity> updatePost(
            @RequestParam("files") MultipartFile[] files
    ){
        HttpResponseEntity httpResponseEntity = postsService.uploadMultiFiles(files);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseEntity);
    }

    @PostMapping("/user-hidden-posts")
    public ResponseEntity<HttpResponseEntity> userHiddenPosts(
        @RequestBody @Valid PostsHiddenRequest postsHiddenRequest
    ){
        HttpResponseEntity httpResponseEntity = postsService.userHidden(postsHiddenRequest);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseEntity);
    }

    @PostMapping("/user-unhidden-posts")
    public ResponseEntity<HttpResponseEntity> userUnHiddenPosts(
            @RequestBody @Valid PostsHiddenRequest postsHiddenRequest
    ){
        HttpResponseEntity httpResponseEntity = postsService.userUnhidden(postsHiddenRequest);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseEntity);
    }

    @DeleteMapping("/user-delete")
    public ResponseEntity<HttpResponseEntity> userDelete(
            @RequestBody @Valid PostsDeleteRequest postsDeleteRequest
    ){
        HttpResponseEntity httpResponseEntity = postsService.userDelete(postsDeleteRequest);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseEntity);
    }

    @DeleteMapping("/admin-delete")
    public ResponseEntity<HttpResponseEntity> adminDelete(
            @RequestBody @Valid PostsDeleteRequest postsDeleteRequest
    ){
        HttpResponseEntity httpResponseEntity = postsService.adminDelete(postsDeleteRequest);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseEntity);
    }

}
