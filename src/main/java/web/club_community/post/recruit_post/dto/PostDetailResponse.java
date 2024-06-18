package web.club_community.post.recruit_post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import web.club_community.domain.RecruitMemberPost;

import java.time.LocalDateTime;

@Builder
@Data
public class PostDetailResponse {
    private Integer postId;
    private String postTitle;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    private String contents;
    private String fileName;
    private String filePath;

    public static PostDetailResponse of(RecruitMemberPost post) {
        return PostDetailResponse.builder()
                .postId(post.getId())
                .postTitle(post.getTitle())
                .createdTime(post.getCreateTime())
                .contents(post.getContents())
                .fileName(post.getFileName())
                .filePath(post.getFilePath())
                .build();
    }
}
