package web.club_community.club.member;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.club_community.Member.MemberRepository;
import web.club_community.club.ClubRepository;
import web.club_community.domain.Club;
import web.club_community.domain.ClubMember;
import web.club_community.domain.Member;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class ClubMemberService {
    private final ClubMemberRepository clubMemberRepository;
    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;

    public Club findByMaster(Member master) {
        return clubRepository.findByMaster(master)
                .orElseThrow(() -> new NoSuchElementException("마스터 관리자인 동아리가 없습니다."));
    }

    public List<ClubMember> findClubMembersByMaster(Member master) {
        return findByMaster(master).getClubMembers();
    }

    public void deleteClubMember(Member master, Integer clubMemberId) {
        Club club = findByMaster(master);
        ClubMember clubMember = clubMemberRepository.findById(clubMemberId)
                .orElseThrow(() -> new NoSuchElementException("조회된 회원이 없습니다."));
        Member member = clubMember.getMember();
        member.getRoles().remove("clubMember");
        memberRepository.save(member);
        clubMemberRepository.deleteByClubAndId(club, clubMemberId);
    }
}
