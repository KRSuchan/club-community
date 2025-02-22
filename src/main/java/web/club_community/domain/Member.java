package web.club_community.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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
    private LocalDate birth;
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

    @OneToOne(mappedBy = "master", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Club club;
    @OneToOne(mappedBy = "subMaster", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Club clubAsSubMaster;
    @OneToOne(mappedBy = "affair", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Club clubAsAffair;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ClubMember> clubMembers = new ArrayList<>();

}
