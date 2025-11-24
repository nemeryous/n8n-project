package com.shop_api.backend.service.comment;

import java.util.List;

import com.shop_api.backend.dto.CommentDto;

public interface CommentService {
  CommentDto createComment(CommentDto commentDto);

  CommentDto getCommentById(Integer id);

  List<CommentDto> getAllComments();

  CommentDto updateComment(Integer id, CommentDto commentDto);

  void deleteComment(Integer id);
}
