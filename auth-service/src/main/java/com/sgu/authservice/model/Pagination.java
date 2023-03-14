package com.sgu.authservice.model;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pagination {
    private int page;
    private int limit;
    private int total_page;
    private long total_item;
}
