package web.club_community.club.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class ClubMemberService {
    private final ClubMemberRepository clubMemberRepository;

    @Autowired
    public ClubMemberService(ClubMemberRepository clubMemberRepository) {
        this.clubMemberRepository = clubMemberRepository;
    }
}
