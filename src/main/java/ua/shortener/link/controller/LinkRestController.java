package ua.shortener.link.controller;

import jakarta.servlet.http.HttpServletResponse;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/link")
@RequiredArgsConstructor
public final class LinkRestController {

    private final LinkService linkService;
    private final UserRepository userRepository;

    @GetMapping("/list/all")
    public ResponseEntity<Map<String, List<DTOLink>>> getAllLinks() {
        return ResponseEntity.ok(linkService.getAllLinksDTO());
    }

    @GetMapping("/list/active")
    public ResponseEntity< List<DTOLink>> getActiveLinks() {
        return ResponseEntity.ok(linkService.getActiveLinksDTO());
    }

    @GetMapping("/list/non-active")
    public ResponseEntity< List<DTOLink>> getNonActiveLinks() {
        return ResponseEntity.ok(linkService.getNonActiveLinksDTO());
    }

    //todo think about response logic
    @GetMapping("/{shortLink}")
    public ResponseEntity<DTOLink> getLinkByShortLink(final @PathVariable String shortLink) throws IOException {
        DTOLink redirect = linkService.redirect(shortLink);
        return redirect == null ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(redirect);
    }


    @PostMapping("/create")
    public ResponseEntity<DTOLink> createLink(final @RequestBody DTOLink dtoLink) throws ChangeSetPersister.NotFoundException {
        User existingUser = userRepository.findById(1L)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        Link link = new Link();
        link.setUrl(dtoLink.getLink());
        link.setUser(existingUser);
        System.out.println("link = " + link);

        linkService.createLink(link);

        return ResponseEntity.ok(link.toDTO());
    }


    @PostMapping("/edit")
    public ResponseEntity<DTOLink> editLink(final @RequestBody DTOLink updatedDtoLink) {
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
    public ResponseEntity<Void> deleteLink(final @PathVariable String shortLink) {
        linkService.deleteLink(shortLink);
        return ResponseEntity.ok().build();
    }
}