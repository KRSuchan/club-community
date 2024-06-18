package web.club_community.post.notice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.club_community.domain.notice.Notice;
import web.club_community.domain.notice.NoticeType;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    @Query("select n from Notice n order by n.createdTime DESC ")
    Slice<Notice> findAllSliceOrderByCreatedTime(Pageable pageable);

    @Query("select n from Notice n order by n.createdTime DESC ")
    Page<Notice> findAllPageOrderByCreatedTime(Pageable pageable);

    @Query("select n from Notice n where n.noticeType = :type order by n.createdTime DESC ")
    Page<Notice> findAllPageOrderByCreatedTimeTypeEntire(Pageable pageable, NoticeType type);

}
