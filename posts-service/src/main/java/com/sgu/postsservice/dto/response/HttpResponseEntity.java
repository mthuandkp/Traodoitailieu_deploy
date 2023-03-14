package com.sgu.postsservice.dto.response;

import com.sgu.postsservice.utils.DateUtils;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HttpResponseEntity {
    private int code;
    private String message;
    private List<?> data;
    @Builder.Default
    private String time = DateUtils.getNowWithFormat();
    private Pagination pagination;

    public static HttpResponseEntity convertToResponeEntity(int code, String mesage, List<?> data,Pagination pagination) {
        return HttpResponseEntity.builder()
                .code(code)
                .message(mesage)
                .data(data)
                .pagination(pagination)
                .build();
    }
}
