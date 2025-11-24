package com.shop_api.backend.service.comment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop_api.backend.dto.CommentDto;
import com.shop_api.backend.repository.CommentRepository;

@Service
public class CommentServiceImpl implements CommentService {

  @Autowired
  private CommentRepository commentRepository;

  @Override
  public CommentDto createComment(CommentDto commentDto) {

    return CommentDto.fromEntity(commentRepository.save(CommentDto.toEntity(commentDto)));
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
