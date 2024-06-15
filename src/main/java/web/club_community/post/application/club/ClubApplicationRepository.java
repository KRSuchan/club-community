package web.club_community.post.application.club;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.club_community.domain.ClubApplication;

import java.util.List;

public interface ClubApplicationRepository extends JpaRepository<ClubApplication, Integer> {
    @Query("SELECT c FROM ClubApplication c ORDER BY c.createTime")
    List<ClubApplication> findAllOrderByCreateTime();

}
