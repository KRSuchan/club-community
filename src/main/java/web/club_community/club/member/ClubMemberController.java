package web.club_community.club.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.club_community.club.dto.ClubMemberResponse;
import web.club_community.config.SessionConst;
import web.club_community.domain.ClubMember;
import web.club_community.domain.Member;
import web.club_community.exception.runtime.SessionNotFoundException;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/club-member")
public class ClubMemberController {
    private final ClubMemberService clubMemberService;

    // 마스터 회원의 부원 조회
    @GetMapping("/list")
    public ResponseEntity<List<ClubMemberResponse>> list(HttpServletRequest request) throws AuthenticationException {
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
        List<ClubMember> clubMembers = clubMemberService.findClubMembersByMaster(loginMember);
        List<ClubMemberResponse> data = clubMembers
                .stream()
                .map(ClubMemberResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(data);
    }

    @DeleteMapping("/{clubMemberId}/delete")
    public ResponseEntity<?> delete(HttpServletRequest request, @PathVariable Integer clubMemberId) throws AuthenticationException {
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
        clubMemberService.deleteClubMember(loginMember, clubMemberId);
        return ResponseEntity.ok().build();
    }
}
