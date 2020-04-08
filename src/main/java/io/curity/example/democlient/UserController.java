package io.curity.example.democlient;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/user")
    public String user(Model model,
                        @AuthenticationPrincipal OidcUser oidcUser) {
        model.addAttribute("userName", oidcUser.getName());
        model.addAttribute("audience", oidcUser.getAudience());
        return "user";
    }
}


