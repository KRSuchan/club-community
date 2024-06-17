package web.club_community.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ClubApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "NAME", nullable = false)
    private String name;
    @OneToOne
    private Member applier;
    @Column(name = "CLUB_TYPE", nullable = false)
    private ClubType clubType;

    @Builder.Default
    @Column(name = "CREATE_TIME", nullable = false)
    private LocalDateTime createTime = LocalDateTime.now();

    // 대표 사진
    @Column(name = "FILE_PATH")
    private String filePath;
    @Column(name = "FILE_NAME")
    private String fileName;

    @Embedded
    private Professor professor;

    @Column(name = "STATUS", nullable = false)
    private ApplyStatus status;
    @Column(name = "REJECT_REASON")
    private String rejectReason;
}
