package ua.shortener.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.shortener.link.Link;
import ua.shortener.link.service.LinkService;
import ua.shortener.security.auth.dto.SignUpRequest;
import ua.shortener.user.User;
import ua.shortener.user.service.UserService;
import org.springframework.validation.BindingResult;

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
        return "/home/generate";
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
    @PostMapping("/signup")
    public String saveUser(@Valid SignUpRequest signUpRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "login_register";
        }else {
            userService.saveUser(signUpRequest);
           return "success";
        }
    }

}
