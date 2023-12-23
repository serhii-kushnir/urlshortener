package ua.shortener.link;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ShortLinkController {

    @Autowired
    private ShortLinkService shortLinkService;

    @GetMapping("/ShortLink/{shortUrl}")
    public ModelAndView redirectToOriginalUrl(@PathVariable String shortUrl) {
        // Логіка переходу за коротким посиланням та отримання оригінального посилання
        String originalUrl = shortLinkService.getOriginalUrlByShortUrl(shortUrl);

        if (originalUrl != null) {
            // Оновлення статистики переходів
            shortLinkService.incrementClickCount(shortUrl);

            // Перенаправлення на оригінальне посилання
            return new ModelAndView(new RedirectView(originalUrl));
        } else {
            // Обробка випадку, коли коротке посилання не знайдено
            return new ModelAndView("error"); // Призначте свою сторінку для обробки помилок
        }
    }
}
