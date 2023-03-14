package com.sgu.userservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class PersonRequest {
    @NonNull
    @NotBlank(message = "name can't be blank")
    @Length(min = 1,max = 50)
    private String name;
    @NonNull
    @NotBlank(message = "address can't be blank")
    @Length(min = 20)
    private String address;
    @NonNull
    @NotBlank(message = "phone can't be blank")
    @Pattern(regexp="(0[3|5|7|8|9])+([0-9]{8})")
    private String phone;
    @NonNull
    @NotBlank(message = "birthday can't be blank")
    private String birthday;
    @NonNull
    private Boolean gender;
}
