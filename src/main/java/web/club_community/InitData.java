package web.club_community;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import web.club_community.Member.MemberRepository;
import web.club_community.club.ClubRepository;
import web.club_community.club.member.ClubMemberRepository;
import web.club_community.domain.*;
import web.club_community.domain.notice.ClubNotice;
import web.club_community.domain.notice.NoticeType;
import web.club_community.domain.notice.PublicNotice;
import web.club_community.post.notice.NoticeRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitData {
    private final initService initService;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        log.info("데이터 초기화 시작");
        initService.init();
    }

    @RequiredArgsConstructor
    @Component
    @Transactional
    public static class initService {
        private final MemberRepository memberRepository;
        private final ClubMemberRepository clubMemberRepository;
        private final ClubRepository clubRepository;
        private final NoticeRepository noticeRepository;
        private List<Member> masters = new ArrayList<>();
        private List<Member> members = new ArrayList<>();
        private List<Club> centralClubs = new ArrayList<>();
        private List<Club> departmentClubs = new ArrayList<>();

        public void init() {
            // 총 회원 71명 : 시스템 관리자 1명, 클럽장 10명, 클럽원 50명, 일반회원 10명
            initMember();
            // 총 클럽 10개 : 중앙 클럽 5개, 학과 클럽 5개
            initClub();
            initClubMember();
            // 총 공지 110개 : 전체 공지 10개 중앙 클럽 공지 10*5개, 학과 클럽 공지 10*5개
            initNotice();
        }

        private void initNotice() {
            // 전체 공지 10개 생성
            for (int i = 1; i <= 10; i++) {
                initPublicNotice("전체 공지 예시 " + i + "번 제목입니다!!", "전체 공지 예시" + i + "번 내용입니다!!", i);
            }
            // 중앙 공지 10 * 5개 생성
            for (int i = 1; i <= centralClubs.size(); i++) {
                Club clubIdx = centralClubs.get(i - 1);
                for (int j = 0; j < 10; j++) {
                    initDepartmentNotice(clubIdx.getName() + " 공지 예시 " + j + "번 제목입니다!!", clubIdx.getName() + " 공지 예시" + j + "번 내용입니다!!", clubIdx, i + j);
                }
            }
            // 학과 공지 10 * 5개 생성
            for (int i = 1; i <= departmentClubs.size(); i++) {
                Club clubIdx = departmentClubs.get(i - 1);
                for (int j = 0; j < 10; j++) {
                    initDepartmentNotice(clubIdx.getName() + " 공지 예시 " + j + "번 제목입니다!!", clubIdx.getName() + " 공지 예시" + j + "번 내용입니다!!", clubIdx, i + j);
                }
            }
        }

        private void initDepartmentNotice(String title, String contents, Club club, int idx) {
            LocalDateTime now = LocalDateTime.now().minusDays(idx).minusHours(idx);
            ClubNotice notice = ClubNotice.builder()
                    .title(title)
                    .contents(contents)
                    .club(club)
                    .noticeType(NoticeType.CLUB)
                    .createdTime(now)
                    .updatedTime(now)
                    .build();
            noticeRepository.save(notice);
        }

        private void initPublicNotice(String title, String contents, int idx) {
            LocalDateTime now = LocalDateTime.now().minusDays(idx).minusHours(idx);
            PublicNotice notice = PublicNotice.builder()
                    .title(title)
                    .contents(contents)
                    .createdTime(now)
                    .noticeType(NoticeType.ENTIRE)
                    .updatedTime(now)
                    .build();
            noticeRepository.save(notice);
        }

        private void initClubMember() {
            // 10개의 동아리에 대해 11번째 회원부터 5명씩 동아리에 넣기
            // 초반 10명은 동아리에 속하지 않은 일반 회원
            int idx = 10;
            for (int i = 0; i < centralClubs.size(); i++) {
                List<ClubMember> clubMembers = new ArrayList<>();
                for (int j = idx; j < idx + 5; j++) {
                    clubMembers.add(ClubMember.builder()
                            .club(centralClubs.get(i))
                            .member(members.get(j))
                            .roles(List.of("member"))
                            .build());
                }
                clubMemberRepository.saveAll(clubMembers);
                idx += 5; // 다음 동아리의 시작 회원 번호 업데이트
            }
            for (int i = 0; i < departmentClubs.size(); i++) {
                List<ClubMember> clubMembers = new ArrayList<>();
                for (int j = idx; j < idx + 5; j++) {
                    clubMembers.add(ClubMember.builder()
                            .club(departmentClubs.get(i))
                            .member(members.get(j))
                            .roles(List.of("member"))
                            .build());
                }
                clubMemberRepository.saveAll(clubMembers);
                idx += 5; // 다음 동아리의 시작 회원 번호 업데이트
            }
        }


        private void initClub() {
            // 중앙 동아리 생성 5개, 컴소공 동아리 생성 5개
            centralClubs.add(initClub("소리샘",
                    "통기타 활동을 통한 대학문화 창달",
                    "2015년에 창설된 소리샘은 통기타를 통해 대학문화를 활발히 창달하고 있습니다. " +
                            "주 2회, 매주 금요일 오후 7시에 모여서 음악과 문화를 공유하며 활발한 활동을 펼치고 있습니다. " +
                            "회원들 간의 친목도 높이고 있습니다.",
                    "매주 금요일 오후 7시",
                    ClubType.CENTRAL,
                    masters.get(0)));
            centralClubs.add(initClub("거북선신화",
                    "발명을 통한 창업",
                    "2013년에 설립된 거북선신화는 발명을 통한 창업을 지원하고 있으며, " +
                            "매주 금요일 오후 7시에 회의 및 활동을 진행하고 있습니다. " +
                            "회원들은 창의적인 발상과 협력을 통해 다양한 프로젝트를 추진하고 있습니다.",
                    "매주 금요일 오후 7시",
                    ClubType.CENTRAL,
                    masters.get(1)));
            centralClubs.add(initClub("금오테니스",
                    "테니스인의 저변 확대 및 심신 단련",
                    "2010년에 시작된 금오테니스는 테니스 활동을 통해 회원들의 체력과 심신을 단련하고 있습니다. " +
                            "주 2회, 매주 금요일 오후 7시에 회원들은 즐겁고 건강한 운동을 즐기며 친목을 다집니다. " +
                            "함께 성장하고 있는 동아리입니다.",
                    "매주 금요일 오후 7시",
                    ClubType.CENTRAL,
                    masters.get(2)));
            centralClubs.add(initClub("금오영상문화연구회",
                    "영화에 대한 폭넓은 이해와 연구",
                    "2008년에 설립된 금오영상문화연구회는 영화에 대한 폭넓은 연구와 이해를 추구하고 있습니다. " +
                            "주 2회, 매주 금요일 오후 7시에 회원들은 영화를 분석하고 토론하며 서로의 시각을 공유합니다. " +
                            "다양한 영화제와 행사에도 적극적으로 참여하고 있습니다.",
                    "매주 금요일 오후 7시",
                    ClubType.CENTRAL,
                    masters.get(3)));
            centralClubs.add(initClub("with us",
                    "미취학 아동의 인성발달과 자아실현을 위한 교육 봉사",
                    "2016년에 출발한 with us는 미취학 아동의 인성 발달과 자아실현을 위한 교육 봉사를 목표로 활동하고 있습니다. " +
                            "매주 토요일 오전 10시에 모여서 아이들과 함께 다양한 활동을 펼치며 소중한 시간을 보내고 있습니다. " +
                            "동아리 회원들은 아이들과의 소통과 교감을 통해 보람을 느끼고 있습니다.",
                    "매주 토요일 오전 10시",
                    ClubType.CENTRAL,
                    masters.get(4)));
            // 컴소공 동아리 생성
            departmentClubs.add(initClub("셈틀꾼",
                    /* 셈틀꾼 introduce from cse homepage*/
                    "'셈틀꾼'이란 뜻은 순수 한국어로 프로그래머를 뜻합니다. " +
                            "셈틀꾼은 국립금오공과대학교 컴퓨터소프트웨어공학과 학술동아리로서 1990년에 과동아리로 승격되었고 " +
                            "지금 현재까지 학술동아리로서 이어지고 있으며, 실력 있는 여러 선배들을 배출하였고 많은 회원이 활동하고 있습니다. " +
                            "또한 현재는 1-2학년 멘티와 2-4학년 멘토로 구성하여 조를 이루어 멘토멘티 수업을 진행하고 있습니다. ",
                    /* 셈틀꾼 history written by ChatGpt*/
                    "셈틀꾼 학술 동아리는 2010년에 설립되었습니다. " +
                            "처음에는 소수의 컴퓨터 소프트웨어공학과 학생들로 시작하여, " +
                            "알고리즘 스터디와 프로그래밍 대회 준비를 주된 활동으로 삼았습니다. ",
                    "매주 금요일 오후 6시",
                    ClubType.DEPARTMENT,
                    masters.get(5)));
            departmentClubs.add(initClub("ACM",
                    /* ACM introduce from cse homepage*/
                    "ACM은 컴퓨터 관련 학회 중 세계적으로 가장 규모가 크며 영향력 있는 학회로 " +
                            "전 세계 컴퓨터 전공 학생을 대상으로 프로그램 경진대회를 개최하고 있습니다. " +
                            "국내에서도 온라인 예선대회를 개최하고 있으며, " +
                            "예선을 통과한 팀들이 모여서 다시 국내 본선대회를 거친 후 입상자를 국제 본선대회에 출전시키고 있습니다.",
                    /* ACM history written by ChatGpt*/
                    "ACM(Association for Computing Machinery)은 2000년에 설립되어, " +
                            "컴퓨터과학 분야에서 혁신과 발전을 이끌어왔습니다. " +
                            "이 학회는 초창기부터 컴퓨터 과학 분야의 중요한 발전을 주도해왔으며, " +
                            "2010년대에는 전 세계적으로 컴퓨터 과학자들을 연결하는 주요 플랫폼으로 자리매김했습니다. " +
                            "ACM은 꾸준한 성장을 거듭하여 오늘날에는 세계에서 가장 큰 컴퓨터 관련 학회 중 하나로 인정받고 있습니다. ",
                    "매주 목요일 오후 7시",
                    ClubType.DEPARTMENT,
                    masters.get(6)));
            departmentClubs.add(initClub("BOSS",
                    /* BOSS introduce from cse homepage*/
                    "컴퓨터소프트웨어공학과 보안 동아리 BOSS는 보안 계열 취업을 위해 " +
                            "매달 한 번씩 각자가 공부해 온 분야에 대해서 세미나를 진행하고 있습니다. " +
                            "'보안'이라는 분야에 대해 호기심을 가지고 스터디를 시작하면서 현재 어느 정도의 기틀을 잡게 되었습니다. ",
                    /* BOSS history written by ChatGpt*/
                    "BOSS는 컴퓨터소프트웨어공학과 학생들이 보안 분야에 관심을 키우고 전문성을 갖출 수 있도록 2010년에 창설되었습니다. " +
                            "그 후 지속적으로 모의해킹, 포렌식, 악성코드 분석 등 다양한 주제로 스터디를 진행하며 선배들의 지원을 받았습니다." +
                            " 이를 통해 멤버들은 보안 분야 취업을 위한 기반을 확고히 해나가고 있습니다.",
                    "매주 금요일 오후 8시",
                    ClubType.DEPARTMENT,
                    masters.get(7)));
            departmentClubs.add(initClub("Method",
                    /* Method introduce from cse homepage*/
                    "컴퓨터소프트웨어공학과 농구 동아리 Method입니다. " +
                            "저희 동아리는 선, 후배들이 같이 저녁에 주2회 같이 농구를 하며 운동도 하고 친목도 다지는 동아리입니다. " +
                            "활동은 주로 팀을 나눠 게임위주로 즐기는 편입니다. 가입하고 싶은데 농구를 못하셔도 괜찮습니다. " +
                            "농구를 좋아하시거나 친목을 다지고 싶으시면 누구든지 환영합니다.",
                    /* Method history written by ChatGpt*/
                    "Method는 컴퓨터소프트웨어공학과 학생들을 위한 농구 동아리로, 2015년에 창설되었습니다. " +
                            "선, 후배들이 함께 모여 매주 두 번 농구를 즐기고 운동을 하며 친목을 다집니다. " +
                            "게임 위주의 활동으로 팀을 나누어 경기를 즐기며, 실력보다는 즐거움과 친목을 중시합니다. " +
                            "모든 학생들을 환영하며, 농구를 못해도 참여할 수 있습니다.",
                    "매주 수요일 오후 7시",
                    ClubType.DEPARTMENT,
                    masters.get(8)));
            departmentClubs.add(initClub("SOFT",
                    /* SOFT introduce from cse homepage*/
                    "2011년 컴퓨터공학부가 학부시절 소속의 C. O. S. T라는 동아리에서 " +
                            "컴퓨터공학과와 컴퓨터 소프트웨어공학과로 나뉘며 새로이 S. O. F. T라는 " +
                            "컴퓨터소프트웨어공학과 소속의 축구동아리를 만들었습니다. " +
                            "주마다 주간 수업 후 주 2회 모여 시합 및 연습을 하며 체력증진과 친목을 다집니다. ",
                    /* SOFT history written by ChatGpt*/
                    "2011년, C.O.S.T 동아리가 컴퓨터공학과와 컴퓨터소프트웨어공학과로 분할되어 S.O.F.T 축구동아리가 탄생했습니다. " +
                            "매주 주간 수업 후 주 2회 모여 체력과 친목을 쌓으며 활동하고 있으며, 2018년까지 우수한 성적을 유지해왔습니다. " +
                            "저희는 항상 우승을 목표로 더 나은 모습을 보여드릴 것을 약속합니다.",
                    "매주 목요일 오후 7시",
                    ClubType.DEPARTMENT,
                    masters.get(9)));
        }

        /**
         * private String name;
         * private String introduction;
         * private String history;
         * private String meetingTime;
         * private ClubType clubType;
         */
        private Club initClub(String name, String introduction, String history, String meetingTime, ClubType clubType, Member master) {
            Club club = Club.builder()
                    .name(name)
                    .introduction(introduction)
                    .history(history)
                    .meetingTime(meetingTime)
                    .clubType(clubType)
                    .master(master)
                    .professor(Professor.builder()
                            .prof_name("김교수")
                            .prof_department("컴퓨터소프트웨어공학과")
                            .prof_phone_number("+82 10-9999-9999")
                            .build())
                    .build();

            club = clubRepository.save(club);
//            동아리장을 동아리부원에 넣는 로직 제외
            ClubMember clubMember = ClubMember.builder()
                    .member(master)
                    .club(club)
                    .build();
            clubMemberRepository.save(clubMember);
            return club;
        }


        private void initMember() {
            // 시스템 관리자 계정 1개 생성
            initMember("admin@email.com", "admin", "시스템 관리자",
                    LocalDate.of(1999, 1, 1), Gender.MALE, "컴퓨터소프트웨어공학과",
                    20180901, "+82 10-0000-0000", List.of("clubMember", "master", "admin"));
            // 동아리장 계정 10개 생성
            for (int i = 1; i <= 10; i++) {
                masters.add(initMember("master" + i + "@email.com", "master" + i, "동아리장" + i,
                        LocalDate.of(2000, 1, 1), Gender.FEMALE, "컴퓨터소프트웨어공학과",
                        20190901 + i, i < 10 ? "+82 10-1111-112" + i : "+82 10-1111-11" + i, List.of("clubMember", "master")));
            }
            // 일반 회원 계정 60개 생성
            for (int i = 1; i <= 10; i++) {
                members.add(initMember("user" + i + "@email.com", "user" + i, "일반유저" + i,
                        LocalDate.of(2001, 1, 1), Gender.MALE, "컴퓨터공학과",
                        20200901 + i, i < 10 ? "+82 10-1111-111" + i : "+82 10-1111-11" + i, List.of()));
            }
            for (int i = 11; i <= 60; i++) {
                members.add(initMember("user" + i + "@email.com", "user" + i, "일반유저" + i,
                        LocalDate.of(2001, 1, 1), Gender.MALE, "컴퓨터공학과",
                        20200901 + i, "+82 10-1111-11" + i, List.of("clubMember")));
            }
        }

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
        private Member initMember(String email, String password, String name, LocalDate birth, Gender gender, String department, Integer code, String phoneNumber, List<String> roles) {
            Member member = Member.builder()
                    .email(email)
                    .password(password)
                    .birth(birth)
                    .name(name)
                    .gender(gender)
                    .department(department)
                    .code(code)
                    .phoneNumber(phoneNumber)
                    .roles(roles)
                    .build();
            return memberRepository.save(member);
        }
    }
}
