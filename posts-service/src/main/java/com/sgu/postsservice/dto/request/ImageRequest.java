package com.sgu.postsservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ImageRequest {
    @NonNull
    @NotBlank(message = "Hình ảnh không thể rỗng")
    private String url;
}
