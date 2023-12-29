package ua.shortener.link.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import ua.shortener.link.Link;
import ua.shortener.link.dto.DTOLink;

import java.util.List;
import java.util.Optional;

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

    public void deleteLink(final String shortLink) {
        linkRepository.deleteById(shortLink);
    }

    public Link editLink(Link existingLink) {
        if(linkRepository.findById(existingLink.getShortLink()).isEmpty()){
            System.out.println("Such link not exist");
            return null;
        }

        //        if (!existingLink.getUrl().equals(updatedDtoLink.getLink())) {
//            existingLink.setUrl(updatedDtoLink.getLink());
//        }

        return linkRepository.save(existingLink);
    }
}
