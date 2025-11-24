package com.shop_api.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop_api.backend.dto.CommentDto;
import com.shop_api.backend.service.comment.CommentService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("${api.prefix}/comments")
@PreAuthorize("isAuthenticated()")
public class CommentController {

  @Autowired
  private CommentService commentService;

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto) {
    return ResponseEntity.ok(commentService.createComment(commentDto));
  }

  @GetMapping
  public ResponseEntity<List<CommentDto>> getAllComments() {
    return ResponseEntity.ok(commentService.getAllComments());
  }

}
