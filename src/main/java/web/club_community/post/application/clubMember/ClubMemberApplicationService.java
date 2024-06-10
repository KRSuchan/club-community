package web.club_community.post.application.clubMember;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class ClubMemberApplicationService {
    private final ClubMemberApplicationRepository applicationRepository;

    @Autowired
    public ClubMemberApplicationService(ClubMemberApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }
}
