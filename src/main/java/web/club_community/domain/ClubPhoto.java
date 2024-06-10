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
public class ClubPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "CREATE_TIME", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();
    @Column(name = "UPDATE_TIME", nullable = false)
    private LocalDateTime updateTime = LocalDateTime.now();

    // 파일 관리
    @Column(name = "FILE_PATH")
    private String filePath;
    @Column(name = "FILE_NAME")
    private String fileName;
}
