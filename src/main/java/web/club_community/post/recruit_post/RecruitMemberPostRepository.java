package web.club_community.post.recruit_post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.club_community.domain.RecruitMemberPost;

public interface RecruitMemberPostRepository extends JpaRepository<RecruitMemberPost, Integer> {
    @Query("select n from RecruitMemberPost n order by n.createTime DESC")
    Slice<RecruitMemberPost> findAllSliceOrderByCreatedTime(Pageable pageable);

    @Query("select n from RecruitMemberPost n order by n.createTime DESC ")
    Page<RecruitMemberPost> findAllPageOrderByCreatedTime(Pageable pageable);
}
