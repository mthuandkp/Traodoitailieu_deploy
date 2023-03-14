package com.sgu.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class DeleteRequest {
    @NotNull(message = "id của tài khoản không tồn tại")
    private Long id;
    @NotNull(message = "Lý do khoá không tồn tại")
    @NotBlank(message = "Lý do khoá không thể rỗng")
    @Length(min = 20,message = "Lý do khoá phải có ít nhất 20 ký tự")
    private String reasonForBlock;
}
