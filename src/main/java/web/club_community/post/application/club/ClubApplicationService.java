package web.club_community.post.application.club;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class ClubApplicationService {
    private final ClubApplicationRepository clubApplicationRepository;

    @Autowired
    public ClubApplicationService(ClubApplicationRepository clubApplicationRepository) {
        this.clubApplicationRepository = clubApplicationRepository;
    }
}
