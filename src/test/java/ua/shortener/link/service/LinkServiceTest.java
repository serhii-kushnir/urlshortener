package ua.shortener.link.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ua.shortener.link.Link;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LinkServiceTest {

    @InjectMocks
    private LinkService linkService;

    @Mock
    private LinkRepository linkRepository;

    @Test
    void getAllLinks() {
    }

    @Test
    void getLinkByShortLink() {
    }

    @Test
    void createLink() {
    }

    @Test
    void deleteLink() {
    }

    @Test
    void editLink() {
    }

    @Test
    void getAllLinksDTO() {

    }

    @Test
    void getActiveLinksDTO() {
    }

    @Test
    void getNonActiveLinksDTO() {
    }

    @Test
    void redirect() {
    }

    @Test
    void testRedirect() {
    }
}