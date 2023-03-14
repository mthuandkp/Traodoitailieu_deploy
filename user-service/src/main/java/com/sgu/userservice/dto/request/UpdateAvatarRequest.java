package com.sgu.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class UpdateAvatarRequest {
    @NonNull
    @NotBlank(message = "username can't be blank")
    private String username;
}
