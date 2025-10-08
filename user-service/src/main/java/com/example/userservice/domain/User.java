// 패키지: 도메인(엔티티) 객체가 위치하는 곳
package com.example.userservice.domain;

// JPA 관련 애노테이션 import
import jakarta.persistence.Entity;          // JPA가 이 클래스를 테이블과 매핑하도록 표시
import jakarta.persistence.Table;           // 매핑될 실제 테이블 이름 지정
import jakarta.persistence.GeneratedValue;  // 기본키 값 자동 생성 전략 지정
import jakarta.persistence.GenerationType;  // 생성 전략 종류 (IDENTITY, SEQUENCE 등)
import jakarta.persistence.Id;              // 기본키 필드 표시

@Entity                   // 이 클래스가 JPA 엔티티(테이블과 매핑)임을 선언
@Table(name = "users")    // 이 엔티티가 매핑될 실제 DB 테이블명 지정
public class User {

  @Id                                           // 기본키(Primary Key) 필드
  @GeneratedValue(strategy = GenerationType.IDENTITY) // DB의 AUTO_INCREMENT( MySQL 등 ) 전략 사용
  private Long userId;                          // 사용자 고유 식별자
  private String email;                         // 이메일 (로그인 ID로 사용 가능)
  private String name;                          // 사용자 이름
  private String password;                      // 비밀번호 (실서비스에서는 반드시 암호화 필요)

  public User() {                               // JPA가 프록시 생성 시 필요 (기본 생성자 필수)
  }

  // 필요한 값만 받아서 객체를 생성하는 생성자
  public User(String email, String name, String password) {
    this.email = email;
    this.name = name;
    this.password = password;
  }

  // Getter: 외부에서 값 읽기 (엔티티는 보통 setter 최소화 -> 불변성 일부 유지)
  public Long getUserId() {
    return userId;
  }

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
