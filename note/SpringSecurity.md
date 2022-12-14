# ๐ Spring Security

### ๋น๋ฐ๋ฒํธ ์ํธํํ์ฌ DB ์ ์ฅํ๊ธฐ

**SecurityConfig**

```java
package com.hospital.review.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                .antMatchers("/api/**").permitAll()
                .antMatchers("/api/v1/users/join", "/api/v1/users/login").permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .build();
    }
}
```

- ๊ธฐ์กด์ Spring Boot์์๋ ๋ณด์ ํํฐ์ฒด์ธ ์ค์  ์ `WebSecurityConfigurerAdapter`๋ฅผ ์์๋ฐ์ ์ค์ ํ์์ง๋ง, ์ง๊ธ์ Bean์ผ๋ก ๋ฑ๋กํ์ฌ ์ฌ์ฉํ๋๋ก ๋ณ๊ฒฝ๋จ

- `.httpBasic().disable()` : ๊ธฐ๋ณธ ์ค์ ์ ๋น์ธ์ฆ ์ ๋ก๊ทธ์ธ ํผ ํ๋ฉด์ผ๋ก ๋ฆฌ๋ค์ด๋ ํธ ๋๋ฉฐ, Rest API์ด๋ฏ๋ก ๊ธฐ๋ณธ์ค์ ์ ์ฌ์ฉํ์ง ์๊ธฐ ์ํด disable() ์ค์ 

- `.csrf().disable()` : ์ ์์ ์ธ ์ฌ์ฉ์๊ฐ ์๋์น ์์ ์์กฐ์์ฒญ์ ๋ณด๋ด๋ ๊ฒ(Cross site Request forgery; csrf)๋ก๋ถํฐ ๋ณดํธํ๋ ๊ฒ์ ์ฌ์ฉํ์ง ์์ 

  ๐ Rest API ์๋ฒ๋ Stateless ์ํ์ด๋ฏ๋ก ์๋ฒ์ ์ธ์ฆ์ ๋ณด๋ฅผ ๋ณด๊ดํ์ง ์๊ธฐ ๋๋ฌธ์

- `.cors()` : ๊ต์ฐจ ์ถ์ฒ๋ฅผ ๊ณต์ ํ  ์ ์๋ ๊ตฌ๋ํ์ ๋ถ์ฌํ๋๋ก ๋ธ๋ผ์ฐ์ ์ ์๋ ค์ฃผ๋ ์ ์ฑ(Cross-Origin Resource Sharing; CORS) ์ผ๋ก, ์๋ก ๋ค๋ฅธ ์ถ์ฒ๋ฅผ ๊ฐ์ง Application์ด ์๋ก์ Resource์ ์ ๊ทผํ  ์ ์๋๋ก ํด์ค
- `.authorizeRequests()` : HttpServletRequest๋ฅผ ์ด์ฉ
- `.antMatchers().permitAll()` : ํน์  ๋ฆฌ์์ค์ ๋ํ ์ ๊ทผ์ ์ธ์ฆ์ ์ฐจ ์์ด ํ์ฉ
- `.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.์ ์ฑ์์)` 
  - `SessionCreation .ALWAYS` : ํญ์ ์ธ์์ ์์ฑ
  - `SessionCreation.if_REQUIRED` : ํ์์ ์์ฑ (๊ธฐ๋ณธ)
  - `SessionCreation.NEVER` : ์์ฑํ์ง ์๊ณ , ๊ธฐ์กด ์ธ์์ด ์กด์ฌํ๋ฉด ์ฌ์ฉ
  - `SessionCreation.STATELESS` : ์์ฑํ์ง ์๊ณ , ๊ธฐ์กด์ ์ธ์๋ ์ฌ์ฉํ์ง ์์

**EncrypterConfig**

```java
package com.hospital.review.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class EncrypterConfig {

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();  // password๋ฅผ ์ธ์ฝ๋ฉ ํด์ค๋ ์ฐ๊ธฐ ์ํจ
    }
}
```

- ๋น๋ฐ๋ฒํธ ์ํธํํ๋ ๋ฉ์๋
  - `BCryptPasswordEncoder` : ์คํ๋ง ์ํ๋ฆฌํฐ์์ ๊ธฐ๋ณธ์ ์ผ๋ก ์ฌ์ฉํ๋ ์ํธํ ๋ฐฉ์์ผ๋ก ์ํธํ๊ฐ ๋ ๋๋ง๋ค ์๋ก์ด ๊ฐ์ ์์ฑ, ์์์ ์ธ ๊ฐ์ ์ถ๊ฐํด์ ์ํธํํ์ง ์์๋ ๋จ
  - `StandardPasswordEncoder` : SHA-256 ์ํธํ ์ฌ์ฉ
  - `NoOpPasswordEncoder` : ์ํธํ๋ฅผ ์ฌ์ฉํ์ง ์์ ๋ ์ฌ์ฉ 

**๐ก ์คํ ๊ฒฐ๊ณผ**

![image-20221129120102033](./assets/image-20221129120102033.png)