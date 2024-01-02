package ua.shortener.link.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Link", description = "API для посилань")
public final class LinkRestController {

    private final LinkService linkService;
    private final UserRepository userRepository;

    @GetMapping("/list")
    @Operation(summary = "Отримати всі посилання")
    public ResponseEntity<List<DTOLink>> getAllLinks() {
        List<Link> links = linkService.getAllLinks();
        List<DTOLink> dtoLinkList = links.stream()
                .map(Link::toDTO).toList();
        return ResponseEntity.ok(dtoLinkList);
    }

    @GetMapping("/{shortLink}")
    @Operation(summary = "Отримати посилання за коротким посиланням")
    public ResponseEntity<DTOLink> getLinkByShortLink(final @PathVariable @Parameter(description = "Коротке посилання") String shortLink) {
        return linkService.getLinkByShortLink(shortLink)
                .map(link -> ResponseEntity.ok(link.toDTO()))
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping("/create")
    @Operation(summary = "Створити нове посилання")
    public ResponseEntity<DTOLink> createLink(final @RequestBody @Parameter(description = "Дані для створення посилання")
                                                  DTOLink dtoLink) throws ChangeSetPersister.NotFoundException {
        User existingUser = userRepository.findById(1L)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        Link link = new Link();
        link.setUrl(dtoLink.getLink());
        link.setUser(existingUser);

        linkService.createLink(link);

        return ResponseEntity.ok(link.toDTO());
    }


    @PostMapping("/edit")
    @Operation(summary = "Редагувати посилання")
    public ResponseEntity<DTOLink> editLink(final @RequestBody @Parameter(description = "Оновлені дані посилання")
                                                DTOLink updatedDtoLink) {
        //todo fix user problem

        Link link = new Link();
        link.setShortLink(updatedDtoLink.getShortLink());
        link.setUrl(updatedDtoLink.getLink());
        link.setOpenCount(updatedDtoLink.getOpenCount());
        link.setUser(userRepository.findById(1L).orElseThrow());
        linkService.editLink(link);

        return ResponseEntity.ok(link.toDTO());

//        linkService.getLinkByShortLink(shortLink)
//                .map(existingLink -> {
//                    Link editedLink = linkService.editLink(existingLink);
//
//                    // Зберегти оригінальний shortLink
//                    String originalShortLink = existingLink.getShortLink();
//                    editedLink.setShortLink(originalShortLink);
//
//                    return ResponseEntity.ok(editedLink.toDTO());
//                })
//                .orElse(ResponseEntity.notFound().build());
    }



    @PostMapping("/delete/{shortLink}")
    @Operation(summary = "Видалити посилання")
    public ResponseEntity<Void> deleteLink(final @PathVariable @Parameter(description = "Коротке посилання")
                                               String shortLink) {
        linkService.deleteLink(shortLink);
        return ResponseEntity.ok().build();
    }
}