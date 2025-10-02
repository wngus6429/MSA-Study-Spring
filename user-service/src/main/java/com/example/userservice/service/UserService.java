// 패키지: Service 계층 (비즈니스 로직 담당)
package com.example.userservice.service;

// 이 클래스가 스프링이 관리하는 Service(빈) 임을 표시
import org.springframework.stereotype.Service;
// 도메인 엔티티 (DB 테이블과 매핑)
import com.example.userservice.domain.User;
// 회원가입 요청 DTO
import com.example.userservice.dto.SignUpRequestDto;
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
}