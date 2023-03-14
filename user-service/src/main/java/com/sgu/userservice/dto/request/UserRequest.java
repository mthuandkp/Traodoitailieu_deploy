package com.sgu.userservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class UserRequest {
    @NotNull(message = "Tên người dùng không tồn tại")
    @NotBlank(message = "Tên người dùng không thể rỗng")
    @Length(min = 1,max = 50)
    private String name;
    @NotNull(message = "Địa chỉ dùng không tồn tại")
    @NotBlank(message = "Địa chỉ dùng không thể rỗng")
    @Length(min = 20)
    private String address;
    @NonNull
    @NotBlank(message = "phone can't be blank")
    @Pattern(regexp="(0[3|5|7|8|9])+([0-9]{8})")
    private String phone;
    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    @NotNull(message = "Ngày sinh không tồn tại")
    @NotBlank(message = "Ngày sinh không thể rỗng")
    private String birthday;
    @NotNull(message = "Giới tính không tồn tại")
    private Boolean gender;
    @NotNull(message = "Mssv không tồn tại")
    @NotBlank(message = "Mssv không thể rỗng")
    @Length(min = 10,max = 10,message = "Mã ssv phải có 10 ký tự")
    @Pattern(regexp = "^(31)+[0-9]{8}$")
    private String username;
    @NotBlank(message = "password can't be blank")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
    private String password;
}
