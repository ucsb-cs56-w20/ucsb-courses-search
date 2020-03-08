package edu.ucsb.cs56.ucsb_courses_search.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import edu.ucsb.cs56.ucsb_courses_search.entity.Course;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import org.apache.http.client.ClientProtocolException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.annotation.PostConstruct;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
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
import com.google.api.services.calendar.model.Events;

import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeServlet;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Executor;
import org.apache.http.impl.client.BasicResponseHandler;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.collect.ImmutableMap;

/*Request.Get("http://targethost/homepage")
    .execute().returnContent();
Request.Post("http://targethost/login")
    .bodyForm(Form.form().add("username",  "vip").add("password",  "secret").build())
    .execute().returnContent();*/

import edu.ucsb.cs56.ucsb_courses_search.model.ClientCredentials;

@Service
@WebServlet(urlPatterns = "/GoogleCalendar/*")
public class GoogleCalendarService extends AbstractAuthorizationCodeServlet{

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

    private Logger logger = LoggerFactory.getLogger(GoogleCalendarService.class);

    // Must be called before createGoogleCalendar()
    public void setClasses(Iterable<Course> myclasses) {
        this.myclasses = myclasses;
    }

    public Credential createCredentialWithRefreshToken(HttpTransport transport, JsonFactory jsonFactory, TokenResponse tokenResponse) {
     return new Credential.Builder(BearerToken.authorizationHeaderAccessMethod()).setTransport(
        transport)
        .setJsonFactory(jsonFactory)
        .setTokenServerUrl(null)
        .setClientAuthentication(new BasicAuthentication(clientId, clientSecret))
        .build()
        .setFromTokenResponse(tokenResponse);
  }

    @Override
    protected AuthorizationCodeFlow initializeFlow() throws IOException {
        return new GoogleAuthorizationCodeFlow.Builder(
            new NetHttpTransport(), JSON_FACTORY,
            clientId, clientSecret,
            Collections.singleton(CalendarScopes.CALENDAR)).setDataStoreFactory(
                new FileDataStoreFactory(new java.io.File("tokens"))).setAccessType("offline").build();
    }

    @Override
    protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
      GenericUrl url = new GenericUrl(req.getRequestURL().toString());
      url.setRawPath("/courseschedule/callback");
      return url.build();
    }

    /*protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
        // google redirects with
        //http://localhost:8089/callback?state=this_can_be_anything_to_help_correlate_the_response%3Dlike_session_id&code=4/ygE-kCdJ_pgwb1mKZq3uaTEWLUBd.slJWq1jM9mcUEnp6UAPFm0F2NQjrgwI&authuser=0&prompt=consent&session_state=a3d1eb134189705e9acf2f573325e6f30dd30ee4..d62c

    // if the user denied access, we get back an error, ex
        // error=access_denied&state=session%3Dpotatoes
        if (req.getParameter("error") != null) {
            resp.getWriter().println(req.getParameter("error"));
             return;
        } else {

        // google returns a code that can be exchanged for a access token
            String code = req.getParameter("code");
            String body = post("https://accounts.google.com/o/oauth2/token", ImmutableMap.<String,String>builder()
                .put("code", code)
                .put("client_id", clientId)
                .put("client_secret", clientSecret)
                .put("redirect_uri", getRedirectUri(req))
                .put("grant_type", "authorization_code"));

        // get the access token by post to Google

    // ex. returns
    //   {
    //       "access_token": "ya29.AHES6ZQS-BsKiPxdU_iKChTsaGCYZGcuqhm_A5bef8ksNoU",
    //       "token_type": "Bearer",
    //       "expires_in": 3600,
    //       "id_token": "eyJhbGciOiJSUzI1NiIsImtpZCI6IjA5ZmE5NmFjZWNkOGQyZWRjZmFiMjk0NDRhOTgyN2UwZmFiODlhYTYifQ.eyJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwiZW1haWxfdmVyaWZpZWQiOiJ0cnVlIiwiZW1haWwiOiJhbmRyZXcucmFwcEBnbWFpbC5jb20iLCJhdWQiOiI1MDgxNzA4MjE1MDIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdF9oYXNoIjoieUpVTFp3UjVDX2ZmWmozWkNublJvZyIsInN1YiI6IjExODM4NTYyMDEzNDczMjQzMTYzOSIsImF6cCI6IjUwODE3MDgyMTUwMi5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsImlhdCI6MTM4Mjc0MjAzNSwiZXhwIjoxMzgyNzQ1OTM1fQ.Va3kePMh1FlhT1QBdLGgjuaiI3pM9xv9zWGMA9cbbzdr6Tkdy9E-8kHqrFg7cRiQkKt4OKp3M9H60Acw_H15sV6MiOah4vhJcxt0l4-08-A84inI4rsnFn5hp8b-dJKVyxw1Dj1tocgwnYI03czUV3cVqt9wptG34vTEcV3dsU8",
    //       "refresh_token": "1/Hc1oTSLuw7NMc3qSQMTNqN6MlmgVafc78IZaGhwYS-o"
    //   }
        JSONObject jsonObject = null;

        // get the access token from json and request info from Google
        try {
            jsonObject = (JSONObject) new JSONParser().parse(body);
        } catch (ParseException e) {
            throw new RuntimeException("Unable to parse json " + body);
        }

        // google tokens expire after an hour, but since we requested offline access we can get a new token without user involvement via the refresh token
        String accessToken = (String) jsonObject.get("access_token");

        // you may want to store the access token in session
        req.getSession().setAttribute("access_token", accessToken);

        // get some info about the user with the access token
        String json = get(new StringBuilder("https://www.googleapis.com/oauth2/v1/userinfo?access_token=").append(accessToken).toString());
        try {
            jsonObject = (JSONObject) new JSONParser().parse(json);
        } catch (ParseException e) {
            throw new RuntimeException("Unable to parse json " + body);
        }

        String email = (String) jsonObject.get("email");
        String fullName = (String) jsonObject.get("name");
        logger.info(this.getClass().getName()).log(Level.INFO, "email= {0} ,name={1}", new Object[]{email, fullName}); 
        //do the rest stuff
        }
    }*/
    @Override
    protected String getUserId(HttpServletRequest req){
        return "";
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException {
        AuthorizationCodeFlow flow = initializeFlow();
        String url = flow.newAuthorizationUrl().setState("xyz")
        .setRedirectUri("http://localhost:8080/courseschedule/callback").build();
        resp.sendRedirect(url);
    } 
    public String getUrl() throws IOException{
        AuthorizationCodeFlow flow = initializeFlow();
        String url = flow.newAuthorizationUrl().setState("xyz")
        .setRedirectUri("http://localhost:8080/courseschedule/callback").build();
        return url;
    }
    // Precondition: Must call setClasses(Iterable<Course> myclasses) before calling
    // this function
    // public void createGoogleCalendar(OAuth2AuthenticationToken token) throws IOException, GeneralSecurityException {
    //     final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    //     AuthorizationCodeFlow flow = initializeFlow();
    //     String url = flow.newAuthorizationUrl().setState("xyz")
    //     .setRedirectUri("http://localhost:8080/courseschedule").build();
    //     logger.info(url);
    //     Credential credential = flow.loadCredential(getUserId(token));
    //     /*if(credential == null){
    //         TokenResponse tRes = flow.newTokenRequest(code).setRedirectUri("http://localhost:8080/login/oauth2/code/google").execute();
    //         credential = flow.createAndStoreCredential(tRes, getUserId(token));
    //     }*/
    //     //Credential credential = createCredentialWithRefreshToken(HTTP_TRANSPORT, JSON_FACTORY, new TokenResponse());
    //     Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
    //             .setApplicationName(APPLICATION_NAME).build();
    //     logger.info("Calendar built");
    // }
}