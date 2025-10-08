package com.example.userservice.dto;

// 사용자 정보를 클라이언트에 응답할 때 사용하는 DTO (서버 -> 클라이언트 전달용)
public class UserResponseDto {
  private Long userId;
  private String email;
  private String name;
  // 패스워드는 정보 노츨 하면 안되서 안 넣음

  // 생성자: new UserResponseDto(1L, "test@email.com", "홍길동") 이렇게 호출될 때 실행됨
  public UserResponseDto(Long userId, String email, String name) {
    // this.userId: 이 객체(UserResponseDto)의 필드를 가리킴
    // userId: 생성자로 전달받은 매개변수 값
    // 즉, 전달받은 값을 이 객체의 필드에 저장(초기화)하는 작업
    this.userId = userId;   // 매개변수 userId를 이 객체의 userId 필드에 저장
    this.email = email;     // 매개변수 email을 이 객체의 email 필드에 저장
    this.name = name;       // 매개변수 name을 이 객체의 name 필드에 저장
    
    // 결과: 3개의 데이터를 담고 있는 UserResponseDto 객체가 완성됨!
  }

  public Long getUserId() {
    return userId;
  }

  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }
}
