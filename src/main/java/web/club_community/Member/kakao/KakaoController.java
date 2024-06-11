package web.club_community.Member.kakao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController("")
public class KakaoController {
    private final KakaoService kakaoService;

    @Value("${kakao.api_uri}")
    private String api_uri;

    @GetMapping("/api/callback")
    public ResponseEntity<KakaoUserInfoResponseDto> callback(@RequestParam("code") String code) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);
        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/api/login/kakao-uri")
    public ResponseEntity<?> getOAuth() {
        return ResponseEntity.ok(api_uri);
    }
}
