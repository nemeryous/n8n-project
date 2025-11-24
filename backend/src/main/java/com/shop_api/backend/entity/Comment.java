package com.shop_api.backend.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "comments")
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "customer_name")
  private String customerName;

  @Column(name = "customer_comment")
  private String customerComment;

  @Column(name = "ai_reply")
  private String aiReply;

  @Column(name = "sentiment")
  private String sentiment;

  @Column(name = "comment_id")
  private String commentId;

  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;
  
}
