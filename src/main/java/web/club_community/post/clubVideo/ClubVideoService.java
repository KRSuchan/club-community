package web.club_community.post.clubVideo;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.club_community.club.ClubRepository;
import web.club_community.domain.Club;
import web.club_community.domain.ClubVideo;
import web.club_community.domain.Member;
import web.club_community.post.clubVideo.dto.VideoCreateRequest;

import java.util.NoSuchElementException;

@Service
@Transactional
@AllArgsConstructor
public class ClubVideoService {
    private final ClubVideoRepository clubVideoRepository;
    private final ClubRepository clubRepository;

    public Slice<ClubVideo> findVideosMainPage(Pageable pageable) {
        return clubVideoRepository.findAllSliceOrderByCreatedTime(pageable);
    }

    public Page<ClubVideo> findVideosPageable(Pageable pageable) {
        return clubVideoRepository.findAllPageOrderByCreatedTime(pageable);
    }

    public ClubVideo createVideo(VideoCreateRequest form, Member writer) {
        Club club = clubRepository.findByMaster(writer)
                .orElseThrow(() -> new NoSuchElementException("동아리장인 동아리가 조회되지 않았습니다."));
        ClubVideo recruitMemberPost = ClubVideo.builder()
                .title(form.getTitle())
                .url(form.getUrl())
                .build();
        return clubVideoRepository.save(recruitMemberPost);
    }

    public ClubVideo findById(Integer id) {
        return clubVideoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("조회되는 영상 게시글이 없습니다."));
    }
}
