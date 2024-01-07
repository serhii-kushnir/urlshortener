package ua.shortener.MVC;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
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

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/shortify")
public class WebController {
    private final LinkService linkService;
    private final UserService userService;
    private final AuthenticationServiceImpl authenticationService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public ModelAndView showRegistrationPage(@ModelAttribute("registrationRequest") RegistrationRequest registrationRequest){
        return new ModelAndView("/register");
    }
    @GetMapping("/hello")
    public ModelAndView showLoginPage(){
        return new ModelAndView("/login");
    }

    @PostMapping("/signup")
    public String registerUser(@Valid RegistrationRequest registrationRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "register";
        }else {
            authenticationService.createUserFromRequest(registrationRequest);
            return "success";
        }
    }

    @PostMapping("/signin")
    public String loginUser(@RequestParam("email") String email,
                            @RequestParam("password") String password) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);

        try {
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "redirect:/shortify/home_user"; // Успішний вхід
        } catch (AuthenticationException e) {
            return "redirect:/shortify/welcome?error=true"; // Невдалий вхід
        }
    }

//    @PostMapping("/signin")
//    public String loginUser(@RequestParam("email") String email,
//                            @RequestParam("password") String password,
//                        RedirectAttributes redirectAttributes) {
//        Optional<User> user = userService.findUserByEmail(email);
//
//    if (user.isPresent()) {
//        User foundUser = user.get();
//        if (passwordEncoder.matches(password, foundUser.getPassword())) {
//            return "redirect:/shortify/home_user";
//        }
//    }
//    redirectAttributes.addFlashAttribute("error", "Invalid email or password");
//    return "redirect:/shortify/welcome";
//    }

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
