package web.club_community.Member.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import web.club_community.domain.Gender;

import java.time.LocalDate;

@Getter
@Setter
public class MemberSignupRequest {
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String name;
    @NotNull
    private LocalDate birth;
    @NotNull
    private Gender gender;
    @NotNull
    private String department;
    @NotNull
    private Integer code;
    @NotNull
    private String phoneNumber;
}
