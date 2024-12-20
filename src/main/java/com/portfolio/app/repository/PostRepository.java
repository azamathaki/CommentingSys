package com.portfolio.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.app.model.Post;

public interface PostRepository extends JpaRepository<Post, Long>{
    
}
