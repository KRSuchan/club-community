package web.club_community.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ClubMemberApplication {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "STATUS", nullable = false)
    private ApplyStatus status;
    @Column(name = "CREATE_TIME", nullable = false)
    private LocalDateTime createTime;
    // 가입 신청서 업로드
    @Column(name = "FILE_PATH")
    private String filePath;
    @Column(name = "FILE_NAME")
    private String fileName;
}
