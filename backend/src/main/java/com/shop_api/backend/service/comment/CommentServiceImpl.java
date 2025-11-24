package com.shop_api.backend.service.comment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop_api.backend.dto.CommentDto;
import com.shop_api.backend.entity.Comment;
import com.shop_api.backend.repository.CommentRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

  @Autowired
  private CommentRepository commentRepository;

  @Override
  public CommentDto createComment(CommentDto commentDto) {
    log.info("Creating comment: {}", commentDto);

    Comment comment = commentRepository.save(CommentDto.toEntity(commentDto));
    
    log.info("Comment created successfully with ID: {}", comment);
    return CommentDto.fromEntity(comment);
  }

  @Override
  public CommentDto getCommentById(Integer id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getCommentById'");
  }

  @Override
  public List<CommentDto> getAllComments() {
    return CommentDto.fromEntities(commentRepository.findAll());
  }

  @Override
  public CommentDto updateComment(Integer id, CommentDto commentDto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateComment'");
  }

  @Override
  public void deleteComment(Integer id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteComment'");
  }

}
