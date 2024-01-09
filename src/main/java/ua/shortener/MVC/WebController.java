package ua.shortener.MVC;

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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Controller
@RequiredArgsConstructor
@RequestMapping("/shortify")
@Slf4j
public class WebController {
    private final LinkService linkService;
    private final UserService userService;
    private final AuthenticationServiceImpl authenticationService;

    @GetMapping("/register")
    public ModelAndView showRegistrationPage(@ModelAttribute("registrationRequest") RegistrationRequest registrationRequest){
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
        }else {
            authenticationService.createUserFromRequest(registrationRequest);
            return "success";
        }
    }
//    @PostMapping("/login")
//    public String loginUser(@ModelAttribute("loginRequest") LoginRequest loginRequest,
//                            Model model,
//                            HttpServletResponse response) {
//        log.info("IN POST LOGIN USERNAME = " + loginRequest.getEmail() + ", PASSWORD = " + loginRequest.getPassword());
//        return "redirect:/shortify/home_user";
//    }
    @PostMapping("/delete")
    public String deleteLink(@RequestParam(name = "shortLink") String shortLink){
        linkService.deleteLink(shortLink);
        return "redirect:/shortify/home_user";
    }

    @GetMapping("/home_user/example")
    public String showQRcodePage(){
        return "example_video";
    }
    @PostMapping("/home_user/generate")
    public String generateLink(@RequestParam("url") String url, RedirectAttributes redirectAttributes ) throws ChangeSetPersister.NotFoundException {
        User existingUser = userService.getUserById(1L)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        Link link = new Link();
        link.setUrl(url);
        link.setUser(existingUser);
        linkService.createLink(link);
        redirectAttributes.addFlashAttribute("message", "Link created successfully");
        return "redirect:/shortify/home_user";
    }

    @GetMapping("/home_guest")
    public ModelAndView getAllLinks(){
        ModelAndView result = new ModelAndView("/home_guest");
        result.addObject("linkList", linkService.getAllLinks());
        return result;
    }

    @GetMapping("/home_user")
    public ModelAndView showHomeUserPage(){
        ModelAndView result = new ModelAndView("/home_user");
        List<Link> linkList = linkService.getAllLinks();
        result.addObject("linkList", linkList);
        return result;
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

//if(parameterMap.keySet().contains("logout")){
//        log.info("DO LOGOUT COOKIE WILL BE CHANGE TO DEPRECATE ONE");
//        String oldToken = jwtTokenCookie.get().getValue();
//        String invalidToken = jwtService.generateTokenInvalidToken(oldToken);
//        Cookie cookie = new Cookie("jwtToken", invalidToken);
//        response.addCookie(cookie);
//        filterChain.doFilter(request, response);
//        return;
//        }