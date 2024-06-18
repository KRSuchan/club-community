package web.club_community.post.notice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import web.club_community.domain.notice.Notice;
import web.club_community.domain.notice.NoticeType;

import java.time.LocalDateTime;

@Data
@Builder
public class NoticeDashboardResponse {
    private Integer noticeId;
    private String noticeTitle;
    private NoticeType noticeType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    public static NoticeDashboardResponse of(Notice notice) {
        return NoticeDashboardResponse.builder()
                .noticeId(notice.getId())
                .noticeTitle(notice.getTitle())
                .noticeType(notice.getNoticeType())
                .createdTime(notice.getCreatedTime())
                .build();
    }
}
