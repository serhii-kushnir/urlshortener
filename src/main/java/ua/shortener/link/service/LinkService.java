package ua.shortener.link.service;

import jakarta.servlet.http.HttpServletResponse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import ua.shortener.link.Link;
import ua.shortener.link.dto.DTOLink;
import ua.shortener.user.User;
import ua.shortener.user.service.UserService;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Principal;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public final class LinkService {

    private final LinkRepository linkRepository;
    @Getter
    private final Map<String, Link> cache = new HashMap<>();
    private final UserService userService;


    public List<Link> getAllLinks() {
        return linkRepository.findAll();
    }

    public Optional<Link> getLinkByShortLink(final String shortLink) {
        return linkRepository.findById(shortLink);
    }

    public void createLink(Link link) {
        String url = link.getUrl();
        if (!url.startsWith("https://") && !url.startsWith("http://")){
            url = "https://" + url;
        }
        if (!isUrlAccessible(url).orElse(false)) {
            throw new IllegalArgumentException("Invalid URL");
        }
        link.setUrl(url);
        linkRepository.save(link);
    }

    private Optional<Boolean> isUrlAccessible(final String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return Optional.of(responseCode == HttpURLConnection.HTTP_OK);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public void deleteLink(final String shortLink) {
        if (!linkRepository.existsById(shortLink)) {
            throw new IllegalArgumentException("Link with id " + shortLink + " not found");
        } else {
            Optional<Link> userById = linkRepository.findById(shortLink);
            if (userById.isPresent()) {
                Link linkToDelete = userById.get();
                User user = linkToDelete.getUser();
                user.getLinks().remove(linkToDelete);
            }
            linkRepository.deleteById(shortLink);
            log.info("Note by " + shortLink + " was deleted");
        }
    }

    public Link editLink(Link existingLink) {
        if (linkRepository.findById(existingLink.getShortLink()).isEmpty()) {
            return null;
        }
        return linkRepository.save(existingLink);
    }

    public Map<String, List<DTOLink>> getAllLinksDTO() {
        List<Link> links = getAllLinks();
        LocalDateTime dateTime = LocalDateTime.now();
        Map<String, List<DTOLink>> result = new HashMap<>();

        List<DTOLink> notActiveLinks = links
                .stream()
                .filter(link -> link.getValidUntil().isBefore(dateTime))
                .map(Link::toDTO)
                .toList();

        List<DTOLink> activeLinks = links
                .stream()
                .filter(link -> link.getValidUntil().isAfter(dateTime))
                .map(Link::toDTO)
                .toList();

        result.put("Active", activeLinks);
        result.put("Not active", notActiveLinks);

        return result;
    }

    public List<DTOLink> getActiveLinksDTO() {
        return getAllLinksDTO().get("Active");
    }

    public List<DTOLink> getNonActiveLinksDTO() {
        return getAllLinksDTO().get("Not active");
    }

    public void redirect(final String shortLink, final HttpServletResponse response) throws IOException {
        if (cache.containsKey(shortLink)){
            Link cacheLink = cache.get(shortLink);
            String url = cacheLink.getUrl();
            response.sendRedirect(url);
            cacheLink.setOpenCount(cacheLink.getOpenCount() + 1);
            return;
        }

        Optional<Link> optionalSearchedLink = getLinkByShortLink(shortLink);
        if (optionalSearchedLink.isPresent()) {
            Link searchedLink = optionalSearchedLink.get();
            response.sendRedirect(optionalSearchedLink.get().getUrl());
            Link link = optionalSearchedLink.get();
            link.setOpenCount(link.getOpenCount() + 1);
            editLink(link);
            cache.put(searchedLink.getShortLink(), searchedLink);
        } else {
            response.sendRedirect("/");
        }
    }

    public DTOLink redirect(final String shortLink) {
        Optional<Link> searchedLink = getLinkByShortLink(shortLink);
        if (searchedLink.isPresent()) {
            Link link = searchedLink.get();
            link.setOpenCount(link.getOpenCount() + 1);
            editLink(link);
            return link.toDTO();
        }
        return null;
    }

    public Map<String, List<Link>> getAllUsersLinks(String email) {
        User userByEmail = userService.findUserByEmail(email).orElseThrow();
        LocalDateTime now = LocalDateTime.now();
        Map<String, List<Link>> linksMap = new HashMap<>();
        long userId = userByEmail.getId();
        cache
                .keySet()
                .stream()
                .filter(shortUrl -> cache.get(shortUrl).getUser().getId() == userId)
                .map(cache::get)
                .forEach(this::editLink);
        List<Link> links = userByEmail.getLinks();
        List<Link> active = links.stream()
                .filter(link -> link.getValidUntil().isAfter(now))
                .toList();

        List<Link> notActive = links.stream()
                .filter(link -> link.getValidUntil().isBefore(now))
                .toList();

        linksMap.put("activeLinks", active);
        linksMap.put("notActiveLinks", notActive);
        return linksMap;
    }

    public Map<String, List<DTOLink>> getAllUsersDTOLinks(String email){
        return transformToDTOLink(getAllUsersLinks(email));
    }

    private Map<String, List<DTOLink>> transformToDTOLink(Map<String, List<Link>> mapUsersLink){
        Map<String, List<DTOLink>> result = new HashMap<>();
        List<DTOLink> activeLinks = mapUsersLink.get("activeLinks").stream()
                .map(Link::toDTO)
                .toList();
        List<DTOLink> notActiveLinks = mapUsersLink.get("notActiveLinks").stream()
                .map(Link::toDTO)
                .toList();
        result.put("activeLinks", activeLinks);
        result.put("notActiveLinks", notActiveLinks);
        return result;
    }
}
