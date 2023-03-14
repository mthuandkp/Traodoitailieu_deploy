package com.sgu.postsservice.service;

import com.sgu.postsservice.dto.request.PostRequest;
import com.sgu.postsservice.dto.request.PostUpdateRequest;
import com.sgu.postsservice.dto.request.PostsDeleteRequest;
import com.sgu.postsservice.dto.request.PostsHiddenRequest;
import com.sgu.postsservice.dto.response.HttpResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface PostsService {
    public HttpResponseEntity createPosts(PostRequest postRequest);

    public HttpResponseEntity getAll();

    public HttpResponseEntity getAllAvailablePosts();

    public HttpResponseEntity getAllAvailablePostsWithPagination(int page, int size);

    public HttpResponseEntity getAllWithPagination(int page, int size);

    public HttpResponseEntity searchByKeyWord(String keyword);

    public HttpResponseEntity searchMultipleCondition(Long categoryId, Boolean isNewest, String keyword, Integer page, Integer size);

    public HttpResponseEntity updatePosts(Long id, PostUpdateRequest postUpdateRequest);

    public HttpResponseEntity uploadMultiFiles(MultipartFile[] files);

    public HttpResponseEntity getById(Long id);

    public HttpResponseEntity getByAccountId(Long id);

    public HttpResponseEntity getByCategoryId(Long id);

    public HttpResponseEntity userHidden(PostsHiddenRequest postsHiddenRequest);

    public HttpResponseEntity userUnhidden(PostsHiddenRequest postsHiddenRequest);

    public HttpResponseEntity userDelete(PostsDeleteRequest postsDeleteRequest);

    public HttpResponseEntity adminDelete(PostsDeleteRequest postsDeleteRequest);

    public HttpResponseEntity getByCategorySlug(String slug, int page, int size);

    public HttpResponseEntity getBySlug(String slug);
}
