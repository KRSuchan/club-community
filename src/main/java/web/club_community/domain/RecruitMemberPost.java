package web.club_community.domain;

import jakarta.persistence.*;
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
public class RecruitMemberPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "CREATE_TIME", nullable = false)
    private LocalDateTime createTime;
    @Column(name = "UPDATE_TIME", nullable = false)
    private LocalDateTime updateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MANAGER_USER_ID", nullable = false)
    private Club club;

    // 파일 첨부 관리
    @Column(name = "FILE_PATH")
    private String filePath;
    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "TITLE", nullable = false)
    private String title;
    @Column(name = "CONTENTS", nullable = false)
    private String contents;
}
