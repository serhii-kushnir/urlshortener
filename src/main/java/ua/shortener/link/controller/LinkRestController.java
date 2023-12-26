package ua.shortener.link.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ua.shortener.link.Link;
import ua.shortener.link.dto.DTOLink;
import ua.shortener.link.service.LinkService;
import ua.shortener.user.User;
import ua.shortener.user.service.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/link")
@RequiredArgsConstructor
public final class LinkRestController {

    private final LinkService linkService;
    private final UserRepository userRepository;

    @GetMapping("/list")
    public ResponseEntity<List<DTOLink>> getAllLinks() {
        List<Link> links = linkService.getAllLinks();
        List<DTOLink> dtoLinkList = links.stream()
                .map(Link::toDTO).toList();
        return ResponseEntity.ok(dtoLinkList);
    }

    @GetMapping("/{shortLink}")
    public ResponseEntity<DTOLink> getLinkByShortLink(final @PathVariable String shortLink) {
        return linkService.getLinkByShortLink(shortLink)
                .map(link -> ResponseEntity.ok(link.toDTO()))
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping("/create")
    public ResponseEntity<DTOLink> createLink(final @RequestBody DTOLink dtoLink) throws ChangeSetPersister.NotFoundException {
        User existingUser = userRepository.findById(1L)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        Link link = new Link();
        link.setLink(dtoLink.getLink());
        link.setUser(existingUser);

        linkService.createLink(link);

        return ResponseEntity.ok(link.toDTO());
    }


    @PostMapping("/edit/{shortLink}")
    public ResponseEntity<DTOLink> editLink(final @PathVariable String shortLink, final @RequestBody DTOLink updatedDtoLink) {
        return linkService.getLinkByShortLink(shortLink)
                .map(existingLink -> {
                    Link editedLink = linkService.editLink(existingLink, updatedDtoLink);

                    // Зберегти оригінальний shortLink
                    String originalShortLink = existingLink.getShortLink();
                    editedLink.setShortLink(originalShortLink);

                    return ResponseEntity.ok(editedLink.toDTO());
                })
                .orElse(ResponseEntity.notFound().build());
    }



    @PostMapping("/delete/{shortLink}")
    public ResponseEntity<Void> deleteLink(final @PathVariable String shortLink) {
        linkService.deleteLink(shortLink);
        return ResponseEntity.ok().build();
    }
}