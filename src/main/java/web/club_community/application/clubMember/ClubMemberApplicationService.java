package web.club_community.application.clubMember;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import web.club_community.Member.MemberRepository;
import web.club_community.club.ClubRepository;
import web.club_community.club.member.ClubMemberRepository;
import web.club_community.domain.*;
import web.club_community.file.FileProperty;
import web.club_community.file.FilePropertyUtil;
import web.club_community.file.FileRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class ClubMemberApplicationService {
    private final ClubMemberApplicationRepository applicationRepository;
    private final FilePropertyUtil filePropertyUtil;
    private final ClubRepository clubRepository;
    private final FileRepository fileRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final MemberRepository memberRepository;

    private Club findClubById(Integer clubId) {
        return clubRepository.findById(clubId)
                .orElseThrow(() -> new NoSuchElementException("조회된 동아리가 없습니다."));
    }

    private Club findClubByMaster(Member master) {
        return clubRepository.findByMaster(master)
                .orElseThrow(() -> new NoSuchElementException("마스터 관리자인 동아리가 없습니다."));
    }

    public List<ClubMemberApplication> findClubMemberApplicationByMaster(Member master) {
        return applicationRepository.findByClubOrderByCreatedDateDesc(findClubByMaster(master));
    }

    public ClubMemberApplication save(Integer clubId, Member member, MultipartFile applicationFile) {
        FileProperty fileProperty = filePropertyUtil.createFileProperty(applicationFile);
        Club club = findClubById(clubId);
        fileRepository.save(fileProperty, applicationFile);
        ClubMemberApplication application = ClubMemberApplication.builder()
                .club(club)
                .member(member)
                .status(ApplyStatus.PENDING)
                .fileName(fileProperty.getUploadFileName())
                .filePath(fileProperty.getFilePath())
                .build();
        return applicationRepository.save(application);
    }

    public ClubMemberApplication findById(Integer applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NoSuchElementException("조회된 신청서가 없습니다."));
    }

    public ClubMemberApplication approveApplication(Integer applicationId) {
        ClubMemberApplication application = findById(applicationId);
        Club club = application.getClub();
        Member applier = application.getMember();
        applier.getRoles().add("clubMember");
        ClubMember clubMember = ClubMember.builder().club(club).member(applier).build();
        clubMemberRepository.save(clubMember);
        memberRepository.save(applier);
        application.setStatus(ApplyStatus.ACCEPT);
        return applicationRepository.save(application);
    }

    public ClubMemberApplication rejectApplication(Integer applicationId) {
        ClubMemberApplication application = findById(applicationId);
        application.setStatus(ApplyStatus.REJECT);
        return applicationRepository.save(application);
    }
}
