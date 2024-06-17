package web.club_community.club;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import web.club_community.club.dto.ClubUpdateForm;
import web.club_community.domain.Club;
import web.club_community.domain.Member;
import web.club_community.file.FileProperty;
import web.club_community.file.FilePropertyUtil;
import web.club_community.file.FileRepository;

import java.util.NoSuchElementException;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    private final FilePropertyUtil filePropertyUtil;
    private final FileRepository fileRepository;

    public Club findByMaster(Member master) {
        return clubRepository.findByMaster(master)
                .orElseThrow(() -> new NoSuchElementException("마스터 관리자인 동아리가 없습니다."));
    }

    public Club updateClub(Member master, ClubUpdateForm form, MultipartFile mainPicture) {
        Club club = findByMaster(master);
        club.setName(form.getClubName());
        club.setIntroduction(form.getIntroduction());
        club.setHistory(form.getHistory());
        club.setMeetingTime(form.getMeetingTime());
        if (mainPicture != null && !mainPicture.isEmpty()) {
            FileProperty fileProperty = filePropertyUtil.createFileProperty(mainPicture);
            club.setFileName(fileProperty.getUploadFileName());
            club.setFilePath(fileProperty.getFilePath());
            fileRepository.save(fileProperty, mainPicture);
        }
        return clubRepository.save(club);
    }
}
