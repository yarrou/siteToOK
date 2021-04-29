package site.alexkononsol.siteToOK.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String text;


    public Article() {
        date = LocalDate.now();
    }

    public Article(String text) {
        this.text = text;
        date = LocalDate.now();
    }

    public Article(String text, String title) {
        this.text = text;
        this.title = title;
        date = LocalDate.now();
    }
}
