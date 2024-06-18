package web.club_community.post.clubVideo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import web.club_community.domain.ClubVideo;

import java.time.LocalDateTime;

@Data
@Builder
public class VideoDashboardResponse {
    private Integer videoId;
    private String videoTitle;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    private String videoUrl;

    public static VideoDashboardResponse of(ClubVideo clubVideo) {
        return VideoDashboardResponse.builder()
                .videoId(clubVideo.getId())
                .videoTitle(clubVideo.getTitle())
                .createdTime(clubVideo.getCreatedDate())
                .videoUrl(clubVideo.getUrl()).
                build();
    }
}
