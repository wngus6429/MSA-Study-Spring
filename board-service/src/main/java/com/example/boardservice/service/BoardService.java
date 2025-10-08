package com.example.boardservice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.boardservice.client.UserClient;
import com.example.boardservice.domain.Board;
import com.example.boardservice.domain.BoardRepository;
import com.example.boardservice.dto.BoardResponseDto;
import com.example.boardservice.dto.CreateBoardRequestDto;
import com.example.boardservice.dto.UserDto;
import com.example.boardservice.dto.UserResponseDto;

import jakarta.transaction.Transactional;

@Service
public class BoardService {
  private final BoardRepository boardRepository;
  private final UserClient userClient;

  public BoardService(BoardRepository boardRepository, UserClient userClient) {
    this.boardRepository = boardRepository;
    this.userClient = userClient;
  }

	@Transactional
  public void create(CreateBoardRequestDto createBoardRequestDto) {
    Board board = new Board(
      createBoardRequestDto.getTitle(),
      createBoardRequestDto.getContent(), 
      createBoardRequestDto.getUserId()  
    );

    this.boardRepository.save(board);
  }

  // 게시글 전체 조회
  public List<BoardResponseDto> getBoards() {
    List<Board> boards = boardRepository.findAll();

    // userId 목록 추출
    List<Long> userIds = boards.stream()
        .map(Board::getUserId)
        .distinct() // 중복 제거
        .toList();

    // user-service로부터 사용자 정보 불러오기
    List<UserResponseDto> userResponseDtos = userClient.fetchUsersByIds(userIds);

    // userId를 Key로 하는 Map 생성
    Map<Long, UserDto> userMap = new HashMap<>();
    for (UserResponseDto userResponseDto : userResponseDtos) {
      Long userId = userResponseDto.getUserId();
      String name = userResponseDto.getName();
      userMap.put(userId, new UserDto(userId, name));
    }

    // 게시글 정보와 사용자 정보를 조합해서 BoardResponseDto 만들기
    return boards.stream()
        .map(board -> new BoardResponseDto(
            board.getBoardId(),
            board.getTitle(),
            board.getContent(),
            userMap.get(board.getUserId()) // 맵에서 UserDto 가져오기
        ))
        .toList();
  }

  public BoardResponseDto getBoard(Long boardId) {
    // 게시글 불러오기
    Board board = boardRepository.findById(boardId)
        .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

    // user-service로부터 사용자 정보 불러오기
    Optional<UserResponseDto> optionalUserResponseDto = userClient.fetchUser(board.getUserId());
    
    // UserDto 생성
    UserDto userDto = null;
    if (optionalUserResponseDto.isPresent()) {
      UserResponseDto userResponseDto = optionalUserResponseDto.get();
      userDto = new UserDto(
          userResponseDto.getUserId(),
          userResponseDto.getName()
      );
    }
    
    // ResponseDto 생성
    BoardResponseDto boardResponseDto = new BoardResponseDto(
        board.getBoardId(),
        board.getTitle(),
        board.getContent(),
        userDto
    );

    return boardResponseDto;
  }
}