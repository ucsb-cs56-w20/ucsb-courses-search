package edu.ucsb.cs56.ucsb_courses_search.service;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public interface MembershipService {

    /** is current logged in user an admin for the website */
    public boolean isAdmin(OAuth2AuthenticationToken oAuth2AuthenticationToken);

    /** is current logged in user a member of the UCSB GSuite and able to log in */
    public boolean isMember(OAuth2AuthenticationToken oAuth2AuthenticationToken);

    /**
     * Return the current user's role on the site.
     * Note that if the user is both an admin and a member, this function will return admin
     */ 
    default public String role(OAuth2AuthenticationToken token) {
       if (token==null)
            return "Guest";
       if (isAdmin(token))
            return "Admin";
       if(isMember(token))
	    return "Member";
       return "Guest";
    }

}
