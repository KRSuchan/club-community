package web.club_community.post.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import web.club_community.domain.notice.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {
}
