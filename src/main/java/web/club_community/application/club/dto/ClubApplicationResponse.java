package web.club_community.application.club.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import web.club_community.domain.ApplyStatus;
import web.club_community.domain.ClubApplication;

import java.time.LocalDateTime;

@Builder
@Data
public class ClubApplicationResponse {
    private Integer clubId;
    private String clubName;
    private ApplyStatus applyStatus;
    private String reason;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    public static ClubApplicationResponse of(ClubApplication clubApplication) {
        return ClubApplicationResponse.builder()
                .clubId(clubApplication.getId())
                .clubName(clubApplication.getName())
                .applyStatus(clubApplication.getStatus())
                .reason(clubApplication.getRejectReason())
                .updateTime(LocalDateTime.now())
                .build();
    }
}
