package ua.shortener.link.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public final class ShortLinkGenerator {

    private ShortLinkGenerator() {
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int LENGTH = 8;

    public static String generateShortLink() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(LENGTH);

        for (int i = 0; i < LENGTH; ++i) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }
}
