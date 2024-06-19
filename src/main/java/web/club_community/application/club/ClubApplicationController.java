package web.club_community.application.club;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.club_community.application.club.dto.*;
import web.club_community.config.SessionConst;
import web.club_community.domain.ClubApplication;
import web.club_community.domain.Member;
import web.club_community.exception.runtime.SessionNotFoundException;

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
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        if (bindingResult.hasErrors() || mainPicture == null || mainPicture.isEmpty()) {
            throw new IllegalArgumentException("입력 정보가 잘못되었습니다.");
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        ClubApplication savedApplication = clubApplicationService.save(form, loginMember, mainPicture);
        ClubApplicationCreateResponse data = ClubApplicationCreateResponse.of(savedApplication);
        return ResponseEntity.ok().body(data);
    }

    // 시스템 관리자의 동아리 신청 목록 조회 상태 확인
    @GetMapping("/list")
    public ResponseEntity<List<ClubApplicationListResponse>> clubApplicationList(HttpServletRequest request) throws AuthenticationException {
        log.info("request {}", request);
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
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

    // 시스템 관리자의 동아리 신청 승인
    @PutMapping("/{applicationId}/approve")
    public ResponseEntity<ClubApplicationResponse> approveClubApplication(@PathVariable Integer applicationId, HttpServletRequest request) throws AuthenticationException {
        log.info("request {}", request);
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null || !loginMember.getRoles().contains("admin")) {
            throw new AuthenticationException("권한이 없습니다.");
        }
        ClubApplication application = clubApplicationService.approveClubApplication(applicationId);
        ClubApplicationResponse data = ClubApplicationResponse.of(application);
        return ResponseEntity.ok().body(data);
    }

    // 시스템 관리자의 동아리 신청 거절
    @PutMapping("/{applicationId}/reject")
    public ResponseEntity<ClubApplicationResponse> rejectClubApplication(@PathVariable Integer applicationId,
                                                                         @Validated @RequestBody RejectApplicationRequest rejectRequest,
                                                                         BindingResult bindingResult,
                                                                         HttpServletRequest request) throws AuthenticationException {
        log.info("request {}", request);
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        if (bindingResult.hasErrors() || applicationId == null) {
            throw new IllegalArgumentException("입력 정보가 잘못되었습니다.");
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null || !loginMember.getRoles().contains("admin")) {
            throw new AuthenticationException("권한이 없습니다.");
        }
        ClubApplication application = clubApplicationService.rejectClubApplication(applicationId, rejectRequest);
        ClubApplicationResponse data = ClubApplicationResponse.of(application);
        return ResponseEntity.ok().body(data);
    }

    // 내가 신청한 동아리 검토 상태 조회
    @GetMapping("/my")
    public ResponseEntity<List<ClubApplicationResponse>> getMyApplications(HttpServletRequest request) throws AuthenticationException {
        log.info("request {}", request);
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            throw new AuthenticationException("권한이 없습니다.");
        }
        List<ClubApplication> applications = clubApplicationService.findMyClubApplication(loginMember);
        List<ClubApplicationResponse> data = applications
                .stream()
                .map(ClubApplicationResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(data);
    }
}
