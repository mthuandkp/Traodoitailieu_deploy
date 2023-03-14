package com.sgu.userservice.model;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pagination {
    private int page;
    private int size;
    private int total_page;
    private long total_size;
}
