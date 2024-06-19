package web.club_community.application.club;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import web.club_community.Member.MemberRepository;
import web.club_community.Member.MemberService;
import web.club_community.application.club.dto.ClubApplicationCreateForm;
import web.club_community.application.club.dto.RejectApplicationRequest;
import web.club_community.club.ClubRepository;
import web.club_community.club.member.ClubMemberRepository;
import web.club_community.domain.*;
import web.club_community.exception.runtime.AlreadyProcessedException;
import web.club_community.file.FileProperty;
import web.club_community.file.FilePropertyUtil;
import web.club_community.file.FileRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ClubApplicationService {
    private final ClubApplicationRepository clubApplicationRepository;
    private final FilePropertyUtil filePropertyUtil;
    private final FileRepository fileRepository;
    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final MemberService memberService;

    public ClubApplication save(ClubApplicationCreateForm form, Member applier, MultipartFile clubPicture) {
        FileProperty fileProperty = filePropertyUtil.createFileProperty(clubPicture);
        ClubApplication clubApplication = ClubApplication.builder()
                .name(form.getName())
                .applier(applier)
                .status(ApplyStatus.PENDING)
                .fileName(fileProperty.getUploadFileName())
                .filePath(fileProperty.getFilePath())
                .professor(form.getProfessor())
                .clubType(form.getClubType())
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

    private ClubApplication findById(Integer id) {
        return clubApplicationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("조회된 신청서가 없습니다."));
    }

    private ClubApplication checkValidatedClubApplication(Integer applicationId) {
        ClubApplication clubApplication = findById(applicationId);
        if (clubApplication.getStatus() != ApplyStatus.PENDING)
            throw new AlreadyProcessedException("이미 처리된 요청입니다.");
        return clubApplication;
    }

    public ClubApplication approveClubApplication(Integer applicationId) {
        ClubApplication clubApplication = checkValidatedClubApplication(applicationId);
        Member newMaster = clubApplication.getApplier();
        clubApplication.setStatus(ApplyStatus.ACCEPT);
        // 동아리 생성
        Club newClub = Club.builder()
                .name(clubApplication.getName())
                .clubType(clubApplication.getClubType())
                .filePath(clubApplication.getFilePath())
                .fileName(clubApplication.getFileName())
                .professor(clubApplication.getProfessor())
                .master(newMaster)
                .build();
        // 동아리장 회원 추가를 위한 생성한 동아리 우선 등록
        newClub = clubRepository.save(newClub);
//        동아리장을 동아리 부원에 넣는 로직 삭제
        // 동아리장 추가
        ClubMember clubMember = ClubMember.builder().club(newClub).member(newMaster).build();
        clubMemberRepository.save(clubMember);
        // 동아리장인 권한 추가
        newMaster.getRoles().add("master");
        memberRepository.save(newMaster);
        // 수정된 최종 지원서 반환
        return clubApplicationRepository.save(clubApplication);
    }

    public ClubApplication rejectClubApplication(Integer applicationId, RejectApplicationRequest request) {
        ClubApplication clubApplication = checkValidatedClubApplication(applicationId);
        clubApplication.setStatus(ApplyStatus.REJECT);
        clubApplication.setRejectReason(request.getReason());
        return clubApplicationRepository.save(clubApplication);
    }

    public List<ClubApplication> findMyClubApplication(Member member) {
        List<ClubApplication> myApplications = clubApplicationRepository.findClubApplicationsByApplier(member);
        return myApplications;
    }
}
