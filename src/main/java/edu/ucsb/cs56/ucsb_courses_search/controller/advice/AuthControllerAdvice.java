package edu.ucsb.cs56.ucsb_courses_search.controller.advice;

import edu.ucsb.cs56.ucsb_courses_search.service.MembershipService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class AuthControllerAdvice {

    @Autowired
    private MembershipService membershipService;

    @ModelAttribute("isLoggedIn")
    public boolean getIsLoggedIn(OAuth2AuthenticationToken token){
        return token != null;
    }

    @ModelAttribute("id")
    public String getUid(OAuth2AuthenticationToken token){
        if (token == null) return "";
        //for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : token.getPrincipal().getAttributes().entrySet()) {
        //    System.out.println(entry.getKey() + ":" + entry.getValue().toString());
        //}
        return token.getPrincipal().getAttributes().get("sub").toString();
    }

    @ModelAttribute("login")
    public String getLogin(OAuth2AuthenticationToken token){
        if (token == null) return "";

        return token.getPrincipal().getAttributes().get("login").toString();
    }

    @ModelAttribute("isMember")
    public boolean getIsMember(OAuth2AuthenticationToken token){
        return membershipService.isMember(token);
    }
    @ModelAttribute("isAdmin")
    public boolean getIsAdmin(OAuth2AuthenticationToken token){
        return membershipService.isAdmin(token);
    }

    @ModelAttribute("role")
    public String getRole(OAuth2AuthenticationToken token){
        return membershipService.role(token);
    }
}
