// 패키지: controller 계층 (사용자 요청을 받는 웹 레이어)
package com.example.userservice.controller;

// HTTP 응답을 만들 때 사용하는 객체 (상태코드/헤더/바디 포함 가능)
import org.springframework.http.ResponseEntity;
// HTTP POST 요청을 매핑하기 위한 애노테이션
import org.springframework.web.bind.annotation.PostMapping;
// HTTP 요청 본문(JSON 등)을 자바 객체로 변환하여 매개변수에 바인딩
import org.springframework.web.bind.annotation.RequestBody;
// 클래스 레벨에서 공통 URL 경로를 지정
import org.springframework.web.bind.annotation.RequestMapping;
// 이 클래스가 REST API 컨트롤러임을 알리는 애노테이션 (@Controller + @ResponseBody)
import org.springframework.web.bind.annotation.RestController;
// 회원가입 요청 데이터를 담는 DTO (클라이언트 -> 서버 전달용)
import com.example.userservice.dto.SignUpRequestDto;
// 비즈니스 로직을 수행하는 서비스 계층
import com.example.userservice.service.UserService;

// @RestController: JSON 형태로 응답하는 컨트롤러 지정
// @RequestMapping("/users"): 이 클래스의 모든 엔드포인트 앞에 /users 가 붙음
@RestController
@RequestMapping("/users")
public class UserController {
  // 의존성 주입 받을 서비스 (회원 관련 비즈니스 로직 담당)
  private final UserService userService;

  // 생성자 주입: 스프링이 UserService 빈을 자동으로 넣어줌 (권장되는 주입 방식)
  public UserController(UserService userService) {
    this.userService = userService;
  }

  // POST /users/sign-up 요청을 처리 (회원가입 기능)
  @PostMapping("sign-up")
  public ResponseEntity<Void> signUp(
      // 요청 JSON(body)을 SignUpRequestDto로 변환해서 받음
      @RequestBody SignUpRequestDto signUpRequestDto
  ) {
    // 서비스 계층에 회원가입 처리 위임
    userService.signUp(signUpRequestDto);
    // 내용 없는 204 No Content 응답 반환 (성공했지만 바디 보낼 필요 없음)
    return ResponseEntity.noContent().build();
  }
}
