package web.club_community.post.application.club.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import web.club_community.domain.ApplyStatus;
import web.club_community.domain.ClubApplication;
import web.club_community.domain.Professor;

import java.time.LocalDateTime;

@Data
@Builder
public class ClubApplicationListResponse {
    private Integer id;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    private String filePath;
    private ApplyStatus applyStatus;
    private String rejectReason;
    private String applierName;
    private Professor professor;

    public static ClubApplicationListResponse of(ClubApplication application) {
        return ClubApplicationListResponse.builder()
                .id(application.getId())
                .name(application.getName())
                .createdTime(application.getCreateTime())
                .filePath(application.getFilePath())
                .applyStatus(application.getStatus())
                .rejectReason(application.getRejectReason())
                .applierName(application.getApplier().getName())
                .professor(application.getProfessor())
                .build();
    }
}
