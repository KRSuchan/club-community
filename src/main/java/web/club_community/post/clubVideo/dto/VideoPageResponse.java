package web.club_community.post.clubVideo.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class VideoPageResponse {
    private List<VideoDashboardResponse> data;
    private Integer totalPages;

    public static VideoPageResponse of(List<VideoDashboardResponse> data, Integer totalPages) {
        return VideoPageResponse.builder()
                .data(data)
                .totalPages(totalPages)
                .build();
    }
}
