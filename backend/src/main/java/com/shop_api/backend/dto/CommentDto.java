package com.shop_api.backend.dto;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop_api.backend.entity.Comment;

import lombok.Data;

@Data
public class CommentDto {

  @JsonProperty("customerName")
  private String customerName;

  @JsonProperty("customerComment")
  private String customerComment;

  @JsonProperty("aiReply")
  private String aiReply;

  @JsonProperty("sentiment")
  private String sentiment;

  @JsonProperty("commentId")
  private String commentId;

  public static Comment toEntity(CommentDto commentDto) {
    final Comment comment = new Comment();

    comment.setCustomerName(commentDto.getCustomerName());
    comment.setCustomerComment(commentDto.getCustomerComment());
    comment.setAiReply(commentDto.getAiReply());
    comment.setSentiment(commentDto.getSentiment());
    comment.setCommentId(commentDto.getCommentId());
    comment.setCreatedAt(Instant.now());
    comment.setUpdatedAt(Instant.now());

    return comment;
  }

  public static CommentDto fromEntity(Comment comment) {
    final CommentDto commentDto = new CommentDto();

    commentDto.setCustomerName(comment.getCustomerName());
    commentDto.setCustomerComment(comment.getCustomerComment());
    commentDto.setAiReply(comment.getAiReply());
    commentDto.setSentiment(comment.getSentiment());
    commentDto.setCommentId(comment.getCommentId());

    return commentDto;
  }

  public static List<CommentDto> fromEntities(List<Comment> comments) {
    return comments.stream().map(CommentDto::fromEntity).collect(Collectors.toList());
  }
}
