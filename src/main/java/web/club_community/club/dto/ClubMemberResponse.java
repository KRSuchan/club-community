package web.club_community.club.dto;

import lombok.Builder;
import lombok.Data;
import web.club_community.domain.ClubMember;
import web.club_community.domain.Gender;
import web.club_community.domain.Member;

@Builder
@Data
public class ClubMemberResponse {
    private Integer clubMemberId; //
    private String email;
    private String name;
    private Gender gender;
    private String department;
    private String phoneNumber;

    public static ClubMemberResponse of(ClubMember clubMember) {
        Member member = clubMember.getMember();
        return ClubMemberResponse.builder()
                .clubMemberId(clubMember.getId())
                .email(member.getEmail())
                .name(member.getName())
                .gender(member.getGender())
                .department(member.getDepartment())
                .phoneNumber(member.getPhoneNumber())
                .build();
    }
}
