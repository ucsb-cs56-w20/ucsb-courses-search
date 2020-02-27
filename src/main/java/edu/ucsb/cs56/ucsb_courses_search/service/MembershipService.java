package edu.ucsb.cs56.ucsb_courses_search.service;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public interface MembershipService {

    /** is current logged in user a member of the github org */
    public boolean isAdmin(OAuth2AuthenticationToken oAuth2AuthenticationToken);

    default public String role(OAuth2AuthenticationToken token) {
        if (token==null)
            return "Guest";
       if (isAdmin(token))
            return "Admin";
        return "Guest";
    }

}
