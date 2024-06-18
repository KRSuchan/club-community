package web.club_community.post.recruit_post;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import web.club_community.club.ClubRepository;
import web.club_community.domain.Club;
import web.club_community.domain.Member;
import web.club_community.domain.RecruitMemberPost;
import web.club_community.file.FileProperty;
import web.club_community.file.FilePropertyUtil;
import web.club_community.file.FileRepository;
import web.club_community.post.recruit_post.dto.PostCreateRequest;

import java.util.NoSuchElementException;

@Service
@Transactional
@AllArgsConstructor
public class RecruitMemberPostService {
    private final ClubRepository clubRepository;
    private final FilePropertyUtil filePropertyUtil;
    private final FileRepository fileRepository;
    private RecruitMemberPostRepository recruitMemberPostRepository;

    public Slice<RecruitMemberPost> findPostsMainPage(Pageable pageable) {
        return recruitMemberPostRepository.findAllSliceOrderByCreatedTime(pageable);
    }

    public Page<RecruitMemberPost> findPostsPageable(Pageable pageable) {
        return recruitMemberPostRepository.findAllPageOrderByCreatedTime(pageable);
    }

    public RecruitMemberPost findById(Integer id) {
        return recruitMemberPostRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("게시글이 조회되지 않았습니다."));
    }

    public RecruitMemberPost createPost(PostCreateRequest form, Member writer, MultipartFile photoFile) {
        FileProperty fileProperty = filePropertyUtil.createFileProperty(photoFile);
        Club club = clubRepository.findByMaster(writer)
                .orElseThrow(() -> new NoSuchElementException("동아리장인 동아리가 조회되지 않았습니다."));
        RecruitMemberPost recruitMemberPost = RecruitMemberPost.builder()
                .title(form.getTitle())
                .contents(form.getContent())
                .club(club)
                .fileName(fileProperty.getUploadFileName())
                .filePath(fileProperty.getFilePath())
                .build();
        // 파일 저장
        fileRepository.save(fileProperty, photoFile);
        return recruitMemberPostRepository.save(recruitMemberPost);
    }
}
