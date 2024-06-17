package web.club_community.club.dto;

import lombok.Builder;
import lombok.Data;
import web.club_community.domain.Member;

@Builder
@Data
public class MasterResponse {
    private Integer studentCode;
    private String name;
    private String phoneNumber;

    public static MasterResponse of(Member member) {
        return MasterResponse.builder()
                .studentCode(member.getCode())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber()).build();
    }
}
