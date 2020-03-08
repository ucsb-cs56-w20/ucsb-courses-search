package edu.ucsb.cs56.ucsb_courses_search.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import edu.ucsb.cs56.ucsb_courses_search.model.ClientCredentials;

@Service
public class GoogleCalendarService {

    private static final String APPLICATION_NAME = "UCSB Courses Search Google Calendar Export";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    // private static final String CREDENTIALS_FILE_PATH = "../../localhost.json";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    // private ClientCredentials clientCredentials = new ClientCredentials();
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;
    private Iterable<Course> myclasses;
    private String email;

    private Logger logger = LoggerFactory.getLogger(GoogleCalendarService.class);

    // Must be called before createGoogleCalendar()
    public void initialize(Iterable<Course> myclasses, String email) {
        this.myclasses = myclasses;
        this.email = email;
    }

    public static Credential createCredentialWithAccessTokenOnly(TokenResponse tokenResponse) {
        return new Credential(BearerToken.authorizationHeaderAccessMethod()).setFromTokenResponse(tokenResponse);
    }

    public Credential createCredentialWithRefreshToken(HttpTransport transport, JsonFactory jsonFactory,
            TokenResponse tokenResponse) {
        logger.info("Client ID: " + clientId);
        logger.info("Client Secret: " + clientSecret);
        return new Credential.Builder(BearerToken.authorizationHeaderAccessMethod()).setTransport(transport)
                .setJsonFactory(jsonFactory).setTokenServerUrl(null)
                .setClientAuthentication(new BasicAuthentication(clientId, clientSecret)).build()
                .setFromTokenResponse(tokenResponse);
    }

    // Precondition: Must call setClasses(Iterable<Course> myclasses) before calling
    // this function
    public void createGoogleCalendar() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = createCredentialWithRefreshToken(HTTP_TRANSPORT, JSON_FACTORY, new TokenResponse().setScope("CalendarScopes.CALENDAR"));
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME).build();
        logger.info("Calendar built");

        ArrayList<EventAttendee> attendees = new ArrayList<EventAttendee>();
        attendees.add(new EventAttendee().setEmail(email));

        for(Course c: myclasses){
            Event event = new Event();
            event.setSummary(c.getClassname());
            event.setLocation(c.getLocation());
            event.setAttendees(attendees);

            //Test
            getXsDT(c);
        }
    }

    private EventDateTime getStart(Course c){
        String xsDT = getXsDT(c);
        return new EventDateTime();
    }

    private String getXsDT(Course c){
        String xsDT = "";
        logger.info("Assoc Lec Time: " + c.getAssociatedLectureTime());
        if(c.getQuarter().contains("S20")){
            if(c.getAssociatedLectureDay().contains("M")){
                xsDT += "2020-03-30";
            }
            else if(c.getAssociatedLectureDay().contains("T")){
                xsDT += "2020-03-31";
            }
            else if(c.getAssociatedLectureDay().contains("W")){
                xsDT += "2020-04-01";
            }
            else if(c.getAssociatedLectureDay().contains("R")){
                xsDT += "2020-04-02";
            }
            else{
                xsDT += "2020-04-03";
            }
        }
        xsDT += "T";
        xsDT += c.getAssociatedLectureTime();
        return xsDT;
    }
}