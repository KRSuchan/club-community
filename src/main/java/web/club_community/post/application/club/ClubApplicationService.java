package web.club_community.post.application.club;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import web.club_community.domain.ApplyStatus;
import web.club_community.domain.ClubApplication;
import web.club_community.domain.Member;
import web.club_community.file.FileProperty;
import web.club_community.file.FilePropertyUtil;
import web.club_community.file.FileRepository;
import web.club_community.post.application.club.dto.ClubApplicationCreateForm;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ClubApplicationService {
    private final ClubApplicationRepository clubApplicationRepository;
    private final FilePropertyUtil filePropertyUtil;
    private final FileRepository fileRepository;

    public ClubApplication save(ClubApplicationCreateForm form, Member applier, MultipartFile clubPicture) {
        FileProperty fileProperty = filePropertyUtil.createFileProperty(clubPicture);
        ClubApplication clubApplication = ClubApplication.builder()
                .name(form.getName())
                .applier(applier)
                .status(ApplyStatus.PENDING)
                .fileName(fileProperty.getUploadFileName())
                .filePath(fileProperty.getFilePath())
                .professor(form.getProfessor())
                .build();
        // 파일 저장
        fileRepository.save(fileProperty, clubPicture);
        // 동아리 지원서 저장
        clubApplication = clubApplicationRepository.save(clubApplication);
        return clubApplication;
    }

    public List<ClubApplication> findAll() {
        List<ClubApplication> findAllOrderByCreateTime = clubApplicationRepository.findAllOrderByCreateTime();
        return findAllOrderByCreateTime;
    }
}
