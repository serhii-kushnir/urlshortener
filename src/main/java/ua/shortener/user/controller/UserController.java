package ua.shortener.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ua.shortener.link.Link;
import ua.shortener.link.service.LinkService;
import ua.shortener.user.User;
import ua.shortener.user.service.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/shortify")
public class UserController {
    private final UserService userService;
    private final LinkService linkService;

    @PostMapping("/home/generate")
    public String generateLink(@RequestParam("url") String url ) throws ChangeSetPersister.NotFoundException {
        User existingUser = userService.getUserById(1L)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        Link link = new Link();
        link.setUrl(url);
        link.setUser(existingUser);
        linkService.createLink(link);
        return "HOME";
    }
    @GetMapping("/about")
    public ModelAndView showAboutPage(){
        ModelAndView result = new ModelAndView("/about");
        return result;
    }

    @GetMapping("/thanks_to")
    public ModelAndView showThanksToPage(){
        ModelAndView result = new ModelAndView("/thanks_to");
        return result;
    }

}
