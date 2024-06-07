package web.club_community.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
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
    @Column(name = "INTRODUCTION", nullable = false)
    private String introduction;
    @Column(name = "HISTORY", nullable = false)
    private String history;
    @Column(name = "MEETING_TIME")
    private String meetingTime;
    @Column(name = "CLUB_TYPE")
    private ClubType clubType;

    // 대표 사진
    @Column(name = "FILE_PATH")
    private String filePath;
    @Column(name = "FILE_NAME")
    private String fileName;

    // 마스터 관리자
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Master", nullable = false)
    private Member master;

    // 동아리 부원
    @OneToMany(mappedBy = "clubMember", cascade = CascadeType.ALL)
    private List<ClubMember> clubMembers = new ArrayList<>();
}
