package web.club_community.post.clubVideo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoCreateRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String url;
}
