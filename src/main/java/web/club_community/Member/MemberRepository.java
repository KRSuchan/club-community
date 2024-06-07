package web.club_community.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import web.club_community.domain.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
}
