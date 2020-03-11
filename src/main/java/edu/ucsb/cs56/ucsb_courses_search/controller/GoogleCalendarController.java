package edu.ucsb.cs56.ucsb_courses_search.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.OAuth2Credentials;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import edu.ucsb.cs56.ucsb_courses_search.repository.ScheduleItemRepository;
import edu.ucsb.cs56.ucsb_courses_search.entity.ScheduleItem;

@Controller
public class GoogleCalendarController {
    private static final String APPLICATION_NAME = "UCSB Courses Search Google Calendar Export";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    
    private OAuth2AuthorizedClientService clientService;
    
    @Autowired
    private ScheduleItemRepository scheduleItemRepository;

    @Autowired
    public GoogleCalendarController(ScheduleItemRepository scheduleItemRepository, OAuth2AuthorizedClientService clientService) {
        this.clientService = clientService;
        this.scheduleItemRepository = scheduleItemRepository;
    }
    private Logger logger = LoggerFactory.getLogger(GoogleCalendarController.class);


    @GetMapping("/GoogleCalendar")
    public String createGoogleCalendar(OAuth2AuthenticationToken token) throws IOException, GeneralSecurityException{
        String uid = token.getPrincipal().getAttributes().get("sub").toString();
        Iterable<ScheduleItem> myclasses = scheduleItemRepository.findByUid(uid);
        OAuth2AuthorizedClient client =
             clientService.loadAuthorizedClient(
                 token.getAuthorizedClientRegistrationId(),
                 token.getName());
        String stringAccessToken = client.getAccessToken().getTokenValue();
        Date expirationTime = Date.from(client.getAccessToken().getExpiresAt());
        AccessToken accessToken = new AccessToken(stringAccessToken, expirationTime);
        OAuth2Credentials oAuth2Credential = OAuth2Credentials.create(accessToken);
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(oAuth2Credential);

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, requestInitializer)
                .setApplicationName(APPLICATION_NAME).build();
        logger.info("Calendar built");

        for(ScheduleItem c: myclasses){
            Event event = new Event();
            logger.info("Event instantiated");
            event.setSummary(c.getClassname());
            logger.info("Event summary set");
            event.setLocation(c.getLocation());
            logger.info("Event location set");
            event.setStart(getStart(c));
            logger.info("Event start date set");
            event.setEnd(getEnd(c));
            logger.info("Event end date set");
            event.setRecurrence(Arrays.asList(getRecurrenceStr(c)));
            logger.info("Event recurrence set");
            Event classEvent = service.events().insert("primary", event).execute();
            logger.info("Event ID: " + classEvent.getId());
        }
        return "redirect:/courseschedule";
    }

    private EventDateTime getStart(ScheduleItem c){
        String timeStr = getRfc3339(c, true);
        DateTime start = DateTime.parseRfc3339(timeStr);
        return new EventDateTime().setDateTime(start).setTimeZone("America/Los_Angeles");
    }

    private EventDateTime getEnd(ScheduleItem c){
        String timeStr = getRfc3339(c, false);
        DateTime end = DateTime.parseRfc3339(timeStr);
        return new EventDateTime().setDateTime(end).setTimeZone("America/Los_Angeles");
    }

    private String getRfc3339(ScheduleItem c, boolean isStart){
        String time = "";
        if(c.getQuarter().contains("S20")){
            if(c.getMeetday().contains("M")){
                time += "2020-03-30";
            }
            else if(c.getMeetday().contains("T")){
                time += "2020-03-31";
            }
            else if(c.getMeetday().contains("W")){
                time += "2020-04-01";
            }
            else if(c.getMeetday().contains("R")){
                time += "2020-04-02";
            }
            else{
                time += "2020-04-03";
            }
        }
        time += "T";
        if(isStart)
            time = addStartTime(c, time);
        else
            time = addEndTime(c, time);
        time += ".000-07:00";
        return time;
    }

    private String addStartTime(ScheduleItem c, String time){
        time += c.getMeettime().substring(0, c.getMeettime().indexOf("-"));
        time += ":00";
        return time;
    }

    private String addEndTime(ScheduleItem c, String time){
        time += c.getMeettime().substring(c.getMeettime().indexOf("-") + 1);
        time += ":00";
        return time;
    }

    private String getRecurrenceStr(ScheduleItem c){
        String recStr = "RRULE:FREQ=WEEKLY;BYDAY=";
        recStr = addDays(c, recStr);
        recStr = addUntil(c, recStr);
        return recStr;
    }

    private String addDays(ScheduleItem c, String recStr){
        boolean isMultipleDays = false;
        if(c.getMeetday().contains("M")){
            recStr += "MO";
            isMultipleDays = true;
        }
        if(c.getMeetday().contains("T")){
            if(isMultipleDays)
                recStr += ",";
            recStr += "TU";
            isMultipleDays = true;
        }
        if(c.getMeetday().contains("W")){
            if(isMultipleDays)
                recStr += ",";
            recStr += "WE";
            isMultipleDays = true;
        }
        if(c.getMeetday().contains("R")){
            if(isMultipleDays)
                recStr += ",";
            recStr += "TH";
            isMultipleDays = true;
        }
        if(c.getMeetday().contains("F")){
            if(isMultipleDays)
                recStr += ",";
            recStr += "FR";
        }
        return recStr;
    }

    private String addUntil(ScheduleItem c, String recStr){
        recStr += ";UNTIL=";
        if(c.getQuarter().contains("S20")){
            recStr += "20200605";
        }
        recStr += "T170000Z";
        return recStr;
    }
}