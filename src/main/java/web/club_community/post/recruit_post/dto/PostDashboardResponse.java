package web.club_community.post.recruit_post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import web.club_community.domain.RecruitMemberPost;

import java.time.LocalDateTime;

@Data
@Builder
public class PostDashboardResponse {
    private Integer postId;
    private String postTitle;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    public static PostDashboardResponse of(RecruitMemberPost post) {
        return PostDashboardResponse.builder()
                .postId(post.getId())
                .postTitle(post.getTitle())
                .createdTime(post.getCreateTime())
                .build();
    }
}
