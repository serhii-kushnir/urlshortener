package ua.shortener.link;

import jakarta.persistence.*;
import lombok.Data;
import ua.shortener.user.User;

import java.time.LocalDateTime;

import static ua.shortener.link.service.ShortLinkGenerator.generateShortLink;


@Entity
@Data
@Table(name = "links")
public class Link {

    @Id
    @Column(name = "short_link", length = 8)
    private String shortLink;

    @Column(name = "link", length = 100, nullable = false)
    private String link;

    @Column(name = "open_count", nullable = false)
    private Integer openCount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Link() {
        this.shortLink = generateShortLink();
        this.createdAt = LocalDateTime.now();
        this.openCount = 0;
    }
}
