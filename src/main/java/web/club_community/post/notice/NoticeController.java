package web.club_community.post.notice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.club_community.config.SessionConst;
import web.club_community.domain.Member;
import web.club_community.domain.notice.Notice;
import web.club_community.domain.notice.PublicNotice;
import web.club_community.exception.runtime.SessionNotFoundException;
import web.club_community.post.notice.dto.NoticeCreateRequest;
import web.club_community.post.notice.dto.NoticeDashboardResponse;
import web.club_community.post.notice.dto.NoticeDetailResponse;
import web.club_community.post.notice.dto.NoticePageResponse;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {
    private final NoticeService noticeService;

    // 메인페이지 공지 slice
    @GetMapping("/main/{pageSize}")
    public ResponseEntity<List<NoticeDashboardResponse>> dashboardNotice(HttpServletRequest request,
                                                                         @PathVariable Integer pageSize) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        Pageable pageable = PageRequest.of(0, pageSize);
        Slice<Notice> notices = noticeService.findNoticesMainPage(pageable);
        List<NoticeDashboardResponse> data = notices.getContent()
                .stream()
                .map(NoticeDashboardResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(data);
    }

    // 공지 게시판 목록, 모든 공지 타입 리턴
    @GetMapping("/list/{pageNum}/{pageSize}/all")
    public ResponseEntity<NoticePageResponse> noticeListAll(HttpServletRequest request,
                                                            @PathVariable Integer pageNum,
                                                            @PathVariable Integer pageSize) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<Notice> notices = noticeService.findNoticesPageable(pageable);
        List<NoticeDashboardResponse> data = notices.getContent()
                .stream()
                .map(NoticeDashboardResponse::of)
                .collect(Collectors.toList());
        NoticePageResponse result = NoticePageResponse.of(data, notices.getTotalPages());
        return ResponseEntity.ok().body(result);
    }

    // 공지 게시판 목록, 학내 공지 타입 리턴
    @GetMapping("/list/{pageNum}/{pageSize}/entire")
    public ResponseEntity<NoticePageResponse> noticeListEntire(HttpServletRequest request,
                                                               @PathVariable Integer pageNum,
                                                               @PathVariable Integer pageSize) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<PublicNotice> notices = noticeService.findNoticesPageableEntire(pageable);
        List<NoticeDashboardResponse> data = notices.getContent()
                .stream()
                .map(NoticeDashboardResponse::of)
                .collect(Collectors.toList());
        NoticePageResponse result = NoticePageResponse.of(data, notices.getTotalPages());
        return ResponseEntity.ok().body(result);
    }

    // 공지 상세 정보 조회
    @GetMapping("/detail/{noticeId}")
    public ResponseEntity<NoticeDetailResponse> noticeDetail(HttpServletRequest request,
                                                             @PathVariable Integer noticeId) throws AuthenticationException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        Notice notice = noticeService.findByIdAndMember(noticeId, loginMember);
        NoticeDetailResponse data = NoticeDetailResponse.of(notice);
        return ResponseEntity.ok().body(data);
    }

    // 공지 등록
    @PostMapping("/create")
    public ResponseEntity<NoticeDashboardResponse> createNotice(HttpServletRequest request,
                                                                @Validated @RequestPart("data") NoticeCreateRequest noticeCreateRequest,
                                                                BindingResult bindingResult,
                                                                @RequestPart(value = "photo", required = false) MultipartFile photo) throws AuthenticationException {
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
        Notice notice = noticeService.createNotice(noticeCreateRequest, loginMember, photo);
        NoticeDashboardResponse data = NoticeDashboardResponse.of(notice);
        return ResponseEntity.ok().body(data);
    }
}