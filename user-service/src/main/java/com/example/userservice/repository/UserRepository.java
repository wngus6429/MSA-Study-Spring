// 패키지: Repository 계층 (DB 접근 담당)
package com.example.userservice.repository;

// 스프링 데이터 JPA가 제공하는 기본 CRUD 메서드 포함 인터페이스
import org.springframework.data.jpa.repository.JpaRepository;
// User 엔티티 import
import com.example.userservice.domain.User;

// JpaRepository<엔티티 타입, 기본키 타입>
// findAll, findById, save, delete 등 기본 메서드를 자동 구현
public interface UserRepository extends JpaRepository<User, Long> {
	// 추후 이메일로 조회하고 싶다면 메서드 시그니처만 추가하면 됨 예) Optional<User> findByEmail(String email);
}