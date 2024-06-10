package web.club_community.post.application.clubMember;

import org.springframework.data.jpa.repository.JpaRepository;
import web.club_community.domain.ClubMemberApplication;

public interface ClubMemberApplicationRepository extends JpaRepository<ClubMemberApplication, Integer> {
}
