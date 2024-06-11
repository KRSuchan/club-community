package web.club_community.Member.dto.response;

import lombok.Builder;
import lombok.Data;
import web.club_community.domain.Gender;
import web.club_community.domain.Member;

import java.time.LocalDate;

@Data
@Builder
public class MemberSignupResponse {
    private String email;
    private String name;
    private LocalDate birth;
    private Gender gender;
    private String department;
    private Integer code;
    private String phoneNumber;

    public static MemberSignupResponse of(Member member) {
        return MemberSignupResponse.builder()
                .email(member.getEmail())
                .name(member.getName())
                .birth(member.getBirth())
                .gender(member.getGender())
                .department(member.getDepartment())
                .code(member.getCode())
                .phoneNumber(member.getPhoneNumber())
                .build();
    }
}
