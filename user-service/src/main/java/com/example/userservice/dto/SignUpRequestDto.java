// 패키지: DTO(Data Transfer Object) - 계층 간 데이터 전달 전용 객체들 위치
package com.example.userservice.dto;

public class SignUpRequestDto {   // 회원가입 요청 시 클라이언트가 보내는 JSON을 받기 위한 클래스
  private String email;           // 사용자가 입력한 이메일
  private String name;            // 사용자가 입력한 이름
  private String password;        // 사용자가 입력한 비밀번호 (서버에서 암호화 처리 권장)

  // Getter 메서드: JSON -> 객체 변환 후 컨트롤러/서비스에서 값 읽기 위해 필요
  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }

  public String getPassword() {
    return password;
  }
}

