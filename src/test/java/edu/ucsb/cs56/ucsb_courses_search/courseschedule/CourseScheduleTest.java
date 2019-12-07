package edu.ucsb.cs56.ucsb_courses_search.courseschedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import edu.ucsb.cs56.ucsb_courses_search.AuthControllerAdvice;
import edu.ucsb.cs56.ucsb_courses_search.BootstrapTestHelper;
import edu.ucsb.cs56.ucsb_courses_search.NavigationTestHelper;
import edu.ucsb.cs56.ucsb_courses_search.controllers.CourseController;
import edu.ucsb.cs56.ucsb_courses_search.entities.Course;
import edu.ucsb.cs56.ucsb_courses_search.repositories.CourseRepository;

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

    private Authentication mockAuthentication;

    private static CourseRepository mockCourseRepository;

    private static final String URL = "/courseschedule";

    /**
     * This configures the test to ONLY load one controller, which means that there
     * are far fewer beans that have to be mocked and stubbed. See
     * https://stackoverflow.com/a/45228072 plus the comments underneath about
     * Configuration.
     */
    @Configuration
    public static class TestConf {
        @Bean
        public CourseController courseController() {
            // This sets up findAll to always return an empty list.
            // Eventually, we'll probably want it to actually have some data in it.
            List<Course> emptyCourseList = new ArrayList<Course>();
            mockCourseRepository = mock(CourseRepository.class);
            when(mockCourseRepository.findAll()).thenReturn(emptyCourseList);
            return new CourseController(mockCourseRepository);
        }
    }

    /**
     * Set up an OAuth mock user so that we can unit test protected endpoints
     */
    @Before
    public void setUp() {
        OAuth2User principal = OAuthUtils.createOAuth2User("Chris Gaucho", "cgaucho@example.com");
        mockAuthentication = OAuthUtils.getOauthAuthenticationFor(principal);

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