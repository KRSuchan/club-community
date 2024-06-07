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
public class ClubApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "NAME", nullable = false)
    private String name;
    @OneToOne
    private Member applier;
    @Column(name = "CREATE_TIME", nullable = false)
    private LocalDateTime createTime;
    @Column(name = "PROFESSOR_NAME", nullable = false)
    private String professorName;
    @Column(name = "PROFESSOR_DEPARTMENT", nullable = false)
    private String professorDepartment;
    @Column(name = "PROFESSOR_PHONENUMBER", nullable = false)
    private String professorPhoneNumber;
    @Column(name = "STATUS", nullable = false)
    private ApplyStatus status;
    @Column(name = "REJECT_REASON")
    private String rejectReason;
}
