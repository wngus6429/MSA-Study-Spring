# User Service API 동작 흐름 정리

이 문서는 User Service의 각 API가 어떤 순서로 동작하는지 코드 레벨에서 상세하게 설명합니다.

---

## 📌 1. 사용자 조회 API (GET /users/{userId})

### 요청 예시

```http
GET http://localhost:8080/users/3
```

### 응답 예시

```json
{
  "userId": 3,
  "email": "wngus6429@gmail.com",
  "name": "박주현"
}
```

### 코드 실행 흐름

#### ① **HTTP 요청 수신** → `UserController.getUser()`

```java
// 📁 UserController.java

@GetMapping("{userId}")
public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId) {
    UserResponseDto userResponseDto = userService.getUser(userId);
    return ResponseEntity.ok(userResponseDto);
}
```

**동작:**

- 클라이언트가 `GET /users/3` 요청
- `@PathVariable`로 URL의 `{userId}` 값(3)을 `Long userId` 매개변수에 바인딩
- `userService.getUser(3)` 호출하여 비즈니스 로직 위임

---

#### ② **비즈니스 로직 처리** → `UserService.getUser()`

```java
// 📁 UserService.java

public UserResponseDto getUser(Long id) {
    // ID로 사용자 조회, 없으면 예외 발생
    User user = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    // 엔티티 -> DTO 변환 후 반환
    return new UserResponseDto(
        user.getUserId(),
        user.getEmail(),
        user.getName()
    );
}
```

**동작:**

- `userRepository.findById(3)` 호출하여 DB에서 사용자 조회
- 조회 결과가 없으면 `IllegalArgumentException` 발생 (400 에러 응답)
- 조회 성공 시 `User` 엔티티를 `UserResponseDto`로 변환

---

#### ③ **데이터베이스 조회** → `UserRepository.findById()`

```java
// 📁 UserRepository.java

public interface UserRepository extends JpaRepository<User, Long> {
    // findById는 JpaRepository가 자동 제공하는 메서드
}
```

**동작:**

- JPA가 자동으로 다음 SQL 실행:
  ```sql
  SELECT * FROM users WHERE user_id = 3;
  ```
- 결과를 `User` 엔티티 객체로 매핑하여 `Optional<User>` 반환

---

#### ④ **엔티티 → DTO 변환** → `UserResponseDto` 생성

```java
// 📁 UserResponseDto.java

public class UserResponseDto {
    private Long userId;
    private String email;
    private String name;

    public UserResponseDto(Long userId, String email, String name) {
        this.userId = userId;
        this.email = email;
        this.name = name;
    }
    // getter 메서드들...
}
```

**동작:**

- `User` 엔티티에서 필요한 정보만 추출
- **비밀번호는 제외**하여 보안 강화
- DTO 객체 생성 후 Service로 반환

---

#### ⑤ **HTTP 응답 생성** → `ResponseEntity.ok()` 반환

```java
// 📁 UserController.java (다시)

return ResponseEntity.ok(userResponseDto);
```

**동작:**

- `ResponseEntity.ok()`는 **HTTP 200 OK** 상태 코드와 함께 응답 생성
- Spring이 `UserResponseDto`를 자동으로 JSON으로 변환 (Jackson 라이브러리 사용)
- 최종 응답:
  ```json
  {
    "userId": 3,
    "email": "wngus6429@gmail.com",
    "name": "박주현"
  }
  ```

---

### 전체 흐름 요약

```
[클라이언트]
    ↓ GET /users/3
[1] UserController.getUser(3)
    ↓ userService.getUser(3) 호출
[2] UserService.getUser(3)
    ↓ userRepository.findById(3) 호출
[3] UserRepository.findById(3)
    ↓ SQL: SELECT * FROM users WHERE user_id = 3
[4] User 엔티티 반환 ← DB
    ↓ User → UserResponseDto 변환
[5] UserResponseDto 반환 ← Service
    ↓ ResponseEntity.ok(dto) 생성
[6] HTTP 200 + JSON 응답 → 클라이언트
```

---

### 계층별 책임

| 계층           | 파일                   | 역할                      |
| -------------- | ---------------------- | ------------------------- |
| **Controller** | `UserController.java`  | HTTP 요청/응답 처리       |
| **Service**    | `UserService.java`     | 비즈니스 로직 수행        |
| **Repository** | `UserRepository.java`  | DB 접근                   |
| **Domain**     | `User.java`            | 엔티티 (DB 테이블과 매핑) |
| **DTO**        | `UserResponseDto.java` | 응답 데이터 전송 객체     |

---

## 📌 (추가 예정: 다른 API 기능들)

- 회원가입 API (POST /users/sign-up)
- 기타 기능...
