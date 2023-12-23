package ua.shortener.link.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

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

    public Link() {
        this.shortLink = generateShortLink();
        this.createdAt = LocalDateTime.now();
        this.openCount = 0;
    }
}
