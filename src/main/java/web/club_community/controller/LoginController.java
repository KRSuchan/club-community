package web.club_community.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import web.club_community.kakao.KakaoApi;

import java.util.Map;

@RequiredArgsConstructor
@RestController("/api/login")
public class LoginController {
    private final KakaoApi kakaoApi;

    @GetMapping("/oauth/code/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code) {
        // 1. 인가 코드 받기
        // 2. 토큰 받기
        String accessToken = kakaoApi.getAccessToken(code);

        // 3. 사용자 정보 받기
        Map<String, Object> userInfo = kakaoApi.getUserInfo(accessToken);

        String email = (String) userInfo.get("email");
        String nickname = (String) userInfo.get("nickname");

        System.out.println("email = " + email);
        System.out.println("nickname = " + nickname);
        System.out.println("accessToken = " + accessToken);

        return ResponseEntity.ok(email);
    }
}
