// 패키지: Service 계층 (비즈니스 로직 담당)
package com.example.userservice.service;

import java.util.List;
import java.util.stream.Collectors;
// 이 클래스가 스프링이 관리하는 Service(빈) 임을 표시
import org.springframework.stereotype.Service;
// 도메인 엔티티 (DB 테이블과 매핑)
import com.example.userservice.domain.User;
// 회원가입 요청 DTO
import com.example.userservice.dto.SignUpRequestDto;
import com.example.userservice.dto.UserResponseDto;
// DB 접근을 위한 Repository
import com.example.userservice.repository.UserRepository;
// 트랜잭션 처리 애노테이션 (메서드 실행 중 예외 발생 시 롤백)
import jakarta.transaction.Transactional;

@Service                       // 스프링이 자동으로 빈으로 등록, 비즈니스 로직 위치
public class UserService {
  private final UserRepository userRepository; // DB 작업을 위한 의존성

  // 생성자 주입: 테스트 용이, 순환참조 방지, 불변필드(final) 사용 가능
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

	@Transactional                // 회원가입 과정 전체를 하나의 트랜잭션으로 묶음
  public void signUp(SignUpRequestDto signUpRequestDto) {
    // DTO -> 엔티티 변환 (현재는 단순 값 매핑; 실제 서비스에서는 비밀번호 암호화 필요)
    User user = new User(
      signUpRequestDto.getEmail(),   // 이메일 설정
      signUpRequestDto.getName(),    // 이름 설정
      signUpRequestDto.getPassword() // 비밀번호 설정 (실제로는 BCrypt 같은 해시 필요)
    );

    // 영속성 컨텍스트에 저장 -> 트랜잭션 커밋 시 DB INSERT 실행
    this.userRepository.save(user);
  }

  // 여러 사용자 ID로 사용자 정보 조회
  public List<UserResponseDto> getUsersByIds(List<Long> ids) {
    List<User> users = userRepository.findAllById(ids);
    
    return users.stream()
        .map(user -> new UserResponseDto(
            user.getUserId(),
            user.getEmail(),
            user.getName()
        ))
        .collect(Collectors.toList());
  }

  // 사용자 ID로 사용자 정보 조회
  public UserResponseDto getUser(Long id) {
    // ID로 사용자 조회, 없으면 예외 발생
    User user = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    
    // 🔑 new를 붙이는 이유:
    // 1. 매번 "새로운" 객체를 메모리에 생성해야 함
    // 2. new 없이는 객체 자체를 만들 수 없음 (Java 문법 규칙)
    // 3. 각 사용자마다 다른 데이터를 담은 "별도의" 객체가 필요함
    
    // ✅ new 있을 때: 요청마다 새로운 객체 생성
    // GET /users/1 호출 → UserResponseDto 객체1 생성 (userId=1, email="a@a.com", name="김철수")
    // GET /users/2 호출 → UserResponseDto 객체2 생성 (userId=2, email="b@b.com", name="이영희")
    // → 각각 다른 메모리 공간에 다른 데이터를 담은 별도의 객체!
    
    // ❌ new 없으면: 컴파일 에러! 
    // return UserResponseDto(user.getUserId()...) // 이건 문법적으로 불가능
    // Java에서 객체를 생성하려면 반드시 new 키워드가 필요함
    return new UserResponseDto(
        user.getUserId(),   // 현재 조회된 사용자의 ID
        user.getEmail(),    // 현재 조회된 사용자의 이메일
        user.getName()      // 현재 조회된 사용자의 이름
    );
    // 결과: 이 사용자만의 데이터를 담은 새로운 DTO 객체가 생성되어 반환됨!
  }
}