package com.sgu.postsservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostsHiddenRequest {
    @NotNull(message = "id bài viết không tồn tại")
    private Long postsId;
}
