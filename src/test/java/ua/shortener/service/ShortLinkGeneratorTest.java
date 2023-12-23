package ua.shortener.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ShortLinkGeneratorTest {

    @Test
    void generateShortLink_shouldReturnStringOfCorrectLength() {
        // given
        ShortLinkGenerator underTest = new ShortLinkGenerator();

        // when
        String result = underTest.generateShortLink();

        // then
        assertThat(result).hasSize(7);
    }

    @Test
    void generateShortLink_shouldReturnStringContainingOnlyAllowedCharacters() {
        // given
        ShortLinkGenerator underTest = new ShortLinkGenerator();

        // when
        String result = underTest.generateShortLink();

        // then
        assertThat(result).matches("[A-Za-z0-9]+");
    }

    @Test
    void generateShortLink_shouldNotReturnTheSameResultTwice() {
        // given
        ShortLinkGenerator underTest = new ShortLinkGenerator();

        // when
        String result1 = underTest.generateShortLink();
        String result2 = underTest.generateShortLink();

        // then
        assertThat(result1).isNotEqualTo(result2);
    }
}