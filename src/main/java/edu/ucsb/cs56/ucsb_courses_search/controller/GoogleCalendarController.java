package edu.ucsb.cs56.ucsb_courses_search.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Controller
public class GoogleCalendarController {

    private Logger logger = LoggerFactory.getLogger(GoogleCalendarController.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/GoogleCalendar")
    //<button type="submit" th:formaction="@{/searchResults}" class="btn btn-primary" id="js-course-search-submit">Find Courses</button> use this format to create button in templates/courseschedule/index.html
    //function goes here
    public void exportToGoogleCalendar(Model model, OAuth2AuthenticationToken token) {
        if (token!=null) {
            Iterable<Course> myclasses = getClassesFromRepo(token);

        }
        //if not logged in, handle emptyList case from CourseController
    }

    private Iterable<Course> getClassesFromRepo(OAuth2AuthenticationToken token){
        String uid = token.getPrincipal().getAttributes().get("sub").toString();
        logger.info("uid="+uid);
        logger.info("courseRepository="+courseRepository);
        Iterable<Course> myclasses = courseRepository.findByUid(uid);
        return myclasses;
    }

    private void createGoogleCalendar(OAuth2AuthenticationToken token){
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
        .setApplicationName(APPLICATION_NAME)
        .build();
    }
}