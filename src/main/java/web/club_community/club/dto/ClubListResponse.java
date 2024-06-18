package web.club_community.club.dto;

import lombok.Builder;
import lombok.Data;
import web.club_community.domain.Club;
import web.club_community.domain.Member;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class ClubListResponse {
    private Boolean isClubMember;
    private List<ClubResponse> clubs;

    public static ClubListResponse of(List<Club> clubs, Member member) {
        return ClubListResponse.builder()
                .isClubMember(member.getRoles().contains("clubMember"))
                .clubs(clubs
                        .stream()
                        .map(ClubResponse::of)
                        .collect(Collectors.toList()))
                .build();
    }
}
