package com.sgu.postsservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostsDeleteRequest {
    @NotNull(message = "id bài viết không tồn tại")
    private Long postsId;

    @NotNull(message = "Lý do xoá không tồn tại")
    @NotBlank(message = "Lý do xoá không thể rỗng")
    @Length(min = 5,message = "Lý do xoá phải có ít nhất 5 ký tự")
    private String reasonDelete;
}
