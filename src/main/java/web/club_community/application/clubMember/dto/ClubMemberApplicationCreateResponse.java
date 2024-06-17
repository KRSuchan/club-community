package web.club_community.application.clubMember.dto;

import lombok.Builder;
import lombok.Data;
import web.club_community.domain.ClubMemberApplication;
import web.club_community.domain.Member;

@Builder
@Data
public class ClubMemberApplicationCreateResponse {
    private Integer clubMemberApplicationId;
    private String applierName;

    public static ClubMemberApplicationCreateResponse of(Member member, ClubMemberApplication application) {
        return ClubMemberApplicationCreateResponse.builder()
                .clubMemberApplicationId(application.getId())
                .applierName(member.getName()).build();
    }
}
