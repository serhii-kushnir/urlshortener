package ua.shortener.link.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Link Redirect", description = "API для перенаправлення посилань")
public class LinkRedirectController {

    private final LinkService linkService;

    @GetMapping("/{shortLink}")
    @Operation(summary = "Перенаправити коротке посилання")
    public void redirectLink(final @PathVariable @Parameter(description = "Коротке посилання") String shortLink, HttpServletResponse response) throws IOException {
        Optional<Link> link = linkService.getLinkByShortLink(shortLink);
        if (link.isPresent()) {
            response.sendRedirect(link.get().getUrl());
            link.get().setOpenCount(link.get().getOpenCount() + 1);
            Link link1 = link.get();
            linkService.editLink(link1);

        } else {
            response.sendRedirect("/");
        }
    }
}
