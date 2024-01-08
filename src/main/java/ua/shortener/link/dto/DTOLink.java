package ua.shortener.link.dto;

import lombok.Data;

@Data
public class DTOLink {
    private String shortLink;
    private String link;
    private Integer openCount;
}
