package ua.shortener.MVC;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.shortener.link.Link;
import ua.shortener.link.service.LinkService;
import ua.shortener.security.auth.AuthenticationServiceImpl;
import ua.shortener.security.auth.dto.registration.RegistrationRequest;
import ua.shortener.user.User;
import ua.shortener.user.service.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/shortify")
public class WebController {
    private final LinkService linkService;
    private final UserService userService;
    private final AuthenticationServiceImpl authenticationService;

    @GetMapping("/home_guest")
    public ModelAndView getAllLinks(){
        ModelAndView result = new ModelAndView("/home_guest");
        result.addObject("linkList", linkService.getAllLinks());
        return result;
    }

    @GetMapping("/welcome")
    public ModelAndView showWelcomePage(@ModelAttribute("registrationRequest") RegistrationRequest registrationRequest){
        return new ModelAndView("/login_register");
    }

    @PostMapping("/signup")
    public String saveUser(@Valid RegistrationRequest registrationRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "login_register";
        }else {
            authenticationService.createUserFromRequest(registrationRequest);
            return "success";
        }
    }

    @PostMapping("/home/generate")
    public String generateLink(@RequestParam("url") String url ) throws ChangeSetPersister.NotFoundException {
        User existingUser = userService.getUserById(1L)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        Link link = new Link();
        link.setUrl(url);
        link.setUser(existingUser);
        linkService.createLink(link);
        return "home_user";
    }

    @GetMapping("/about")
    public ModelAndView showAboutPage(){
        return new ModelAndView("/about");
    }

    @GetMapping("/thanks_to")
    public ModelAndView showThanksToPage(){
        return new ModelAndView("/thanks_to");
    }
}
