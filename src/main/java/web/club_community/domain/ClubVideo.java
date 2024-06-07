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
public class ClubVideo {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "TITLE", nullable = false)
    private String title;
    
    @Column(name = "CREATE_TIME", nullable = false)
    private LocalDateTime createTime;
    @Column(name = "UPDATE_TIME", nullable = false)
    private LocalDateTime updateTime;

    @Column(name = "URL", nullable = false)
    private String url;
}
