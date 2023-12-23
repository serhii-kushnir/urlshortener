package ua.shortener.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import ua.shortener.entity.Link;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;

    public List<Link> getAllLinks() {
        return linkRepository.findAll();
    }

    public Optional<Link> getLinkByShortLink(final String shortLink) {
        return linkRepository.findById(shortLink);
    }

    public Link createLink(final Link link) {
        return linkRepository.save(link);
    }

    public void deleteLink(final String shortLink) {
        linkRepository.deleteById(shortLink);
    }

    private static void exception(final String msg) {
        throw new IllegalArgumentException(msg);
    }
}