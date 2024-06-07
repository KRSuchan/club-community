package web.club_community.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "INTRODUCTION", nullable = false)
    private String introduction;
    @Column(name = "HISTORY", nullable = false)
    private String history;
    @Column(name = "PROFESSOR_NAME", nullable = false)
    private String professorName;
    @Column(name = "PROFESSOR_DEPARTMENT", nullable = false)
    private String professorDepartment;
    @Column(name = "PROFESSOR_PHONENUMBER", nullable = false)
    private String professorPhoneNumber;
}
