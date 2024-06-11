package web.club_community.domain.notice;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
@ToString
@NoArgsConstructor
@SuperBuilder
public abstract class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "CONTENTS", nullable = false)
    private String contents;

    @Column(name = "CREATE_TIME", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "UPDATE_TIME", nullable = false)
    private LocalDateTime updatedTime = LocalDateTime.now();
}
