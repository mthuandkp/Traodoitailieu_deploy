package com.sgu.postsservice.dto.request;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class DeleteCategory {
    @NonNull
    private Long id;
}
