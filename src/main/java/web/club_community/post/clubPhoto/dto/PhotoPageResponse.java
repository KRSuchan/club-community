package web.club_community.post.clubPhoto.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PhotoPageResponse {
    private List<PhotoDashboardResponse> data;
    private Integer totalPages;

    public static PhotoPageResponse of(List<PhotoDashboardResponse> data, Integer totalPages) {
        return PhotoPageResponse.builder()
                .data(data)
                .totalPages(totalPages)
                .build();
    }
}

