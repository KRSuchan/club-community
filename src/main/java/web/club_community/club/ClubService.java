package web.club_community.club;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import web.club_community.Member.MemberRepository;
import web.club_community.club.dto.ClubUpdateForm;
import web.club_community.domain.Club;
import web.club_community.domain.Member;
import web.club_community.file.FileProperty;
import web.club_community.file.FilePropertyUtil;
import web.club_community.file.FileRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    private final FilePropertyUtil filePropertyUtil;
    private final FileRepository fileRepository;
    private final MemberRepository memberRepository;

    public Club findByMaster(Member master) {
        return clubRepository.findByMaster(master)
                .orElseThrow(() -> new NoSuchElementException("마스터 관리자인 동아리가 없습니다."));
    }

    public Club updateClub(Member master, ClubUpdateForm form, MultipartFile mainPicture, MultipartFile applicationFile) {
        Club club = findByMaster(master);
        club.setName(form.getClubName());
        club.setIntroduction(form.getIntroduction());
        club.setHistory(form.getHistory());
        club.setMeetingTime(form.getMeetingTime());
        String subMasterEmail = form.getSubMasterEmail();
        String affairEmail = form.getAffairEmail();
        if (subMasterEmail != null) {
            Member subMaster = memberRepository.findById(subMasterEmail)
                    .orElseThrow(() -> new NoSuchElementException("조회된 회원이 없습니다."));
            club.setSubMaster(subMaster);
        }
        if (affairEmail != null) {
            Member affair = memberRepository.findById(affairEmail)
                    .orElseThrow(() -> new NoSuchElementException("조회된 회원이 없습니다."));
            club.setAffair(affair);
        }
        // 사진 파일 수정에 따른 설정
        if (mainPicture != null && !mainPicture.isEmpty()) {
            FileProperty fileProperty = filePropertyUtil.createFileProperty(mainPicture);
            club.setFileName(fileProperty.getUploadFileName());
            club.setFilePath(fileProperty.getFilePath());
            fileRepository.save(fileProperty, mainPicture);
        }
        // 입부 신청서 수정에 따른 설정
        if (applicationFile != null && !applicationFile.isEmpty()) {
            FileProperty fileProperty = filePropertyUtil.createFileProperty(applicationFile);
            club.setApplicationName(fileProperty.getUploadFileName());
            club.setApplicationPath(fileProperty.getFilePath());
            fileRepository.save(fileProperty, applicationFile);
        }
        return clubRepository.save(club);
    }

    public Club findById(Integer clubId) {
        return clubRepository.findById(clubId)
                .orElseThrow(() -> new NoSuchElementException("조회된 동아리가 없습니다."));
    }

    public List<Club> findAll() {
        return clubRepository.findAllOrderByName();
    }
}
