package web.club_community.post.notice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class NoticePageResponse {
    private List<NoticeDashboardResponse> data;
    private Integer totalPages;

    public static NoticePageResponse of(List<NoticeDashboardResponse> data, Integer totalPages) {
        return NoticePageResponse.builder()
                .data(data)
                .totalPages(totalPages)
                .build();
    }
}
