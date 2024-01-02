package ua.shortener.link.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ua.shortener.link.Link;
import ua.shortener.link.dto.DTOLink;
import ua.shortener.link.service.LinkService;

import ua.shortener.user.User;
import ua.shortener.user.service.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @GetMapping("/{shortLink}")
    public ResponseEntity<DTOLink> getLinkByShortLink(final @PathVariable String shortLink) {
        DTOLink redirect = linkService.redirect(shortLink);
        return redirect == null ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(redirect);
    }


    @PostMapping("/create")
    public ResponseEntity<DTOLink> createLink(final @RequestBody DTOLink dtoLink) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Optional<User> optionalUser = userRepository.findUserByEmail(userEmail);

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();

            Link link = new Link();
            link.setUrl(dtoLink.getLink());
            link.setUser(existingUser);

            linkService.createLink(link);

            return ResponseEntity.ok(link.toDTO());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    @PostMapping("/edit")
    public ResponseEntity<DTOLink> editLink(final @RequestBody DTOLink updatedDtoLink) {
        Link link = new Link();
        link.setShortLink(updatedDtoLink.getShortLink());
        link.setUrl(updatedDtoLink.getLink());
        link.setOpenCount(updatedDtoLink.getOpenCount());
        link.setUser(userRepository.findById(1L).orElseThrow());
        linkService.editLink(link);

        return ResponseEntity.ok(link.toDTO());
    }


    @PostMapping("/delete/{shortLink}")
    public ResponseEntity<Void> deleteLink(final @PathVariable String shortLink) {
        linkService.deleteLink(shortLink);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}