package web.club_community.post.application.club;

import org.springframework.data.jpa.repository.JpaRepository;
import web.club_community.domain.ClubApplication;

public interface ClubApplicationRepository extends JpaRepository<ClubApplication, Integer> {
}
