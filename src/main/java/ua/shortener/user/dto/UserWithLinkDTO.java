package ua.shortener.user.dto;

import lombok.Data;

@Data
public class UserWithLinkDTO {

    private String name;
    private String shortLink;
    private String link;
    private Integer openCount;

}
