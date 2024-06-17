package web.club_community.club.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import web.club_community.domain.Club;
import web.club_community.domain.ClubMember;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Integer> {
    @Modifying
    void deleteByClubAndId(Club club, Integer id);
}
