package web.club_community.Member.dto.response;

import lombok.Builder;
import lombok.Data;
import web.club_community.domain.Member;

@Builder
@Data
public class LoginResponse {
    private String name;
    private Boolean isAdmin;

    public static LoginResponse of(Member member) {
        return LoginResponse.builder()
                .isAdmin(member.getRoles().contains("admin"))
                .name(member.getName())
                .build();
    }
}
