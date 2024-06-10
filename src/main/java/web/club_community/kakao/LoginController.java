package web.club_community.kakao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController("")
public class LoginController {
    private final KakaoService kakaoService;

    @Value("${kakao.api_key}")
    private String client_id;

    @Value("${kakao.redirect_uri}")
    private String redirect_uri;

    @GetMapping("/api/callback")
    public ResponseEntity<KakaoUserInfoResponseDto> callback(@RequestParam("code") String code) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);
        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/api/kakao")
    public ResponseEntity<?> getOauth() {
        return ResponseEntity.ok("https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + client_id + "&redirect_uri=" + redirect_uri);
    }
}
