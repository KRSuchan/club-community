package web.club_community.club.dto;

import lombok.Builder;
import lombok.Data;
import web.club_community.domain.Club;

@Data
@Builder
public class ClubResponse {
    private Integer clubId;
    private String clubName;
    private String clubIntroduction;
    private String clubMasterName;
    private Boolean hasApplicationForm;

    public static ClubResponse of(Club club) {
        return ClubResponse.builder()
                .clubId(club.getId())
                .clubName(club.getName())
                .clubIntroduction(club.getIntroduction())
                .clubMasterName(club.getMaster().getName())
                .hasApplicationForm(club.getApplicationPath() != null)
                .build();
    }
}
