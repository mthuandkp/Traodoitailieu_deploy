package com.sgu.postsservice.repository;

import com.sgu.postsservice.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    public Optional<Category> findByName(String name);


    @Query(value = "SELECT * FROM category c WHERE c.category_slug=?1",nativeQuery = true)
    Optional<Category> findBySlug(String categorySlug);
}
