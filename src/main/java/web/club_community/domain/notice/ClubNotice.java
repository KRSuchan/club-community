package web.club_community.domain.notice;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import web.club_community.domain.Club;

@Entity
@Getter
@Setter
@DiscriminatorValue("D")
@SuperBuilder
@NoArgsConstructor
public class ClubNotice extends Notice {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLUB_ID")
    private Club club;

}
