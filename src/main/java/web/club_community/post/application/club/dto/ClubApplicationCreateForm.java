package web.club_community.post.application.club.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import web.club_community.domain.ClubType;
import web.club_community.domain.Professor;

@Getter
@Setter
public class ClubApplicationCreateForm {
    @NotNull
    private String name;
    @NotNull
    private String introduction;
    @NotNull
    private String history;
    @NotNull
    private String meetingTime;
    @NotNull
    private ClubType clubType;
    //    prof_name : String
    //    prof_department : String
    //    prof_phone_number : String
    @NotNull
    private Professor professor;
}
