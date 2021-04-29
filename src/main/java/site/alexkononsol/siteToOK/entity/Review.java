package site.alexkononsol.siteToOK.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
@Getter @Setter @NoArgsConstructor
@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime date;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String text;

    private boolean approved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public Review(String text) {
        this.text = text;
    }

}
