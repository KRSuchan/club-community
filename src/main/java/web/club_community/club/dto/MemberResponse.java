package web.club_community.club.dto;

import lombok.Builder;
import lombok.Data;
import web.club_community.domain.Member;

@Builder
@Data
public class MemberResponse {
    private Integer studentCode;
    private String name;
    private String phoneNumber;

    public static MemberResponse of(Member member) {
        return MemberResponse.builder()
                .studentCode(member.getCode())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber()).build();
    }
}
