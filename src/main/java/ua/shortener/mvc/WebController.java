package ua.shortener.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ua.shortener.link.Link;
import ua.shortener.link.service.LinkService;

import ua.shortener.security.auth.AuthenticationServiceImpl;
import ua.shortener.security.auth.dto.login.LoginRequest;
import ua.shortener.security.auth.dto.registration.RegistrationRequest;

import ua.shortener.user.User;
import ua.shortener.user.service.UserService;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/shortify")
@Slf4j

public final class WebController {

    private final LinkService linkService;
    private final UserService userService;
    private final AuthenticationServiceImpl authenticationService;
    private static final String REDIRECT_SHORTIFY_HOME_USER = "redirect:/shortify/home_user";

    @GetMapping("/register")
    public ModelAndView showRegistrationPage(final @ModelAttribute("registrationRequest") RegistrationRequest registrationRequest) {
        return new ModelAndView("/register");
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login_page";
    }

    @PostMapping("/register")
    public String registerUser(RegistrationRequest registrationRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()){

            return "register";
        } else {
            authenticationService.createUserFromRequest(registrationRequest);
            return "success";
        }
    }

    @PostMapping("/delete")
    public String deleteLink(final @RequestParam(name = "shortLink") String shortLink) {
        linkService.deleteLink(shortLink);
        return REDIRECT_SHORTIFY_HOME_USER;
    }

    @GetMapping("/home_user/example")
    public String showQRcodePage() {
        return "example_video";
    }

    @PostMapping("/home_user/generate")
    public String generateLink(final @RequestParam("url") String url,
                               final RedirectAttributes redirectAttributes,
                               Principal principal) throws ChangeSetPersister.NotFoundException {
        log.info("IN generateLink PRINCIPAL = " + principal.getName());

        User existingUser = userService.findUserByEmail(principal.getName())
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        Link link = new Link();
        link.setUrl(url);
        link.setUser(existingUser);
        linkService.createLink(link);
        redirectAttributes.addFlashAttribute("message", "Link created successfully");
        return REDIRECT_SHORTIFY_HOME_USER;
    }

    @GetMapping("/home_guest")
    public ModelAndView getAllLinks() {
        ModelAndView result = new ModelAndView("/home_guest");
        result.addObject("linkList", linkService.getAllLinks());
        return result;
    }

    @GetMapping("/home_user")
    public ModelAndView showHomeUserPage(Principal principal) {
        log.info("IN showHomeUserPage. PRINCIPAL = " + principal.getName());
        User user = userService.findUserByEmail(principal.getName()).orElseThrow();
        ModelAndView result = new ModelAndView("/home_user");
        List<Link> linkList = user.getLinks();
        result.addObject("linkList", linkList);
        return result;
    }

    @GetMapping("/about")
    public ModelAndView showAboutPage() {
        return new ModelAndView("/about");
    }

    @GetMapping("/thanks_to")
    public ModelAndView showThanksToPage() {
        return new ModelAndView("/thanks_to");
    }

    @GetMapping("/redirect/{shortLink}")
    public void redirect(@PathVariable(name = "shortLink") String shortLink, HttpServletResponse response) throws IOException {
        linkService.redirect(shortLink, response);
    }
}
