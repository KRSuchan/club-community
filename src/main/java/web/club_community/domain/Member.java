package web.club_community.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    /**
     * EMAIL : 이메일
     * PASSWORD : 비밀번호
     * NAME : 이름
     * BIRTH : 생년월일
     * GENDER : 성별
     * DEPARTMENT : 학과
     * CODE : 학번
     * PHONE_NUMBER : 전화번호
     */
    @Id
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "BIRTHDAY", nullable = false)
    private LocalDate birthday;
    @Column(name = "GENDER", nullable = false)
    private Gender gender;
    @Column(name = "DEPARTMENT", nullable = false)
    private String department;
    @Column(name = "CODE", nullable = false, unique = true)
    private Integer code;
    @Column(name = "PHONE_NUMBER", nullable = false)
    private String phoneNumber;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @OneToOne(mappedBy = "master", cascade = CascadeType.ALL)
    private Club club;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<ClubMember> clubMembers = new ArrayList<>();

}
