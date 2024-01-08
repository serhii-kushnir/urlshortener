package ua.shortener.link.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DTOLinkTest {

    @Test
    void getShortLink() {
        DTOLink dtoLink = new DTOLink();
        dtoLink.setShortLink("testLink");
        assertEquals("testLink", dtoLink.getShortLink());
    }

    @Test
    void getLink() {
        DTOLink dtoLink = new DTOLink();
        dtoLink.setLink("originalLink");
        assertEquals("originalLink", dtoLink.getLink());
    }

    @Test
    void getOpenCount() {
        DTOLink dtoLink = new DTOLink();
        dtoLink.setOpenCount(123);
        assertEquals(123, dtoLink.getOpenCount());
    }

    @Test
    void setShortLink() {
        DTOLink dtoLink = new DTOLink();
        dtoLink.setShortLink("testLink");
        assertEquals("testLink", dtoLink.getShortLink());
    }

    @Test
    void setLink() {
        DTOLink dtoLink = new DTOLink();
        dtoLink.setLink("originalLink");
        assertEquals("originalLink", dtoLink.getLink());
    }

    @Test
    void setOpenCount() {
        DTOLink dtoLink = new DTOLink();
        dtoLink.setOpenCount(123);
        assertEquals(123, dtoLink.getOpenCount());
    }
}