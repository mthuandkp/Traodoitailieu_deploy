package com.sgu.userservice.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class ActiveAccountRequest {
    @NonNull
    @NotBlank(message = "username can't blank")
    private String username;
    @NonNull
    @Min(value = 100000,message = "OTP code must have 6 digits")
    @Max(value = 999999,message = "OTP code must have 6 digits")
    private int code;
}
