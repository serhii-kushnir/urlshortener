package ua.shortener.link;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.shortener.link.service.LinkService;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class CacheSaver {
    private final LinkService linkService;
    @Scheduled(fixedRate = 600000)
    public void saveLinksFromCacheToDatabase() {
        Map<String, Link> cache = linkService.getCache();
        cache.keySet()
                .stream()
                        .map(cache::get)
                                .forEach(linkService::editLink);
        log.info("CACHE SAVER UPDATE DB WITH DATA FROM CACHE");
    }
}
