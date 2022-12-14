# π Exception μ²λ¦¬

### RestControllerAdvice μ μ©νκΈ°

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

- `@ControllerAdvice` λλ `@RestControllerAdvice` μ μΈ μ μ»¨νΈλ‘€λ¬λ‘λΆν° λμ Έμ§ μμΈλ₯Ό `@ExceptionHandler`κ° μ μΈλ λ©μλκ° λ°μ μμΈλ₯Ό μ²λ¦¬ν¨
- `@RestControllerAdvice`λ `@ControllerAdvice` μ λ¬λ¦¬ κ²°κ΄κ°μ JSON ννλ‘ λ°νν  μ μμ

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

β Enumμ΄λ?

π λ―Έλ¦¬ μ§μ  ν΄λκ³  κ·Έ κ° λ§κ³  λ€λ₯Έ κ°λ€μ λ£μ§ λͺ»νκ² νμ¬ μμΈ‘ν λ²μ λ΄μμ νλ‘κ·Έλ¨μ΄ μλνλλ‘ νκΈ° μν κΈ°λ₯ 

ex) κ³μ , μμΌ, μ­ν  λ±μ μ μν  λ μ¬μ©

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

- Messageκ° nullμ΄λΌλ©΄ μλ¬μ½λλ₯Ό ErrorCodeμ λ΄κΈ΄ messageλ₯Ό return