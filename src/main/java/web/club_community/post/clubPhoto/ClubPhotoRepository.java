package web.club_community.post.clubPhoto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.club_community.domain.ClubPhoto;

public interface ClubPhotoRepository extends JpaRepository<ClubPhoto, Integer> {
    @Query("select n from ClubPhoto n order by n.createdDate DESC")
    Slice<ClubPhoto> findAllSliceOrderByCreatedTime(Pageable pageable);

    @Query("select n from ClubPhoto n order by n.createdDate DESC ")
    Page<ClubPhoto> findAllPageOrderByCreatedTime(Pageable pageable);
}
