package web.club_community.club;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController("/api/club")
public class ClubController {
    private final ClubService clubService;
}
