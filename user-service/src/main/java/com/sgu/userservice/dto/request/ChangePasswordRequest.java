package com.sgu.userservice.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class ChangePasswordRequest {

    @NotBlank(message = "username can't be blank ")
    @NonNull
    private String username;
    @NotBlank(message = "new password can't be blank")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
    private String newPassword;
    @NonNull
    @Min(value = 100000)
    @Max(value = 999999)
    private Integer otpCode;
}
