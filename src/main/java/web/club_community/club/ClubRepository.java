package web.club_community.club;

import org.springframework.data.jpa.repository.JpaRepository;
import web.club_community.domain.Club;
import web.club_community.domain.Member;

import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Integer> {
    Optional<Club> findByMaster(Member master);
}
