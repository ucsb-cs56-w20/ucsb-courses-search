package edu.ucsb.cs56.ucsb_courses_search.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

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

import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeCallbackServlet;

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
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.fluent.Form;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.collect.ImmutableMap;

@Service
@WebServlet(urlPatterns = "/courseschedule/callback")
public class GoogleCalendarCallbackService extends AbstractAuthorizationCodeCallbackServlet {
    private static final String APPLICATION_NAME = "UCSB Courses Search Google Calendar Export";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    // private static final String CREDENTIALS_FILE_PATH = "../../localhost.json";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    // private ClientCredentials clientCredentials = new ClientCredentials();
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    private Logger logger = LoggerFactory.getLogger(GoogleCalendarService.class);

    @Override
    protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
      GenericUrl url = new GenericUrl(req.getRequestURL().toString());
      url.setRawPath("/courseschedule/callback");
      return url.build();
    }

    @Override
    protected void onSuccess(HttpServletRequest req, HttpServletResponse resp, Credential credential)
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
                logger.info(code);
                /*LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
                params.put("code", code);
                params.put("client_id", clientId);
                params.put("client_secret", clientSecret);
                params.put("redirect_uri", "http://localhost:8080/courseschedule/callback");
                params.put("grant_type", "authorization_code");*/
                HttpResponse response = Request.Post("https://oauth2.googleapis.com/token").bodyForm(
                    Form.form().add("code", code).add("client_id", clientId).add("client_secret", clientSecret).add("redirect_uri", "http://localhost:8080/courseschedule/callback").add("grant_type", "authorization_code").build())
                    .execute().returnResponse(); 
                HttpEntity entity = response.getEntity();
                String body = EntityUtils.toString(entity);
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new RuntimeException("Expected 200 but got " + response.getStatusLine().getStatusCode() + ", with body " + body);
                }
               
                // get the access token by post to Google
                //String body = post("https://accounts.google.com/o/oauth2/token", params);
                //logger.info(body);

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
            logger.info("email= {0} ,name={1}", new Object[]{email, fullName}); 
            //do the rest stuff
            }
        }

    @Override
    protected String getUserId(HttpServletRequest req) throws ServletException, IOException {
        return "";
    }

    @Override
    protected AuthorizationCodeFlow initializeFlow() throws IOException {
        return new GoogleAuthorizationCodeFlow.Builder(
            new NetHttpTransport(), JSON_FACTORY,
            clientId, clientSecret,
            Collections.singleton(CalendarScopes.CALENDAR)).setDataStoreFactory(
                new FileDataStoreFactory(new java.io.File("tokens"))).setAccessType("offline").build();
    }




    public String get(String url) throws ClientProtocolException, IOException {
        return execute(new HttpGet(url));
    }
        
        // makes a POST request to url with form parameters and returns body as a string
    public String post(String url, Map<String,String> formParameters) throws ClientProtocolException, IOException { 
        HttpPost request = new HttpPost(url);
            
        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            
        for (String key : formParameters.keySet()) {
            nvps.add(new BasicNameValuePair(key, formParameters.get(key))); 
        }
        
        request.setEntity(new UrlEncodedFormEntity(nvps));
            
        return execute(request);
    }
        
        // makes request and checks response code for 200
    private String execute(HttpRequestBase request) throws ClientProtocolException, IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(request);
            
        HttpEntity entity = response.getEntity();
            String body = EntityUtils.toString(entity);
        
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Expected 200 but got " + response.getStatusLine().getStatusCode() + ", with body " + body);
        }
        
        return body;
    }
}