// 패키지 선언: 이 클래스가 속한 패키지 (폴더 구조와 매핑)
package com.example.userservice;

// Spring Boot 애플리케이션을 실행하기 위한 유틸 클래스
import org.springframework.boot.SpringApplication;
// @SpringBootApplication 에 필요한 자동 설정, 컴포넌트 스캔 등을 제공하는 애노테이션이 들어있는 패키지
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication: 아래 3가지를 합친 복합 애노테이션
// 1) @SpringBootConfiguration (== @Configuration) : 빈 설정 클래스 역할
// 2) @EnableAutoConfiguration : classpath, 설정 등을 보고 자동 설정 수행
// 3) @ComponentScan : 현재 패키지 기준 하위 패키지를 스캔하여 @Component/@Service/@Repository/@Controller 등을 빈으로 등록
@SpringBootApplication
public class UserServiceApplication {
	// 자바 애플리케이션의 시작점 (프로그램 시작 시 가장 먼저 호출되는 메서드)
	public static void main(String[] args) {
		// SpringApplication.run: 스프링 컨테이너를 생성하고 내장 톰캣을 띄우며 애플리케이션을 실행
		SpringApplication.run(UserServiceApplication.class, args);
	}
}
