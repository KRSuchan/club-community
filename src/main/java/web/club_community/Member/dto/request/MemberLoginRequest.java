package web.club_community.Member.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberLoginRequest {
    @NotNull
    private String email;
    @NotNull
    private String password;
}
