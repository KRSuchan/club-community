package web.club_community.application.club.dto;

import lombok.Builder;
import lombok.Data;
import web.club_community.domain.ClubApplication;

@Builder
@Data
public class ClubApplicationCreateResponse {
    private Integer id;
    private String clubName;

    public static ClubApplicationCreateResponse of(ClubApplication clubApplication) {
        return ClubApplicationCreateResponse.builder()
                .clubName(clubApplication.getName())
                .id(clubApplication.getId())
                .build();
    }
}
