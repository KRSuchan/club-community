package web.club_community.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class Professor {
    private String prof_name;
    private String prof_department;
    private String prof_phone_number;
}
