package edu.ucsb.cs56.ucsb_courses_search.controller;

import edu.ucsb.cs56.ucsb_courses_search.model.result.MySearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Map;
import java.util.HashMap;

@Controller()
public class WebController {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("searchObject", new MySearchResult());
        return "index";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model, OAuth2AuthenticationToken oAuth2AuthenticationToken) {

        Map<String, String> urls = new HashMap<>();

        // get around an unfortunate limitation of the API
	// NOTE: one of the two links created using this data is the login with Github link on the /login page. We want to remove this link,
	// so we should try to remove that from the database later on
        @SuppressWarnings("unchecked") Iterable<ClientRegistration> iterable = ((Iterable<ClientRegistration>) clientRegistrationRepository);
        iterable.forEach(clientRegistration -> urls.put(clientRegistration.getClientName(),
                "/oauth2/authorization/" + clientRegistration.getRegistrationId()));

        model.addAttribute("urls", urls);
        return "login";
    }
/*
    @GetMapping("/page1")
    public String getPage1(Model model, OAuth2AuthenticationToken oAuth2AuthenticationToken) {

        return "page1";
    }

    @GetMapping("/page2")
    public String getPage2(Model model, OAuth2AuthenticationToken oAuth2AuthenticationToken) {

        return "page2";
    }
*/
}
