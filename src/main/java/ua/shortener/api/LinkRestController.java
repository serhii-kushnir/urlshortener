package ua.shortener.api;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ua.shortener.entity.Link;
import ua.shortener.service.LinkService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public final class LinkRestController {

    private final LinkService linkService;

    @GetMapping("/links")
    public ResponseEntity<List<Link>> getAllLinks() {
        List<Link> links = linkService.getAllLinks();
        return ResponseEntity.ok(links);
    }

    @GetMapping("/id/{shortLink}")
    public ResponseEntity<Link> getLinkByShortLink(final @PathVariable String shortLink) {
        return linkService.getLinkByShortLink(shortLink)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<Link> createLink(final @RequestBody Link link) {
        Link createdLink = linkService.createLink(link);
        return ResponseEntity.ok(createdLink);
    }

    @PostMapping("/delete/{shortLink}")
    public ResponseEntity<Void> deleteLink(final @PathVariable String shortLink) {
        linkService.deleteLink(shortLink);
        return ResponseEntity.noContent().build();
    }
}
