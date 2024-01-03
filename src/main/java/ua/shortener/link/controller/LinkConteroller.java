package ua.shortener.link.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.shortener.link.Link;
import ua.shortener.link.service.LinkService;
import ua.shortener.security.auth.dto.SignUpRequest;
import ua.shortener.user.User;
import ua.shortener.user.service.UserRepository;

@Controller
@RequiredArgsConstructor
@RequestMapping("/shortify")
public class LinkConteroller {
    private final LinkService linkService;


    @GetMapping("/home_guest")
    public ModelAndView getAllLinks(){
        ModelAndView result = new ModelAndView("/home_guest");
        result.addObject("linkList", linkService.getAllLinks());
        return result;
    }
    @GetMapping("/welcome")
    public ModelAndView showWelcomePage(){
        SignUpRequest signUpRequest = new SignUpRequest();
        ModelAndView result = new ModelAndView("/login_register");
        result.addObject("signUpRequest", signUpRequest);
        return result;
    }



}
