package com.sgu.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendOTPRequest {
//    @NonNull
    @NotBlank(message = "email not blank")
    private String username;
}
