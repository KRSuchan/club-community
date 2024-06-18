package web.club_community.post.recruit_post;

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
import web.club_community.domain.RecruitMemberPost;
import web.club_community.exception.runtime.SessionNotFoundException;
import web.club_community.post.recruit_post.dto.PostCreateRequest;
import web.club_community.post.recruit_post.dto.PostDashboardResponse;
import web.club_community.post.recruit_post.dto.PostDetailResponse;
import web.club_community.post.recruit_post.dto.PostPageResponse;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/recruit")
public class RecruitMemberPostController {
    private final RecruitMemberPostService recruitMemberPostService;

    // 메인페이지 부원 모집 slice
    @GetMapping("/main/{pageSize}")
    public ResponseEntity<List<PostDashboardResponse>> dashboardPost(HttpServletRequest request,
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
        Slice<RecruitMemberPost> notices = recruitMemberPostService.findPostsMainPage(pageable);
        List<PostDashboardResponse> data = notices.getContent()
                .stream()
                .map(PostDashboardResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(data);
    }

    // 부원 모집 게시판 목록 조회
    @GetMapping("/list/{pageNum}/{pageSize}")
    public ResponseEntity<PostPageResponse> postListAll(HttpServletRequest request,
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
        Page<RecruitMemberPost> posts = recruitMemberPostService.findPostsPageable(pageable);
        List<PostDashboardResponse> data = posts.getContent()
                .stream()
                .map(PostDashboardResponse::of)
                .collect(Collectors.toList());
        PostPageResponse result = PostPageResponse.of(data, posts.getTotalPages());
        return ResponseEntity.ok().body(result);
    }

    // 부원 모집 게시판 상세 정보 조회
    @GetMapping("/detail/{postId}")
    public ResponseEntity<PostDetailResponse> noticeDetail(HttpServletRequest request,
                                                           @PathVariable Integer postId) throws AuthenticationException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        RecruitMemberPost post = recruitMemberPostService.findById(postId);
        PostDetailResponse data = PostDetailResponse.of(post);
        return ResponseEntity.ok().body(data);
    }

    // 게시글 등록
    @PostMapping("/create")
    public ResponseEntity<PostDetailResponse> createNotice(HttpServletRequest request,
                                                           @Validated @RequestPart("data") PostCreateRequest postCreateRequest,
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
        RecruitMemberPost post = recruitMemberPostService.createPost(postCreateRequest, loginMember, photo);
        PostDetailResponse data = PostDetailResponse.of(post);
        return ResponseEntity.ok().body(data);
    }
}
