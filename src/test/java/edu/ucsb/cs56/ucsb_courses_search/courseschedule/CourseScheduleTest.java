package edu.ucsb.cs56.ucsb_courses_search.courseschedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import edu.ucsb.cs56.ucsb_courses_search.controller.advice.AuthControllerAdvice;
import edu.ucsb.cs56.ucsb_courses_search.BootstrapTestHelper;
import edu.ucsb.cs56.ucsb_courses_search.NavigationTestHelper;
import edu.ucsb.cs56.ucsb_courses_search.controller.CourseController;
import edu.ucsb.cs56.ucsb_courses_search.entity.ScheduleItem;
import edu.ucsb.cs56.ucsb_courses_search.repository.ScheduleItemRepository;
import edu.ucsb.cs56.ucsb_courses_search.repository.ScheduleRepository;
import edu.ucsb.cs56.ucsb_courses_search.service.FeatureToggleService;
import edu.ucsb.cs56.ucsb_courses_search.service.MembershipService;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import java.util.List;
import java.util.ArrayList;

import edu.ucsb.cs56.ucsb_courses_search.OAuthUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

import org.junit.Before;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@WebMvcTest(CourseController.class)
@WebAppConfiguration
public class CourseScheduleTest {

    private Logger logger = LoggerFactory.getLogger(CourseScheduleTest.class);

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthControllerAdvice aca;

    @MockBean
    private ClientRegistrationRepository crr;

    @MockBean
    private ScheduleItemRepository mockScheduleItemRepository;

    @MockBean
    private ScheduleRepository mockScheduleRepository;

    @MockBean
    private MembershipService mockMembershipService;

    @MockBean
    private FeatureToggleService mockFeatureToggleService;

    private Authentication mockAuthentication;

    private static final String URL = "/courseschedule";

    /**
     * Set up an OAuth mock user so that we can unit test protected endpoints
     */
    @Before
    public void setUp() {
        OAuth2User principal = OAuthUtils.createOAuth2User("Chris Gaucho", "cgaucho@ucsb.edu");
        mockAuthentication = OAuthUtils.getOauthAuthenticationFor(principal);
        List<ScheduleItem> emptyScheduleItemList = new ArrayList<ScheduleItem>();
        when(mockScheduleItemRepository.findAll()).thenReturn(emptyScheduleItemList);
	    when(mockMembershipService.isMember((OAuth2AuthenticationToken) mockAuthentication)).thenReturn(true);
    }

    @Test
    @WithMockUser
    public void getCourseSchedule_ContentType() throws Exception {
        logger.info("mockAuthentication=" + mockAuthentication);
        mvc.perform(MockMvcRequestBuilders.get("/courseschedule").with(authentication(mockAuthentication))
                .accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser
    public void getCourseSchedule_BootstrapLoaded() throws Exception {
        BootstrapTestHelper.bootstrapIsLoaded(mvc, URL, mockAuthentication);
    }

    @Test
    @WithMockUser
    public void getCourseSchedule_hasCorrectTitle() throws Exception {
        mvc.perform(
                MockMvcRequestBuilders.get(URL).accept(MediaType.TEXT_HTML).with(authentication(mockAuthentication)))
                .andExpect(status().isOk()).andExpect(xpath("//title").exists())
                .andExpect(xpath("//title").string("My Class Schedule"));
    }

    @Test
    @WithMockUser
    public void getCourseSchedule_hasNavBar() throws Exception {
        NavigationTestHelper.hasNavBar(mvc, URL, mockAuthentication);
    }

    @Test
    @WithMockUser
    public void getCourseSchedule_hasFooter() throws Exception {
        NavigationTestHelper.hasFooter(mvc, URL, mockAuthentication);
    }

}
