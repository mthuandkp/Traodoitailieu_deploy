package com.sgu.postsservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sgu.postsservice.utils.DateUtils;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "category")
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", unique = true, nullable = false)
    private Long id;
    private String name;
    private String url;
    @Builder.Default
    private String createdAt = DateUtils.getNow();
    @Builder.Default
    private String updatedAt = DateUtils.getNow();
    private String categorySlug;
    @JsonIgnore
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Posts> postsList;
}
