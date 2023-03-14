package com.sgu.postsservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostRequest {
    @NotNull(message = "Id tài khoản không tồn tại")
    private Long accountId;
    @NotNull(message = "Id danh mục bài viết không tồn tại")
    private Long categoryId;
    @NotNull(message = "Tiêu đề bài viết không tồn tại")
    @NotBlank(message = "Tiêu đề bài viết không thể rỗng")
    @Length(min = 15,message = "Tiêu đề bài viết phải có tối thiểu 15 ký tự")
    private String title;
    @NotNull(message = "Nội dung bài viết không tồn tại")
    @NotBlank(message = "Nội dung bài viết không thể rỗng")
    @Length(min = 50,message = "Nội dung bài viết ít nhất 50 ký tự")
    private String body;
    @NotNull(message = "Giá không tồn tại")
    @Min(value = 0,message = "Giá tiền phải > 0")
    private Long price;
    @NotNull(message = "Hình ảnh bài viết không tồn tại")
    @NotEmpty(message = "Hình ảnh bài viết không được rỗng")
    private List<ImageRequest> imageList;
}
