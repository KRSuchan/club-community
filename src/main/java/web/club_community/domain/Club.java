package web.club_community.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "NAME", nullable = false)
    private String name;
    @Builder.Default
    @Column(name = "INTRODUCTION", nullable = false)
    private String introduction = "동아리 소개를 입력하세요.";
    @Builder.Default
    @Column(name = "HISTORY", nullable = false)
    private String history = "동아리 연혁을 입력하세요.";
    @Builder.Default
    @Column(name = "MEETING_TIME", nullable = false)
    private String meetingTime = "정기모임 시간을 입력하세요.";
    @Column(name = "CLUB_TYPE")
    private ClubType clubType;

    @Embedded
    private Professor professor;
    // 대표 사진
    @Column(name = "FILE_PATH")
    private String filePath;
    @Column(name = "FILE_NAME")
    private String fileName;

    // 마스터 관리자
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MASTER", nullable = false)
    private Member master;

    // 동아리 부원
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<ClubMember> clubMembers = new ArrayList<>();

}
