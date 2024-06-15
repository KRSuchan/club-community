package web.club_community.post.application.club;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.club_community.config.SessionConst;
import web.club_community.domain.ClubApplication;
import web.club_community.domain.Member;
import web.club_community.post.application.club.dto.ClubApplicationCreateForm;
import web.club_community.post.application.club.dto.ClubApplicationCreateResponse;
import web.club_community.post.application.club.dto.ClubApplicationListResponse;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/application/club")
public class ClubApplicationController {
    private final ClubApplicationService clubApplicationService;

    // 동아리 등록 요청
    @PostMapping("/create")
    public ResponseEntity<ClubApplicationCreateResponse> clubApplicationCreate(@Validated @RequestPart("data") ClubApplicationCreateForm form,
                                                                               BindingResult bindingResult,
                                                                               @RequestPart(value = "mainPicture") MultipartFile mainPicture,
                                                                               HttpServletRequest request) {
        log.info("form {}", form);
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (bindingResult.hasErrors() || mainPicture.isEmpty()) {
            throw new IllegalArgumentException("입력 정보가 잘못되었습니다.");
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        ClubApplication savedApplication = clubApplicationService.save(form, loginMember, mainPicture);
        ClubApplicationCreateResponse data = ClubApplicationCreateResponse.of(savedApplication);
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ClubApplicationListResponse>> clubApplicationList(HttpServletRequest request) throws AuthenticationException {
        log.info("request {}", request);
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null || !loginMember.getRoles().contains("admin")) {
            throw new AuthenticationException("권한이 없습니다.");
        }
        List<ClubApplicationListResponse> data = clubApplicationService.findAll()
                .stream()
                .map(ClubApplicationListResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(data);
    }
}
