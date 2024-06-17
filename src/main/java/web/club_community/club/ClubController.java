package web.club_community.club;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.club_community.club.dto.ClubDashboardResponse;
import web.club_community.club.dto.ClubUpdateForm;
import web.club_community.config.SessionConst;
import web.club_community.domain.Club;
import web.club_community.domain.Member;
import web.club_community.exception.runtime.SessionNotFoundException;

import javax.naming.AuthenticationException;

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
        Club updatedClub = clubService.updateClub(loginMember, form, mainPicture);
        ClubDashboardResponse data = ClubDashboardResponse.of(updatedClub);
        return ResponseEntity.ok().body(data);
    }
}
