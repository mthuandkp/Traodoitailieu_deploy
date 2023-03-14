package com.sgu.authservice.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@NonNull
public class LoginRequest {
    @NonNull
    private String username;
    @NonNull
    private String password;
}
