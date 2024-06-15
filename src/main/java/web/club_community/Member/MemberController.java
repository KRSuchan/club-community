package web.club_community.Member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import web.club_community.Member.dto.request.MemberLoginRequest;
import web.club_community.Member.dto.request.MemberSignupRequest;
import web.club_community.Member.dto.response.LoginResponse;
import web.club_community.Member.dto.response.MemberSignupResponse;
import web.club_community.config.SessionConst;
import web.club_community.domain.Member;

@Controller
@RequestMapping("/api/member")
@Slf4j
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<MemberSignupResponse> signup(@Validated @RequestBody MemberSignupRequest request) {
        Member member = memberService.signupMember(request);
        MemberSignupResponse res = MemberSignupResponse.of(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Validated @RequestBody MemberLoginRequest req,
                                               BindingResult bindingResult,
                                               HttpServletRequest request) {
        log.info("request: {}", req);
        Member member = memberService.loginMember(req);
        if (member == null) {
            bindingResult.reject("login failed", "아이디 또는 비밀번호가 맞지 않습니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);
        LoginResponse data = LoginResponse.of(member);
        return ResponseEntity.ok().body(data);
    }
}
