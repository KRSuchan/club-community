package web.club_community.Member;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import web.club_community.domain.Gender;

import java.time.LocalDate;

@Getter
@Setter
public class MemberCreateRequest {
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String name;
    @NotNull
    private LocalDate birthday;
    @NotNull
    private Gender gender;
    @NotNull
    private String department;
    @NotNull
    private Integer code;
    @NotNull
    private String phoneNumber;
}
