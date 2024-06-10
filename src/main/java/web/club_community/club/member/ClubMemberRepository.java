package web.club_community.club.member;

import org.springframework.data.jpa.repository.JpaRepository;
import web.club_community.domain.ClubMember;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Integer> {
}
