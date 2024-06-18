package web.club_community.post.clubVideo;

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
import web.club_community.config.SessionConst;
import web.club_community.domain.ClubVideo;
import web.club_community.domain.Member;
import web.club_community.exception.runtime.SessionNotFoundException;
import web.club_community.post.clubVideo.dto.VideoCreateRequest;
import web.club_community.post.clubVideo.dto.VideoDashboardResponse;
import web.club_community.post.clubVideo.dto.VideoPageResponse;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/video")
public class ClubVideoController {
    private final ClubVideoService clubVideoService;

    // 메인페이지 동아리 영상 slice
    @GetMapping("/main/{pageSize}")
    public ResponseEntity<List<VideoDashboardResponse>> dashboardVideo(HttpServletRequest request,
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
        Slice<ClubVideo> videos = clubVideoService.findVideosMainPage(pageable);
        List<VideoDashboardResponse> data = videos.getContent()
                .stream()
                .map(VideoDashboardResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(data);
    }

    // 동아리 영상 게시판 목록 조회
    @GetMapping("/list/{pageNum}/{pageSize}")
    public ResponseEntity<VideoPageResponse> videoListAll(HttpServletRequest request,
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
        Page<ClubVideo> posts = clubVideoService.findVideosPageable(pageable);
        List<VideoDashboardResponse> data = posts.getContent()
                .stream()
                .map(VideoDashboardResponse::of)
                .collect(Collectors.toList());
        VideoPageResponse result = VideoPageResponse.of(data, posts.getTotalPages());
        return ResponseEntity.ok().body(result);
    }

    // 동아리 영상 상세 정보 조회
    @GetMapping("/detail/{videoId}")
    public ResponseEntity<VideoDashboardResponse> noticeDetail(HttpServletRequest request,
                                                               @PathVariable Integer videoId) throws AuthenticationException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        ClubVideo post = clubVideoService.findById(videoId);
        VideoDashboardResponse data = VideoDashboardResponse.of(post);
        return ResponseEntity.ok().body(data);
    }

    // 동아리 영상 등록
    @PostMapping("/create")
    public ResponseEntity<VideoDashboardResponse> createVideo(HttpServletRequest request,
                                                              @Validated @RequestBody VideoCreateRequest videoCreateRequest,
                                                              BindingResult bindingResult) throws AuthenticationException {
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
        ClubVideo video = clubVideoService.createVideo(videoCreateRequest, loginMember);
        VideoDashboardResponse data = VideoDashboardResponse.of(video);
        return ResponseEntity.ok().body(data);
    }
}
