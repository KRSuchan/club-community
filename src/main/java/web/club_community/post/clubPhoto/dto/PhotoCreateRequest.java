package web.club_community.post.clubPhoto.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhotoCreateRequest {
    @NotBlank
    private String title;
}
