package com.example.boardservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.boardservice.dto.CreateBoardRequestDto;
import com.example.boardservice.service.BoardService;

@RestController
@RequestMapping("/boards")
public class BoardController {
  private final BoardService boardService;

  public BoardController(BoardService boardService) {
    this.boardService = boardService;
  }

  @PostMapping
  public ResponseEntity<Void> create(
      @RequestBody CreateBoardRequestDto createBoardRequestDto
  ) {
    boardService.create(createBoardRequestDto);
    return ResponseEntity.noContent().build();
  }
}
