package com.sgu.postsservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sgu.postsservice.constant.PostStatus;
import com.sgu.postsservice.model.Category;
import com.sgu.postsservice.model.PostImage;
import com.sgu.postsservice.utils.DateUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class PostsResponse {
    private Long id;
    private Long accountId;
    private String title;
    private String body;
    private Long price;
    private String postsSlug;
    private PostStatus postStatus;
    private String reasonBlock;
    private String thumbnail;
    private String createdAt;
    private String updatedAt;
    private Category category;
    private List<String> imageList;
}
