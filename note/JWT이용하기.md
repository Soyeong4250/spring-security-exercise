# π JWT μ΄μ©νκΈ°

### JWT(JSON Web Token)λ?

- λΉμ¬μ κ°μ μ λ³΄λ₯Ό JSON ννλ‘ μμ νκ² μ μ‘νκΈ° μν ν ν°

- μ£Όλ‘ μλ²μμ ν΅μ μμ κΆν μΈκ°(Authorization)λ₯Ό μν΄ μ¬μ©

  ![image-20221205145545128](./assets/image-20221205145545128.png)

- Header (ν€λ)

  π κ²μ¦κ³Ό κ΄λ ¨λ λ΄μ©μ λ΄κ³  μμ

  ```json
  {
      "alg" : "HS256",
      "typ" : "JWT"
  }
  ```

  π `alg` μμ±μ ν΄μ± μκ³ λ¦¬μ¦μ μ§μ νλ©°, ν ν°μ κ²μ¦ν  λ μ¬μ©λλ μλͺ λΆλΆμμ μ¬μ©

  π `typ` μμ±μ ν ν°μ νμμ μ§μ 

- Payload (λ΄μ©)

  - ν ν°μ λ΄λ μ λ³΄λ₯Ό ν¬ν¨νλ©°, ν¬ν¨λ μμ±λ€μ ν΄λ μ(Claim)μ΄λΌ λΆλ¦Ό

  - Registered Claims (λ±λ‘λ ν΄λ μ) : νμλ μλμ§λ§ ν ν°μ λν μ λ³΄λ₯Ό λ΄κΈ° μν΄ μ΄λ―Έ μ΄λ¦μ΄ μ ν΄μ Έ μλ ν΄λ μ

    π `iss` : λ°κΈμ(issue) μ£Όμ²΄

    π `sub` : JWTμ μ λͺ©(Subject)

    π `aud` : JWTμ μμ μΈ(Audience), μμ²­ μ²λ¦¬ μ£Όμ²΄κ° `aud`κ°μΌλ‘ μμ μ μλ³νμ§ μμΌλ©΄ JWTλ κ±°λΆλ¨

    π `exp` : λ§λ£μκ°(Expiration), μκ°μ NumericDate νμμΌλ‘ μ§μ 

    π `nbf` : 'Not Before'λ₯Ό μλ―Έ

    π `iat` : JWTκ° λ°κΈλ μκ°(issued at)

    π `jti` : JWTμ μλ³μ(JWT ID), μ€λ³΅ μ²λ¦¬ λ°©μ§λ₯Ό μν΄ μ¬μ©

  - Public Claims (κ³΅κ° ν΄λ μ) : ν€ κ°μ λ§μλλ‘ μ μν  μ μμ. λ¨, μΆ©λμ΄ λ°μνμ§ μμ μ΄λ¦μΌλ‘ μ€μ 
  - Private Claims (λΉκ³΅κ° ν΄λ μ) : ν΅μ  κ°μ μνΈ ν©μλκ³  λ±λ‘λ ν΄λ μκ³Ό κ³΅κ°λ ν΄λ μμ΄ μλ ν΄λ μμ μλ―Έ

- Signature(μλͺ) 

  - μΈμ½λ©λ ν€λ, μΈμ½λ©λ λ΄μ©, λΉλ°ν€, ν€λμ μκ³ λ¦¬μ¦ μμ±κ°μ κ°μ Έμ μμ±
  - ν ν°μ κ°λ€μ ν¬ν¨ν΄μ μνΈννκΈ° λλ¬Έμ λ©μμ§κ° λμ€μ λ³κ²½λμ§ μμλμ§ νμΈν  λ μ¬μ©

<br />

<br />

### Spring Security μ κ·Ό κΆν μ€μ 

##### Token μΈμ¦μ΄ μλ μ¬μ©μμ μ κ·Όμ λ§κΈ° μν Security Config μμ 

```java
package com.hospital.review.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                .antMatchers("/api/v1/users/join", "/api/v1/users/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/**").authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .build();
    }
}
```

π μ€ν κ²°κ³Ό - Review λ±λ‘ μλ μ 403 μλ¬ λ°μ

![Untitled](./assets/image-20221205145545129.png)

π κ·Έλ¬λ Joinμ μμμ `.antMatchers("/api/v1/users/join", "/api/v1/users/login").permitAll()`λ‘ νμ©ν΄μ£ΌμκΈ° λλ¬Έμ 200 μ±κ³΅

![image-20221129152223304.png](./assets/image-20221129152223304.png)

<br />

<br />

### JwtTokenFilter κ΅¬ν λ° μ μ©

**JwtTokenFilter.java**

```java
package com.hospital.review.configuration;

import com.hospital.review.domain.entity.User;
import com.hospital.review.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // κΆν λΆμ¬ μ νκΈ°
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("", null, List.of(new SimpleGrantedAuthority("USER")));
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);  // κΆν λΆμ¬
        filterChain.doFilter(request, response);
    }
}
```

- `OncePerRequestFilter`λ‘λΆν° μ€λ²λΌμ΄λ©ν `doFilterInternal()`λ©μλ κ΅¬ν

  π JwtTokenProviderλ₯Ό ν΅ν΄ servletRequestμμ ν ν°μ μΆμΆνκ³  ν ν°μ λν μ ν¨μ±μ κ²μ¬

  π μ ν¨ν ν ν°μ΄λΌλ©΄ Authentication κ°μ²΄λ₯Ό μμ±νμ¬ SecurityContextHolderμ μΆκ°νλ μμ μν

**SecurityConfig.java**

```java
package com.hospital.review.configuration;

import com.hospital.review.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    @Value("${jwt.token.secret}")
    private String secretKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                .antMatchers("/api/v1/users/join", "/api/v1/users/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/**").authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // jwt μ¬μ©νλ κ²½μ° μ¬μ©
                .and()
                .addFilterBefore(new JwtTokenFilter(userService, secretKey), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
```

- `.addFilterBefore(new JwtTokenFilter(userService, secretKey), UsernamePasswordAuthenticationFilter.class)`

  π μ§μ λ νν° μμ μ»€μ€ν νν°λ₯Ό μΆκ° (UsernamePasswordAuthenticationFilter.class)

<br />

<br />

### JWTλ₯Ό μΈμ  μ΄μ©ν΄μΌν κΉ? (μ κ·Όμ μΈμ  λ§μμΌ ν κΉ?)

1οΈβ£ Tokenμ΄ NullμΈ κ²½μ°

2οΈβ£ λ§λ£λ TokenμΈ κ²½μ°

3οΈβ£ μ μ νμ§ μμ TokenμΈ κ²½μ° (μ κ·Ό κΆνμ΄ μ μ νμ§ λͺ»ν κ²½μ°)

**JwtTokenFilter.java**

```java
package com.hospital.review.configuration;

import com.hospital.review.domain.entity.User;
import com.hospital.review.service.UserService;
import com.hospital.review.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authorizationHeader : {}", authorizationHeader);
		// 1οΈβ£
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.error("μΈμ¦ν€λκ° μλͺ» λμμ΅λλ€.");
            filterChain.doFilter(request, response);
            return;
        }

        String token;
        try {
            token = authorizationHeader.split(" ")[1];
        } catch (Exception e) {
            log.error("token μΆμΆμ μ€ν¨νμ΅λλ€.");
            filterChain.doFilter(request, response);
            return;
        }

        // 2οΈβ£
        if(JwtTokenUtil.isExpired(token, secretKey)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3οΈβ£
        // Tokenμμ Claimμμ  UserName κΊΌλ΄κΈ°
        String userName = JwtTokenUtil.getUserName(token, secretKey);
        log.info("userName : {}", userName);

        // UserDetailμμ κ°μ Έμ€κΈ°
        User user = userService.getUserByUserName(userName);
        log.info("userRole: {}", user.getRole());

        // Role λ°μΈλ©
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), null, List.of(new SimpleGrantedAuthority(user.getRole().name())));
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);  // κΆν λΆμ¬
        filterChain.doFilter(request, response);
    }
}

```

**JwtTokenUtil.java**

```java
package com.hospital.review.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenUtil {

    private static Claims extractClaims(String token, String key) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    // 3οΈβ£
    public static String getUserName(String token, String secretKey) {
        return extractClaims(token, secretKey).get("userName", String.class);
    }

    // 2οΈβ£
    public static boolean isExpired(String token, String secretKey) {
        Date expiredDate = extractClaims(token, secretKey).getExpiration();  // expire timestampλ₯Ό return
        return expiredDate.before(new Date());  // νμ¬λ³΄λ€ μ μΈμ§ check
    }

    public static String createToken(String userName, String key, long expireTimeMs) {
        Claims claims = Jwts.claims();  // μΌμ’μ map
        claims.put("userName", userName);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, key) 
                .compact()
                ;
    }
}
```

- JWT Token μμ± μ νμν κ²λ€

  π Claims - userName, μ’μ μ΄λ λ± μΈμ¦νκΈ° μν΄ νμν λ λ―Όκ°ν μ λ³΄λ€

  π IssuesAt - Token λ°κΈ μκ°

  π Expiration - Token λ§λ£ μκ°

  π signWith - μ΄λ€ secretKeyλ‘ μλͺν  κ²μΈμ§ (ν€λμ μκ³ λ¦¬μ¦, λΉλ°ν€ λ±μ ν¬ν¨)

- `private static Claims extractClaims(String token, String key) {
          return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
      }` λ₯Ό ν΅ν΄ secretKeyλ₯Ό κ°μ Έμ μΈμ¦

**User.java**

 ```java
 package com.hospital.review.domain.entity;
 
 import lombok.AllArgsConstructor;
 import lombok.Builder;
 import lombok.Getter;
 import lombok.NoArgsConstructor;
 
 import javax.persistence.*;
 
 @Entity
 @Builder
 @NoArgsConstructor
 @AllArgsConstructor
 @Getter
 public class User {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;
 
     @Column(unique = true)
     private String userName;
     private String password;
     private String email;
 
     // 3οΈβ£ κΆν Enumνμ λ©€λ²λ³μ μΆκ°
     @Enumerated(EnumType.STRING)
     private UserRole role;
 }
 ```

**UserRole.java**

```java
package com.hospital.review.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRole {
    ADMIN("ADMIN"),
    USER("USER");

    private String name;
}
```

**UserService.java**

```java
package com.hospital.review.service;

import com.hospital.review.domain.dto.UserDto;
import com.hospital.review.domain.dto.UserJoinReqDto;
import com.hospital.review.domain.entity.User;
import com.hospital.review.domain.exception.ErrorCode;
import com.hospital.review.domain.exception.HospitalReviewAppException;
import com.hospital.review.repository.UserRepository;
import com.hospital.review.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret}")
    private String secretKey;
    private long expireTimeMs = 1000 * 60 * 60;  // 1μκ°

    public UserDto join(UserJoinReqDto request) {
        userRepository.findByUserName(request.getUserName())
                .ifPresent(user -> {
                    throw new HospitalReviewAppException(ErrorCode.DUPLICATED_USER_NAME, String.format("UserName : %s", request.getUserName()));
                });

        User savedUser = userRepository.save(request.toEntity(encoder.encode(request.getPassword())));
        return UserDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .email(savedUser.getEmail())
                .build();
    }

    public String login(String userName, String password) {
        // userName μλμ§ νμΈ
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new HospitalReviewAppException(ErrorCode.NOT_FOUND, String.format("%sμ(κ³Ό) μΌμΉνλ νμμ΄ μμ΅λλ€.", userName)));

        // passwordκ° μΌμΉνλμ§ νμΈ
        if(!encoder.matches(password, user.getPassword())) {
            throw new HospitalReviewAppException(ErrorCode.INVALID_PASSWORD, "userName λλ passwordκ° μλͺ» λμμ΅λλ€.");
        }

        // 2κ°μ§ νμΈ μ€ μμΈκ° μλ€λ©΄ Token λ°ν
        return JwtTokenUtil.createToken(userName, secretKey, expireTimeMs);
    }

    // 3οΈβ£
    public User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new HospitalReviewAppException(ErrorCode.NOT_FOUND, ""));
    }
}
```

**UserController.java**

```java
package com.hospital.review.controller;

import com.hospital.review.domain.dto.ReviewCreateReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/reviews")
public class ReviewRestController {

    @PostMapping
    public String write(@RequestBody ReviewCreateReq dto, Authentication authentication) {
        log.info("Controller user: {}", authentication.getName());
        return  "λ¦¬λ·° λ±λ‘ μ±κ³΅";
    }
}
```

π μ€ν κ²°κ³Ό - μ¬μ©μμ roleκ³Ό userNameμ΄ logμ μ μ°νλ κ²μ λ³Ό μ μμ

![image-20221205164552434](./assets/image-20221205164552434.png)

π ν ν°μ΄ λ§λ£λ κ²½μ° - ExpiredJwtException λ°μ

![image-20221206144302164](./assets/image-20221206144302164.png)
