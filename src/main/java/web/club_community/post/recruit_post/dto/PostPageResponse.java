package web.club_community.post.recruit_post.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PostPageResponse {
    private List<PostDashboardResponse> data;
    private Integer totalPages;

    public static PostPageResponse of(List<PostDashboardResponse> data, Integer totalPages) {
        return PostPageResponse.builder()
                .data(data)
                .totalPages(totalPages)
                .build();
    }
}
