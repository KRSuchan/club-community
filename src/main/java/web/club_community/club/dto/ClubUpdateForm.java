package web.club_community.club.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClubUpdateForm {
    @NotNull
    private String clubName;
    @NotNull
    private String introduction;
    @NotNull
    private String history;
    @NotNull
    private String meetingTime;
}
