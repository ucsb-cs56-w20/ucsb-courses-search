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
        if (oauthToken == null) {
            return false;
        }
        OAuth2User oAuth2User = oauthToken.getPrincipal();
        String user = (String) oAuth2User.getAttributes().get("email");


        if (clientService==null) {
            logger.error(String.format("unable to obtain autowired clientService"));
            return false;
        }
        OAuth2AuthorizedClient client = clientService
                .loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());

        if (client==null) {
            logger.info(String.format("clientService was not null but client returned was null for user %s",user));
            return false;
        }

        OAuth2AccessToken token = client.getAccessToken();

        if (token==null) {
            logger.info(String.format("client for %s was not null but getAccessToken returned null",user));
            return false;
        }
        String accessToken = token.getTokenValue();
        if (accessToken==null) {
            logger.info(String.format("token was not null but getTokenValue returned null for user %s",user));
            return false;
        }

	// STUB
        return false;
    }

}
