package com.sgu.postsservice.repository;

import com.sgu.postsservice.model.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage,Long> {
}
