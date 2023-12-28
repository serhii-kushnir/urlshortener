package ua.shortener.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ua.shortener.link.service.ShortLinkGenerator.generateShortLink;

class ShortLinkGeneratorTest {

    @Test
    void generateShortLink_shouldReturnStringOfCorrectLength() {
        // given && when
        String result = generateShortLink();

        // then
        assertThat(result).hasSize(8);
    }

    @Test
    void generateShortLink_shouldReturnStringContainingOnlyAllowedCharacters() {
        // given && when
        String result = generateShortLink();

        // then
        assertThat(result).matches("[A-Za-z0-9]+");
    }

    @Test
    void generateShortLink_shouldNotReturnTheSameResultTwice() {
        // given && when
        String result1 = generateShortLink();
        String result2 = generateShortLink();

        // then
        assertThat(result1).isNotEqualTo(result2);
    }
}