package web.club_community.application.clubMember.dto;

import lombok.Builder;
import lombok.Data;
import web.club_community.domain.ClubMemberApplication;
import web.club_community.domain.Member;

@Builder
@Data
public class ClubMemberApplicationResponse {
    private Integer applicationId;
    private String applierName;
    private String applierPhoneNumber;
    private String applierDepartment;
    private String fileName;

    public static ClubMemberApplicationResponse of(ClubMemberApplication application) {
        Member applier = application.getMember();
        return ClubMemberApplicationResponse.builder()
                .applicationId(application.getId())
                .applierName(applier.getName())
                .applierPhoneNumber(applier.getPhoneNumber())
                .applierDepartment(applier.getDepartment())
                .fileName(application.getFileName())
                .build();
    }
}
