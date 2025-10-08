package com.example.boardservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.boardservice.dto.BoardResponseDto;
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

  @GetMapping()
  public ResponseEntity<List<BoardResponseDto>> getBoards() {
    List<BoardResponseDto> boardResponseDtos = boardService.getBoards();
    return ResponseEntity.ok(boardResponseDtos);
  }

  @GetMapping("/{boardId}")
  public ResponseEntity<BoardResponseDto> getBoard(@PathVariable Long boardId) {
    BoardResponseDto boardResponseDto = boardService.getBoard(boardId);
    return ResponseEntity.ok(boardResponseDto);
  }
}
