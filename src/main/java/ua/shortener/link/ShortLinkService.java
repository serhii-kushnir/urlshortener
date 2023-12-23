package ua.shortener.link;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ShortLinkService {

    // Інші методи та поля...

    public void incrementClickCount(String shortUrl) {
        // Логіка оновлення статистики переходів
        // ...

        // Оновлення в базі даних
        // shortLinkRepository.incrementClickCount(shortUrl);
    }

    @Cacheable(value = "originalUrlCache", key = "#shortUrl")
    public String getOriginalUrlByShortUrl(String shortUrl) {
        // Логіка отримання оригінального посилання
        // ...

        //-----------------------------------
        // Test originalUrl
        String originalUrl;
        if ("12345678".equals(shortUrl)){
            originalUrl = "https://docs.google.com/document/d/1CmvPkcy-fo49BqlTwnC_iYBhj5KSmItM2V9Ps6lLtnI/edit";
        }else {
            originalUrl ="https://www.google.com.ua/?hl=uk";
        }//----------------------------------

                // Повернення оригінального посилання
        return originalUrl;
    }
}
