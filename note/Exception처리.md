# 📌 Exception 처리

### RestControllerAdvice 적용하기

**ExceptionManager**

```java
package com.hospital.review.domain.exception;

import com.hospital.review.domain.entity.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {
    @ExceptionHandler(HospitalReviewAppException.class)
    public ResponseEntity<?> hospitalReviewAppExceptionHandler(HospitalReviewAppException e) {
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(e.getErrorCode().getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> RuntimeExceptionHandler(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error(e.getMessage()));
    }
}
```

- `@ControllerAdvice` 또는 `@RestControllerAdvice` 선언 시 컨트롤러로부터 던져진 예외를 `@ExceptionHandler`가 선언된 메서드가 받아 예외를 처리함
- `@RestControllerAdvice`는 `@ControllerAdvice` 와 달리 결괏값을 JSON 형태로 반환할 수 있음

**ErrorCode**

```java
package com.hospital.review.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "User name is duplicated.");

    private HttpStatus status;
    private String message;
}
```

❓ Enum이란?

👉 미리 지정 해놓고 그 값 말고 다른 값들을 넣지 못하게 하여 예측한 범위 내에서 프로그램이 작동하도록 하기 위한 기능 

ex) 계절, 요일, 역할 등을 정의할 때 사용

**HospitalReviewAppException**

```java
package com.hospital.review.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HospitalReviewAppException extends RuntimeException{

    private ErrorCode errorCode;
    private String message;

    @Override
    public String toString() {
        if(message == null) return errorCode.getMessage();
        return String.format("%s. %s", errorCode.getMessage(), message);
    }
}
```

- Message가 null이라면 에러코드를 ErrorCode에 담긴 message를 return