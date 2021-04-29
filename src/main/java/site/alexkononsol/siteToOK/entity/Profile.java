package site.alexkononsol.siteToOK.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import site.alexkononsol.siteToOK.SiteToOkApplication;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.IOException;
import java.time.LocalDate;
@Slf4j @Getter @Setter
@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @Column(name = "id")
    private Long id;
    @OneToOne
    @MapsId
    private User user;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateBirthday;
    private String name;
    private String soName;
    private int weight;
    @Column(name = "content", columnDefinition="bytea")
    byte[] content;


    public Profile() {
        try {
            content= SiteToOkApplication.class.getClassLoader().getResourceAsStream("static/images/user-male-circle.png").readAllBytes();

        } catch (IOException e) {
            log.error("default icon file not found",e);
        }
    }

    public Profile(Long id){
        this.id=id;
    }



}



