package web.club_community.club.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController("/api/club-member")
public class ClubMemberController {
    private final ClubMemberService clubMemberService;
}
