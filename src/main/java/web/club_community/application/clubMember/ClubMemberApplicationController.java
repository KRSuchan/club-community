package web.club_community.application.clubMember;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController("/api/application/club-member")
public class ClubMemberApplicationController {
    private final ClubMemberApplicationService clubMemberApplicationService;
}
