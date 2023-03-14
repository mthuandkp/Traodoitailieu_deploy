package com.sgu.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class BlockAccountRequest {
    @NotNull(message = "Username không tồn tại")
    @NotBlank(message = "username can't blank")
    private String username;
    @NotNull(message = "Lý do xoá không tồn tại")
    @NotBlank(message = "reason for blocking can't blank")
    private String reasonForBlocking;
}
