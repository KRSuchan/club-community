package web.club_community.application.clubMember.dto;

import lombok.Builder;
import lombok.Data;
import web.club_community.domain.ApplyStatus;
import web.club_community.domain.ClubMemberApplication;

@Builder
@Data
public class ApplicationResultResponse {
    private Integer applicationId;
    private ApplyStatus applyStatus;

    public static ApplicationResultResponse of(ClubMemberApplication application) {
        return ApplicationResultResponse.builder()
                .applicationId(application.getId())
                .applyStatus(application.getStatus())
                .build();
    }
}
