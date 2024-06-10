package web.club_community.club;

import org.springframework.data.jpa.repository.JpaRepository;
import web.club_community.domain.Club;

public interface ClubRepository extends JpaRepository<Club, Integer> {
}
