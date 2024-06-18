package web.club_community.post.clubPhoto.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import web.club_community.domain.ClubPhoto;

import java.time.LocalDateTime;

@Data
@Builder
public class PhotoDashboardResponse {
    private Integer photoId;
    private String photoTitle;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    private String photoPath;

    public static PhotoDashboardResponse of(ClubPhoto clubPhoto) {
        return PhotoDashboardResponse.builder()
                .photoId(clubPhoto.getId())
                .photoTitle(clubPhoto.getTitle())
                .photoPath(clubPhoto.getFilePath())
                .createdTime(clubPhoto.getCreatedDate())
                .build();
    }
}