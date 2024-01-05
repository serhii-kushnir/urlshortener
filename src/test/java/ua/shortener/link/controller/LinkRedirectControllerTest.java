package ua.shortener.link.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import org.springframework.boot.test.context.SpringBootTest;
import ua.shortener.link.Link;
import ua.shortener.link.service.LinkRepository;
import ua.shortener.link.service.LinkService;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
//@SpringBootTest
class LinkRedirectControllerTest {
    @Mock
    private HttpServletResponse response;


    private final LinkRedirectController linkRedirectController = mock(LinkRedirectController.class);



    @Test
    void redirectLink() throws IOException {
        //Given
        String shortLink = "RTu8Rfgb";

        //When
        Mockito.doNothing().when(linkRedirectController).redirectLink(isA(String.class), isA(HttpServletResponse.class));

        //Then
        linkRedirectController.redirectLink(shortLink, response);
        linkRedirectController.redirectLink(shortLink, response);
        linkRedirectController.redirectLink(shortLink, response);

        Mockito.verify(linkRedirectController, times(3)).redirectLink(shortLink, response);


    }


}