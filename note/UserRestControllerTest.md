# đ UserRestController Test

### íě ę°ě ěąęłľ / ě¤í¨ íě¤í¸

```java
package com.hospital.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.review.domain.dto.UserDto;
import com.hospital.review.domain.dto.UserJoinReqDto;
import com.hospital.review.domain.exception.ErrorCode;
import com.hospital.review.domain.exception.HospitalReviewAppException;
import com.hospital.review.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UserRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("íěę°ě ěąęłľ")
    void join_success() throws Exception {
        UserJoinReqDto userJoinReqDto = UserJoinReqDto.builder()
                .userName("Soyeong")
                .password("1234")
                .email("aaaa@likelion.com")
                .build();

        when(userService.join(any())).thenReturn(mock(UserDto.class));

        mockMvc.perform(post("/api/v1/users/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userJoinReqDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("íěę°ě ě¤í¨")
    void join_fail() throws Exception {
        UserJoinReqDto userJoinReqDto = UserJoinReqDto.builder()
                .userName("Soyeong")
                .password("1234")
                .email("aaaa@likelion.com")
                .build();

        when(userService.join(any())).thenThrow(new HospitalReviewAppException(ErrorCode.DUPLICATED_USER_NAME, ""));

        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinReqDto)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

}
```

- when() ëŠěëëĽź íľí´ íě ę°ěě ěëíě ëě ěěě ę˛°ęłźëĽź ě íěŹ íě¤í¸ (Stubbing, ě¤í°ëš)

  đ íě ę°ě ěąęłľ ě : Mock()ě ě´ěŠí userDto (ę°ě§) í´ëě¤ëĽź ěěě ëŚŹí´ę°ěźëĄ ě í¨

  đ íě ę°ě ě¤ í¨ ě: HospitalReviewAppExceptionëĄ ěě¸ëĽź ëě§

**ě¤í ę˛°ęłź**

![image-20221128155851331](./assets/image-20221128155851331.png)

<br />

### Spring Security ě ěŠ í UserRestControllerTest Refactoring

build.gradle ě `implementation 'org.springframework.security:spring-security-test'` ëźě´ë¸ëŹëŚŹ ěśę°

```java
package com.hospital.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.review.domain.dto.UserDto;
import com.hospital.review.domain.dto.UserJoinReqDto;
import com.hospital.review.domain.exception.ErrorCode;
import com.hospital.review.domain.exception.HospitalReviewAppException;
import com.hospital.review.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UserRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("íěę°ě ěąęłľ")
    @WithMockUser
    void join_success() throws Exception {
        UserJoinReqDto userJoinReqDto = UserJoinReqDto.builder()
                .userName("Soyeong")
                .password("1234")
                .email("aaaa@likelion.com")
                .build();

        when(userService.join(any())).thenReturn(mock(UserDto.class));

        mockMvc.perform(post("/api/v1/users/join")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userJoinReqDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("íěę°ě ě¤í¨")
    @WithMockUser
    void join_fail() throws Exception {
        UserJoinReqDto userJoinReqDto = UserJoinReqDto.builder()
                .userName("Soyeong")
                .password("1234")
                .email("aaaa@likelion.com")
                .build();

        when(userService.join(any())).thenThrow(new HospitalReviewAppException(ErrorCode.DUPLICATED_USER_NAME, ""));

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinReqDto)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

}
```

- `@WebMvcTest`ěě ěě˛­ě í  ë 401 Unauthorized ěëŹ ë°ě đ `@WithMockUser`ëĽź ě ě¸íěŹ ęśíě ę°ě´ ëę˛¨ěŁźě´
- `@WithMockUser`ëĽź ěŹěŠí í  403 Forbidden ěëŹ ë°ě đ `.wth(csrf())` ěśę°