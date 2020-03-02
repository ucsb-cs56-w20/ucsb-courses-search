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

    /**
     * Is the currently logged in user a member of the UCSB Gsuite
     *
     * @param oAuth2AuthenticationToken the current user's authentication token
     * @return true if the user is a member of the UCSB GSuite, false otherwise
     */ 
    public boolean isMember(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
	// Per Professor Conrad's request (see https://github.com/ucsb-cs56-w20/ucsb-courses-search/pull/199, bottom),
	// a member will be anyone with a UCSB email address
	String userEmail = oAuth2AuthenticationToken.getPrincipal().getAttributes().get("email").toString();
	if(userEmail.endsWith("@ucsb.edu") || userEmail.endsWith("@umail.ucsb.edu"))
	{
		return true;
	}
	return false;
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

	if(roleToTest == "member")
	{
		return isMember(oauthToken);
	}
        return false;
    }

}
