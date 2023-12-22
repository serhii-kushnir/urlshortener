package ua.shortener.link;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "shortlink")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLink {

    @Id
    @Column(name = "linkid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long linkId;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "originalurl ", nullable = false)
    private String originalUrl;

    @Column(name = "token ", nullable = false)//
    private String token; //8 символьний ключ

    @Column(name = "shorturl  ", nullable = false)
    private String shortUrl; // https://short/+token

    @Column(name = "startdate  ", nullable = false)
    private Date startDate; // дата утворення

    @Column(name = "finaldate  ", nullable = false)
    private Date finalDate; // кінцева дата

    @Column(name = "visit  ", nullable = false)
    private int visit; // кількість переходів по посиланню
}
