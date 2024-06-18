package web.club_community.post.clubVideo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.club_community.domain.ClubVideo;

public interface ClubVideoRepository extends JpaRepository<ClubVideo, Integer> {
    @Query("select n from ClubVideo n order by n.createdDate DESC")
    Slice<ClubVideo> findAllSliceOrderByCreatedTime(Pageable pageable);

    @Query("select n from ClubVideo n order by n.createdDate DESC ")
    Page<ClubVideo> findAllPageOrderByCreatedTime(Pageable pageable);
}
