package ua.shortener.link.controller;

import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ua.shortener.link.Link;
import ua.shortener.link.service.LinkService;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/sh")
@RequiredArgsConstructor
public class LinkRedirectController {

    private final LinkService linkService;

    @GetMapping("/{shortLink}")
    public void redirectLink(final @PathVariable String shortLink, HttpServletResponse response) throws IOException {
        Optional<Link> link = linkService.getLinkByShortLink(shortLink);
        if (link.isPresent()) {
            response.sendRedirect(link.get().getUrl());
            link.get().setOpenCount(link.get().getOpenCount() + 1);
            Link link1 = link.get();
            linkService.createLink(link1);

        } else {
            response.sendRedirect("/");
        }
    }
}
