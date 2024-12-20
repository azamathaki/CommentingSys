package com.portfolio.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.app.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    
}
