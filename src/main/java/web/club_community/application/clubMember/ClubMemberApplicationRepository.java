package web.club_community.application.clubMember;

import org.springframework.data.jpa.repository.JpaRepository;
import web.club_community.domain.Club;
import web.club_community.domain.ClubMemberApplication;

import java.util.List;

public interface ClubMemberApplicationRepository extends JpaRepository<ClubMemberApplication, Integer> {
    List<ClubMemberApplication> findByClubOrderByCreatedDateDesc(Club club);
}
