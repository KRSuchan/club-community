package web.club_community.club;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;
import web.club_community.club.dto.ClubDashboardResponse;
import web.club_community.club.dto.ClubListResponse;
import web.club_community.club.dto.ClubUpdateForm;
import web.club_community.config.SessionConst;
import web.club_community.domain.Club;
import web.club_community.domain.Member;
import web.club_community.exception.runtime.SessionNotFoundException;

import javax.naming.AuthenticationException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/club")
public class ClubController {
    private final ClubService clubService;

    // 마스터 회원의 동아리 기본 정보 조회
    @GetMapping("/dashboard")
    public ResponseEntity<ClubDashboardResponse> dashboard(HttpServletRequest request) throws AuthenticationException {
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
        Club findedClub = clubService.findByMaster(loginMember);
        ClubDashboardResponse data = ClubDashboardResponse.of(findedClub);
        return ResponseEntity.ok().body(data);
    }

    // 마스터 회원의 동아리 기본 정보 수정
    @PutMapping("/update")
    public ResponseEntity<ClubDashboardResponse> update(@Validated @RequestPart("data") ClubUpdateForm form,
                                                        BindingResult bindingResult,
                                                        @RequestPart(value = "mainPicture", required = false) MultipartFile mainPicture,
                                                        @RequestPart(value = "applicationFile", required = false) MultipartFile applicationFile,
                                                        HttpServletRequest request) throws AuthenticationException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("입력 정보가 잘못되었습니다.");
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        if (!loginMember.getRoles().contains("master")) {
            throw new AuthenticationException("권한이 없습니다.");
        }
        Club updatedClub = clubService.updateClub(loginMember, form, mainPicture, applicationFile);
        ClubDashboardResponse data = ClubDashboardResponse.of(updatedClub);
        return ResponseEntity.ok().body(data);
    }

    // 일반회원의 동아리 입부 신청서 다운로드하기
    @GetMapping("/applicationFile/{clubId}")
    public ResponseEntity<Resource> download(@PathVariable Integer clubId,
                                             HttpServletRequest request) throws MalformedURLException, AuthenticationException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        Club club = clubService.findById(clubId);
        String uploadFileName = club.getApplicationName();
        UrlResource resource = new UrlResource("file:" + club.getApplicationPath());
        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

    // 일반 회원의 동아리 목록 조회
    @GetMapping("/list")
    public ResponseEntity<ClubListResponse> clubList(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        List<Club> clubs = clubService.findAll();
        ClubListResponse data = ClubListResponse.of(clubs, loginMember);
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/detail/{clubId}")
    public ResponseEntity<ClubDashboardResponse> clubDetail(@PathVariable Integer clubId,
                                                            HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        Club club = clubService.findById(clubId);
        ClubDashboardResponse data = ClubDashboardResponse.of(club);
        return ResponseEntity.ok().body(data);
    }
}
