package ua.shortener.link;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;

import lombok.Data;

import ua.shortener.link.dto.DTOLink;
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

    @Column(name = "url", length = 100, nullable = false)
    private String url;

    @Column(name = "open_count", nullable = false)
    private Integer openCount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Link() {
        this.shortLink = generateShortLink();
        this.createdAt = LocalDateTime.now();
        this.openCount = 0;
    }

    public DTOLink toDTO() {
        DTOLink dtoLink = new DTOLink();
        dtoLink.setShortLink(this.getShortLink());
        dtoLink.setLink(this.getUrl());
        dtoLink.setOpenCount(this.getOpenCount());

        return dtoLink;
    }
}
