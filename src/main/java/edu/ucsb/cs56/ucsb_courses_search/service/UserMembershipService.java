package edu.ucsb.cs56.ucsb_courses_search.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


/**
 * Service object that wraps the UCSB Academic Curriculum API
 */
@Service
public class UserMembershipService implements MembershipService {

    private Logger logger = LoggerFactory.getLogger(UserMembershipService.class);


    @Autowired
    private OAuth2AuthorizedClientService clientService;

    public UserMembershipService()
    {
    }

    public boolean isAdmin(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        return hasRole(oAuth2AuthenticationToken, "admin");
    }

    /**
     * is current logged in user has role
     *
     * @param roleToTest "member" or "admin"
     * @return if the current logged in user has that role
     */

    public boolean hasRole(OAuth2AuthenticationToken oauthToken, String roleToTest) {
    	// The G Suite's membership concept is not useful for this app (since none of us is likely to be an admin on the ucsb.edu domain)
	// Thus, for now, I've left this function to return false every time
	// This will make it so every user has the role "Guest".
        return false;
    }

}
