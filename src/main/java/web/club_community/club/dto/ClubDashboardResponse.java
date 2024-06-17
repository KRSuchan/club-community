package web.club_community.club.dto;

import lombok.Builder;
import lombok.Data;
import web.club_community.domain.Club;

@Builder
@Data
public class ClubDashboardResponse {
    private Integer clubId;
    private String clubName;
    private String introduction;
    private String history;
    private String imgPath;
    private String meetingTime;
    private MasterResponse master;

    public static ClubDashboardResponse of(Club club) {
        return ClubDashboardResponse.builder()
                .clubId(club.getId())
                .clubName(club.getName())
                .introduction(club.getIntroduction())
                .history(club.getHistory())
                .imgPath(club.getFilePath())
                .meetingTime(club.getMeetingTime())
                .master(MasterResponse.of(club.getMaster())).build();
    }
}
