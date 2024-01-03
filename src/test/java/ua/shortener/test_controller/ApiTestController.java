package ua.shortener.test_controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.shortener.UrlShortenerApplication;
import ua.shortener.link.Link;
import ua.shortener.link.controller.LinkRedirectController;
import ua.shortener.link.service.LinkRepository;

import java.io.IOException;
import java.util.Optional;


@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UrlShortenerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiTestController {
    @Autowired
    public LinkRepository linkRepository;
    @Autowired
    public LinkRedirectController linkRestController;

    @Test
    void longUrlGettingTest() throws IOException {
        String shortLink = "testlink";
        Optional<Link> link = linkRepository.findById(shortLink);

        int expectedCount = getCountFromLink(link) +1;

        MockHttpServletResponse response = new MockHttpServletResponse();
        linkRestController.redirectLink(shortLink, response);

        Optional<Link> link1 = linkRepository.findById(shortLink);
        int actualCount = getCountFromLink(link1);
        Assertions.assertEquals(expectedCount, actualCount);
    }

    private int getCountFromLink(Optional<Link> link){
        int count = 0;
        if(link.isPresent()){
            count = link.get().getOpenCount();
            System.out.println("link.get() = " + link.get());
        }
        return count;
    }
}
