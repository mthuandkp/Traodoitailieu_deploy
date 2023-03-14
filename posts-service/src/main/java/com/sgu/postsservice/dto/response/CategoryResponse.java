package com.sgu.postsservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sgu.postsservice.model.Posts;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private String url;
    private String createdAt;
    private String updatedAt;
    private String categorySlug;
    private List<Posts> postsList;
}
