package edu.ucsb.cs56.ucsb_courses_search.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import edu.ucsb.cs56.ucsb_courses_search.entity.Course;
import edu.ucsb.cs56.ucsb_courses_search.repository.CourseRepository;

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
    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FILE_PATH = "/localhost.json";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private Logger logger = LoggerFactory.getLogger(GoogleCalendarController.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    public GoogleCalendarController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/GoogleCalendar")
    //<button type="submit" th:formaction="@{/searchResults}" class="btn btn-primary" id="js-course-search-submit">Find Courses</button> use this format to create button in templates/courseschedule/index.html
    //function goes here
    public String exportToGoogleCalendar(Model model, OAuth2AuthenticationToken token) {
        logger.info("Inside /GoogleCalendar controller method GoogleCalendarController#exportToGoogleCalendar");
        if (token!=null) {
            Iterable<Course> myclasses = getClassesFromRepo(token);
            try{
                createGoogleCalendar();
            } catch (IOException e) {
                System.out.println("IOException caught");
            } catch (GeneralSecurityException e) {
                System.out.println("GeneralSecurityException caught");
            }
        }
        //if not logged in, handle emptyList case from CourseController
        return "courseschedule/index";
    }

    private Iterable<Course> getClassesFromRepo(OAuth2AuthenticationToken token){
        String uid = token.getPrincipal().getAttributes().get("sub").toString();
        logger.info("uid="+uid);
        logger.info("courseRepository="+courseRepository);
        Iterable<Course> myclasses = courseRepository.findByUid(uid);
        return myclasses;
    }

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = GoogleCalendarController.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8080).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }


    private void createGoogleCalendar() throws IOException, GeneralSecurityException{
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
        .setApplicationName(APPLICATION_NAME)
        .build();
    }
    
    

    
}