package ua.shortener.link.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.shortener.link.Link;
import ua.shortener.link.service.LinkService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/link")
@RequiredArgsConstructor
public final class LinkRestController {

    private final LinkService linkService;

    @GetMapping("/list")
    public ResponseEntity<List<Link>> getAllLinks() {
        List<Link> links = linkService.getAllLinks();
        return ResponseEntity.ok(links);
    }

    @GetMapping("/{shortLink}")
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

    @PostMapping("/edit/{shortLink}")
    public ResponseEntity<Link> editLink(final @PathVariable String shortLink, final @RequestBody Link updatedLink) {
        return linkService.getLinkByShortLink(shortLink)
                .map(existingLink -> {
                    existingLink.setLink(updatedLink.getLink());
                    Link editedLink = linkService.editLink(existingLink);
                    return ResponseEntity.ok(editedLink);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/delete/{shortLink}")
    public ResponseEntity<Void> deleteLink(final @PathVariable String shortLink) {
        linkService.deleteLink(shortLink);
        return ResponseEntity.ok().build();
    }
}