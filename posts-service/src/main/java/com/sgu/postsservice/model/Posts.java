package com.sgu.postsservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sgu.postsservice.constant.PostStatus;
import com.sgu.postsservice.utils.DateUtils;
import com.sgu.postsservice.utils.StringUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Table(name="posts")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    private Long accountId;
    private String title;
    private String body;
    private Long price;
    private String postsSlug;
    @Builder.Default
    private PostStatus postStatus = PostStatus.DISPLAY;
    @Builder.Default
    private String reasonBlock="";
    private String thumbnail;
    @Builder.Default
    private String createdAt = DateUtils.getNow();
    @Builder.Default
    private String updatedAt = DateUtils.getNow();

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Category.class)
    @JoinColumn(name = "category_id")
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ToString.Exclude
    private Category category;

    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ToString.Exclude
    @NotEmpty(message = "Image not empty")
    @NotNull(message = "image not null")
    private List<PostImage> postImageList;

    public int compareTitle(Posts post, List<String> keywordList) {
        int thisPoint = this.calcPriority(this.getTitle(),keywordList);
        int otherPoint = this.calcPriority(post.getTitle(),keywordList);

        if(thisPoint == otherPoint){
            return 0;
        }
        return thisPoint > otherPoint ? -1:1;
    }

    public static int calcPriority(String title, List<String> keywordList) {
        int priority = 0;
        String encodeTitle = StringUtils.convertTextToEnglish(title);
        for(String key: keywordList){
            if(encodeTitle.contains(key)){
                priority++;
            }
        }

        return priority;
    }

    public int compareTime(Posts p, Boolean isNewest) {
        Long thisTime = Long.valueOf(this.getUpdatedAt());
        Long otherTime = Long.valueOf(p.getUpdatedAt());
        if(thisTime == otherTime) return 0;

        if(isNewest){
            return thisTime < otherTime ? 1 : -1;
        }
        return thisTime > otherTime ? 1 : -1;

    }
}
