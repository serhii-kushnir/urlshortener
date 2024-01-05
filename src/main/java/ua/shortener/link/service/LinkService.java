package ua.shortener.link.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import ua.shortener.link.Link;
import ua.shortener.link.dto.DTOLink;

import java.io.IOException;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public final class LinkService {

    private final LinkRepository linkRepository;

    public List<Link> getAllLinks() {
        return linkRepository.findAll();
    }

    public Optional<Link> getLinkByShortLink(final String shortLink) {
        return linkRepository.findById(shortLink);
    }

    public void createLink(final Link link) {
        linkRepository.save(link);
    }

    private Optional<Boolean> isUrlAccessible(String url) {
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
        linkRepository.deleteById(shortLink);
    }

    public Link editLink(final Link existingLink) {
        if(linkRepository.findById(existingLink.getShortLink()).isEmpty()){
            return null;
        }

        return linkRepository.save(existingLink);
    }

    public Map<String, List<DTOLink>> getAllLinksDTO(){
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

    public List<DTOLink> getActiveLinksDTO(){
        return getAllLinksDTO().get("Active");
    }

    public List<DTOLink> getNonActiveLinksDTO(){
        return getAllLinksDTO().get("Not active");
    }

    public void redirect(final String shortLink, HttpServletResponse response) throws IOException {
        Optional<Link> searchedLink = getLinkByShortLink(shortLink);
        if (searchedLink.isPresent()) {
            response.sendRedirect(searchedLink.get().getUrl());
            Link link = searchedLink.get();
            link.setOpenCount(link.getOpenCount() + 1);
            editLink(link);
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
}
