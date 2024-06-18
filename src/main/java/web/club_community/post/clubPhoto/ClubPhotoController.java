package web.club_community.post.clubPhoto;

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
import web.club_community.domain.ClubPhoto;
import web.club_community.domain.Member;
import web.club_community.exception.runtime.SessionNotFoundException;
import web.club_community.post.clubPhoto.dto.PhotoCreateRequest;
import web.club_community.post.clubPhoto.dto.PhotoDashboardResponse;
import web.club_community.post.clubPhoto.dto.PhotoPageResponse;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/photo")
public class ClubPhotoController {
    private final ClubPhotoService clubPhotoService;

    // 메인페이지 동아리 사진 slice
    @GetMapping("/main/{pageSize}")
    public ResponseEntity<List<PhotoDashboardResponse>> dashboardPhoto(HttpServletRequest request,
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
        Slice<ClubPhoto> videos = clubPhotoService.findPhotosMainPage(pageable);
        List<PhotoDashboardResponse> data = videos.getContent()
                .stream()
                .map(PhotoDashboardResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(data);
    }

    // 동아리 사진 게시판 목록 조회
    @GetMapping("/list/{pageNum}/{pageSize}")
    public ResponseEntity<PhotoPageResponse> videoListAll(HttpServletRequest request,
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
        Page<ClubPhoto> posts = clubPhotoService.findPhotosPageable(pageable);
        List<PhotoDashboardResponse> data = posts.getContent()
                .stream()
                .map(PhotoDashboardResponse::of)
                .collect(Collectors.toList());
        PhotoPageResponse result = PhotoPageResponse.of(data, posts.getTotalPages());
        return ResponseEntity.ok().body(result);
    }

    // 동아리 사진 상세 정보 조회
    @GetMapping("/detail/{photoId}")
    public ResponseEntity<PhotoDashboardResponse> noticeDetail(HttpServletRequest request,
                                                               @PathVariable Integer photoId) throws AuthenticationException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        ClubPhoto post = clubPhotoService.findById(photoId);
        PhotoDashboardResponse data = PhotoDashboardResponse.of(post);
        return ResponseEntity.ok().body(data);
    }

    // 동아리 사진 등록
    @PostMapping("/create")
    public ResponseEntity<PhotoDashboardResponse> createVideo(HttpServletRequest request,
                                                              @Validated
                                                              @RequestPart("data") PhotoCreateRequest photoCreateRequest,
                                                              BindingResult bindingResult,
                                                              @RequestPart(value = "photo") MultipartFile photoFile) throws AuthenticationException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        if (bindingResult.hasErrors() || photoFile == null) {
            throw new IllegalArgumentException("입력 정보가 잘못되었습니다.");
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            throw new SessionNotFoundException("세션이 만료되었습니다.");
        }
        ClubPhoto photo = clubPhotoService.createPhoto(photoCreateRequest, loginMember, photoFile);
        PhotoDashboardResponse data = PhotoDashboardResponse.of(photo);
        return ResponseEntity.ok().body(data);
    }
}
