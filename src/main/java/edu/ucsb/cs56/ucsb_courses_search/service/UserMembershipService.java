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
import java.util.List;
import java.util.ArrayList;

/**
 * Service object that wraps the UCSB Academic Curriculum API
 */
@Service
public class UserMembershipService implements MembershipService {

    private Logger logger = LoggerFactory.getLogger(UserMembershipService.class);

    @Value("${app.admin.emails:}")
    final private List<String> adminEmails = new ArrayList<String>();

    @Value("${app.member.hosted-domain}")
    final private String memberHostedDomain = "ucsb.edu";

    @Autowired
    private OAuth2AuthorizedClientService clientService;

    /**
     * @param token OAuth token
     * @return true if current logged-in user is a member but not an admin
     */
    public boolean isMember(OAuth2AuthenticationToken token) {
        return hasRole(token, "member");
    }

    /**
     * @param token OAuth token
     * @return true if current logged-in user is an Admin
     */

    public boolean isAdmin(OAuth2AuthenticationToken token) {
        return hasRole(token, "admin");
    }

    /**
     * does current logged in user have this role
     *
     * @param token      OAuth token
     * @param roleToTest "member" or "admin" (lowercase)
     * @return if the current logged in user has that role
     */

    public boolean hasRole(OAuth2AuthenticationToken token, String roleToTest) {

        logger.info("adminEmails=[" + adminEmails + "]");

        if (token == null) {
            return false;
        }
        if (clientService == null) {
            logger.error(String.format("unable to obtain autowired clientService"));
            return false;
        }

        OAuth2User oAuth2User = token.getPrincipal();

        String email = (String) oAuth2User.getAttributes().get("email");
        // hd is the domain of the email, e.g. ucsb.edu
        String hostedDomain = (String) oAuth2User.getAttributes().get("hd");

        logger.info("email=[" + email + "]");
        logger.info("hostedDomain=" + hostedDomain);
	logger.info("memberhosteddomain=" + memberHostedDomain);

        if (roleToTest.equals("admin") && isAdminEmail(email)) {
            return true;
        }

        if (roleToTest.equals("member") && memberHostedDomain.equals(hostedDomain)) {
            return true;
        }

        return false;   
    }

    private boolean isAdminEmail(String email)
    {
    	return adminEmails.get(0).contains(email);
    }
}

