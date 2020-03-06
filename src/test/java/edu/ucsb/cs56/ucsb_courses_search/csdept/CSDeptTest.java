package edu.ucsb.cs56.ucsb_courses_search.csdept;

import edu.ucsb.cs56.ucsb_courses_search.service.QuarterListService;
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
import edu.ucsb.cs56.ucsb_courses_search.controller.CSDeptController;
import edu.ucsb.cs56.ucsb_courses_search.entity.Course;
import edu.ucsb.cs56.ucsb_courses_search.repository.CourseRepository;
import edu.ucsb.cs56.ucsb_courses_search.service.CurriculumService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import java.util.List;
import java.util.ArrayList;

import edu.ucsb.cs56.ucsb_courses_search.OAuthUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

import org.junit.Before;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@WebMvcTest(CSDeptController.class)
@WebAppConfiguration
public class CSDeptTest {

    private Logger logger = LoggerFactory.getLogger(CSDeptTest.class);

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthControllerAdvice aca;

    @MockBean
    private ClientRegistrationRepository crr;

    @MockBean
    private CourseRepository mockCourseRepository;

    @MockBean
    private CurriculumService mockCurriculumService;

    @MockBean
    private QuarterListService mockQuarterListService;

    private Authentication mockAuthentication;

    private static final String URL = "/csdept/search/classroom";

    /**
     * Set up an OAuth mock user so that we can unit test protected endpoints
     */
    @Before
    public void setUp() {
        OAuth2User principal = OAuthUtils.createOAuth2User("Chris Gaucho", "cgaucho@example.com");
        mockAuthentication = OAuthUtils.getOauthAuthenticationFor(principal);
        List<Course> emptyCourseList = new ArrayList<Course>();
        when(mockCourseRepository.findAll()).thenReturn(emptyCourseList);
    }

    @Test
    @WithMockUser
    public void getClassroomSearch_ContentType() throws Exception {
        logger.info("mockAuthentication=" + mockAuthentication);
        mvc.perform(MockMvcRequestBuilders.get(URL).with(authentication(mockAuthentication))
                .accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser
    public void getClassroomSearch_BootstrapLoaded() throws Exception {
        BootstrapTestHelper.bootstrapIsLoaded(mvc, URL, mockAuthentication);
    }

    @Test
    @WithMockUser
    public void getClassroomSearch_hasCorrectTitle() throws Exception {
        mvc.perform(
                MockMvcRequestBuilders.get(URL).accept(MediaType.TEXT_HTML).with(authentication(mockAuthentication)))
                .andExpect(status().isOk()).andExpect(xpath("//title").exists())
                .andExpect(xpath("//title").string("Search For Courses in CS Department Classrooms"));
    }

    @Test
    @WithMockUser
    public void getClassroomSearch_hasNavBar() throws Exception {
        NavigationTestHelper.hasNavBar(mvc, URL, mockAuthentication);
    }

    @Test
    @WithMockUser
    public void getClassroomSearch_hasFooter() throws Exception {
        NavigationTestHelper.hasFooter(mvc, URL, mockAuthentication);
    }

}