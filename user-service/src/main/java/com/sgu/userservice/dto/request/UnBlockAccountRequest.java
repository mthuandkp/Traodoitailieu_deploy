package com.sgu.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class UnBlockAccountRequest {
    @NonNull
    @NotBlank(message = "user name can't blank")
    private String username;
}
