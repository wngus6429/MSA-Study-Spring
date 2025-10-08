package com.example.boardservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.example.boardservice.dto.UserResponseDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class UserClient {

  private final RestClient restClient;

  public UserClient(
      @Value("${client.user-service.url}") String userServiceUrl
  ) {
    this.restClient = RestClient.builder()
        .baseUrl(userServiceUrl)
        .build();
  }

  public List<UserResponseDto> fetchUsersByIds(List<Long> ids) {
    try {
      return this.restClient.get()
          .uri(uriBuilder -> uriBuilder
              .path("/users")
              .queryParam("ids", ids)
              .build()
          )
          .retrieve()
          .body(new ParameterizedTypeReference<>() {});
    } catch (RestClientException e) {
      // 로깅 : 예외 발생 시 로그를 남겨 문제를 파악할 수 있게 해야 함
      // log.error(...)
      return Collections.emptyList();
    }
  }

  public Optional<UserResponseDto> fetchUser(Long userId) {
    try {
      UserResponseDto userResponseDto = this.restClient.get()
          .uri("/users/{userId}", userId)
          .retrieve()
          .body(UserResponseDto.class);
      return Optional.ofNullable(userResponseDto);
    } catch (RestClientException e) {
      // 로깅 : 예외 발생 시 로그를 남겨 문제를 파악할 수 있게 해야 함
      // log.error("사용자 정보 조회 실패: {}", e.getMessage(), e);
      return Optional.empty();
    }
  }
}