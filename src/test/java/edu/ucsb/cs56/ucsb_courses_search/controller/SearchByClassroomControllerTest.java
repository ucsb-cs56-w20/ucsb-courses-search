package edu.ucsb.cs56.ucsb_courses_search.controller;

import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriverBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.web.context.WebApplicationContext;

import edu.ucsb.cs56.ucsb_courses_search.controller.advice.AuthControllerAdvice;

import static org.junit.Assert.assertEquals;

import java.util.List;

import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import org.openqa.selenium.WebDriver;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class SearchByClassroomControllerTest {

    private Logger logger = LoggerFactory.getLogger(SearchByClassroomControllerTest.class);

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private AuthControllerAdvice aca;

    @MockBean
    private OAuth2AuthorizedClientService o2acs;

    @MockBean
    private ClientRegistrationRepository crr;

    private WebClient webClient;
    private WebDriver driver;

    @Before
    public void setup() {
        webClient = MockMvcWebClientBuilder.webAppContextSetup(context).build();
        webClient.setCssErrorHandler(new SilentCssErrorHandler());
        driver = MockMvcHtmlUnitDriverBuilder.webAppContextSetup(context).build();
    }

    @After
    public void teardown() {
        if (driver != null) {
            driver.close();
        }
    }
    
    @Test
    public void test_get_S20_PHELP_3525() throws Exception {
        String url = "http://localhost:8080/search/byclassroom/results?quarter=20202&building=PHELP&classroom=3525";
        HtmlPage htmlPage = webClient.getPage(url);
        List<DomElement> elems = htmlPage.getByXPath("//table/tbody/tr[1]/td[2]");
        assertEquals("CMPSC     8  ",elems.get(0).getTextContent());
    }

    @Test
    public void test_get_S20_PHELP_3526() throws Exception {
        String url = "http://localhost:8080/search/byclassroom/results?quarter=20202&building=PHELP&classroom=3526";
        HtmlPage htmlPage = webClient.getPage(url);
        String textToFind = "CMPSC   160  ";
        String xpath=String.format("//*[text()='%s']",textToFind);
        List<DomElement> elems = htmlPage.getByXPath(xpath);
        assertEquals(textToFind,elems.get(0).getTextContent());
    }

    @Test
    public void test_get_M19_IV_THEA1() throws Exception {
        String url = "http://localhost:8080/search/byclassroom/results?quarter=20202&building=PHELP&classroom=3526";
        HtmlPage htmlPage = webClient.getPage(url);
        String textToFind = "PSTAT   195  ";
        String xpath=String.format("//*[text()='%s']",textToFind);
        List<DomElement> elems = htmlPage.getByXPath(xpath);
	logger.info("ELEMS13249103948-132840ELEMSSSSSSSSSSSSSSSSSSSSSELEMSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS=" + elems);
        assertEquals(textToFind,elems.get(0).getTextContent());
    }

}
