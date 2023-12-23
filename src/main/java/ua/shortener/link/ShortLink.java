package ua.shortener.link;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.shortener.users.User;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shortlink")
public class ShortLink {

    @Id
    @Column(name = "linkid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long linkId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @JsonIgnore
    private User user;

    @Column(name = "originalurl ", nullable = false)
    private String originalUrl;

    @Column(name = "tokenurl ", nullable = false)//
    private String tokenUrl; //8 символьний ключ

    @Column(name = "shorturl  ", nullable = false)
    private String shortUrl; // https://short/+token

    @Column(name = "startdate  ", nullable = false)
    @Builder.Default
    private LocalDateTime startDate = LocalDateTime.now(); // дата утворення

    @Column(name = "finaldate  ", nullable = false)
    private LocalDateTime finalDate= LocalDateTime.now().plusMonths(1);; // кінцева дата

    @Column(name = "visit  ", nullable = false)
    private int visit; // кількість переходів по посиланню
}