# ๐ Review ๋ฑ๋ก

### Review ๋ฑ๋ก

๐ Review ๋ฑ๋ก์ ์์ Spring Security์ JWT ์ฌ์ฉ์ ์ํ ์ค์ ์ [JWT ์ด์ฉํ๊ธฐ](./JWT์ด์ฉํ๊ธฐ.md) ์ฐธ๊ณ 

**ReviewRestController.java**

```java
package com.token.authenticate.controller;

import com.token.authenticate.domain.dto.ReviewCreateReq;
import com.token.authenticate.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/reviews")
public class ReviewRestController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<String> write(@RequestBody ReviewCreateReq request, Authentication authentication) {
        log.info("writer : {}", authentication.getName());
        return ResponseEntity.ok()
                .body(reviewService.write(authentication.getName()));
    }
}
```

**ReviewService.java**

```java
package com.token.authenticate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    public String write(String userName) {
        return userName + "๋์ ๋ฆฌ๋ทฐ๊ฐ ๋ฑ๋ก๋์์ต๋๋ค.";
    }
}
```

๐ ์คํ ๊ฒฐ๊ณผ

![image-20221206144621449](./assets/image-20221206144621449.png)