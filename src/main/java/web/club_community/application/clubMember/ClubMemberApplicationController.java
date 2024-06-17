package web.club_community.application.clubMember;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;
import web.club_community.application.clubMember.dto.ApplicationResultResponse;
import web.club_community.application.clubMember.dto.ClubMemberApplicationCreateResponse;
import web.club_community.application.clubMember.dto.ClubMemberApplicationResponse;
import web.club_community.config.SessionConst;
import web.club_community.domain.ClubMemberApplication;
import web.club_community.domain.Member;
import web.club_community.exception.runtime.SessionNotFoundException;

import javax.naming.AuthenticationException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/application/club-member")
public class ClubMemberApplicationController {
    private final ClubMemberApplicationService clubMemberApplicationService;

    @PostMapping("/create/{clubId}")
    public ResponseEntity<ClubMemberApplicationCreateResponse> create(@PathVariable Integer clubId,
                                                                      @RequestPart(value = "applicationFile") MultipartFile applicationFile,
                                                                      HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        if (applicationFile.isEmpty()) {
            throw new IllegalArgumentException("입력 정보가 잘못되었습니다.");
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        ClubMemberApplication application = clubMemberApplicationService.save(clubId, loginMember, applicationFile);
        ClubMemberApplicationCreateResponse data = ClubMemberApplicationCreateResponse.of(loginMember, application);
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ClubMemberApplicationResponse>> create(HttpServletRequest request) throws AuthenticationException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        if (!loginMember.getRoles().contains("master")) {
            throw new AuthenticationException("권한이 없습니다.");
        }
        List<ClubMemberApplication> application = clubMemberApplicationService.findClubMemberApplicationByMaster(loginMember);
        List<ClubMemberApplicationResponse> data = application.stream()
                .map(ClubMemberApplicationResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/file/{applicationId}")
    public ResponseEntity<Resource> download(@PathVariable Integer applicationId,
                                             HttpServletRequest request) throws MalformedURLException, AuthenticationException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        if (!loginMember.getRoles().contains("master")) {
            throw new AuthenticationException("권한이 없습니다.");
        }
        ClubMemberApplication application = clubMemberApplicationService.findById(applicationId);
        String uploadFileName = application.getFileName();
        UrlResource resource = new UrlResource("file:" + application.getFilePath());
        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

    // 동아리장의 입부 신청 승인
    @PutMapping("/{applicationId}/approve")
    public ResponseEntity<ApplicationResultResponse> approve(@PathVariable Integer applicationId,
                                                             HttpServletRequest request) throws AuthenticationException {
        log.info("request {}", request);
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null || !loginMember.getRoles().contains("master")) {
            throw new AuthenticationException("권한이 없습니다.");
        }
        ClubMemberApplication application = clubMemberApplicationService.approveApplication(applicationId);
        ApplicationResultResponse data = ApplicationResultResponse.of(application);
        return ResponseEntity.ok().body(data);
    }

    // 동아리장의 입부 신청 거절
    @PutMapping("/{applicationId}/reject")
    public ResponseEntity<ApplicationResultResponse> reject(@PathVariable Integer applicationId,
                                                            HttpServletRequest request) throws AuthenticationException {
        log.info("request {}", request);
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null || !loginMember.getRoles().contains("master")) {
            throw new AuthenticationException("권한이 없습니다.");
        }
        ClubMemberApplication application = clubMemberApplicationService.rejectApplication(applicationId);
        ApplicationResultResponse data = ApplicationResultResponse.of(application);
        return ResponseEntity.ok().body(data);
    }
}
