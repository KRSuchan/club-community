package web.club_community.post.notice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.club_community.domain.notice.PublicNotice;

public interface PublicNoticeRepository extends JpaRepository<PublicNotice, Integer> {
    @Query("select n from PublicNotice n order by n.createdTime DESC ")
    Page<PublicNotice> findAllPageOrderByCreatedTime(Pageable pageable);
}
