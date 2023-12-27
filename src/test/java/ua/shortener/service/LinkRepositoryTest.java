package ua.shortener.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.shortener.UrlShortenerApplication;
import ua.shortener.link.Link;
import ua.shortener.link.service.LinkRepository;
import ua.shortener.service.config.ContainersEnvironment;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UrlShortenerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class LinkRepositoryTest extends ContainersEnvironment {

    @Autowired
    public LinkRepository linkRepository;

    @Test
    public void WhenGetLinkExpectEmptyList(){
        List<Link> list = linkRepository.findAll();
        assertEquals(0, list.size());
    }
}
