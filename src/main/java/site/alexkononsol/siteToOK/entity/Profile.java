package site.alexkononsol.siteToOK.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDate;
@Slf4j @Getter @Setter
@Entity
@Component
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
    private String avatarLink;
    @Column(name = "content", columnDefinition="bytea")
    byte[] content;


    public Profile() {
    }
    public Profile(String defaultLink){
        setAvatarLink(defaultLink);
    }

    public Profile(Long id){
        this.id=id;
    }
    
}



