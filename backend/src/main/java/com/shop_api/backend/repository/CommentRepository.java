package com.shop_api.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.shop_api.backend.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> , JpaSpecificationExecutor<Comment> {

}
