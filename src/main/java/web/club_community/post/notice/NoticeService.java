package web.club_community.post.notice;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import web.club_community.club.ClubRepository;
import web.club_community.club.member.ClubMemberRepository;
import web.club_community.domain.Club;
import web.club_community.domain.Member;
import web.club_community.domain.notice.ClubNotice;
import web.club_community.domain.notice.Notice;
import web.club_community.domain.notice.NoticeType;
import web.club_community.domain.notice.PublicNotice;
import web.club_community.file.FileProperty;
import web.club_community.file.FilePropertyUtil;
import web.club_community.file.FileRepository;
import web.club_community.post.notice.dto.NoticeCreateRequest;

import javax.naming.AuthenticationException;
import java.util.NoSuchElementException;

@Service
@Transactional
@AllArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final ClubNoticeRepository clubNoticeRepository;
    private final PublicNoticeRepository publicNoticeRepository;
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final FilePropertyUtil filePropertyUtil;
    private final FileRepository fileRepository;

    public Slice<Notice> findNoticesMainPage(Pageable pageable) {
        return noticeRepository.findAllSliceOrderByCreatedTime(pageable);
    }

    public Page<Notice> findNoticesPageable(Pageable pageable) {
        return noticeRepository.findAllPageOrderByCreatedTime(pageable);
    }

    public Page<PublicNotice> findNoticesPageableEntire(Pageable pageable) {
        return publicNoticeRepository.findAllPageOrderByCreatedTime(pageable);
    }

    public Notice findById(Integer noticeId) {
        return noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoSuchElementException("공지가 조회되지 않았습니다."));
    }

    public Notice findByIdAndMember(Integer noticeId, Member member) throws AuthenticationException {
        Notice notice = findById(noticeId);
        if (notice.getNoticeType() == NoticeType.CLUB && !member.getRoles().contains("admin")) {
            ClubNotice clubNotice = (ClubNotice) notice;
            Club club = clubNotice.getClub();
            clubMemberRepository.findByClubAndMember(club, member)
                    .orElseThrow(() -> new AuthenticationException("동아리에 속한 회원만 조회 가능합니다."));
        }
        return notice;
    }

    public Notice createNotice(NoticeCreateRequest notice, Member member, MultipartFile photoFile) throws AuthenticationException {
        FileProperty fileProperty = filePropertyUtil.createFileProperty(photoFile);
        if (member.getRoles().contains("admin")) {
            PublicNotice publicNotice = PublicNotice.builder()
                    .title(notice.getTitle())
                    .contents(notice.getContent())
                    .fileName(fileProperty.getUploadFileName())
                    .filePath(fileProperty.getFilePath())
                    .noticeType(NoticeType.ENTIRE).build();
            // 파일 저장
            fileRepository.save(fileProperty, photoFile);
            return publicNoticeRepository.save(publicNotice);
        } else {
            Club club = clubRepository.findByMaster(member)
                    .orElseThrow(() -> new NoSuchElementException("회원이 동아리장인 동아리가 없습니다."));
            ClubNotice clubNotice = ClubNotice.builder()
                    .title(notice.getTitle())
                    .contents(notice.getContent())
                    .noticeType(NoticeType.ENTIRE)
                    .fileName(fileProperty.getUploadFileName())
                    .filePath(fileProperty.getFilePath())
                    .club(club)
                    .build();
            // 파일 저장
            fileRepository.save(fileProperty, photoFile);
            return clubNoticeRepository.save(clubNotice);
        }
    }
}
