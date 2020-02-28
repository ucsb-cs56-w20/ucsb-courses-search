package edu.ucsb.cs56.ucsb_courses_search.controller;

import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriverBuilder;
import org.hibernate.validator.internal.engine.messageinterpolation.ElTermResolver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.web.context.WebApplicationContext;


import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

import org.openqa.selenium.WebDriver;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class SearchByDeptControllerTest {

    private Logger logger = LoggerFactory.getLogger(SearchByDeptControllerTest.class);

    @Autowired
    private WebApplicationContext context;

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
    public void test_get_W20_CMPSC_Undergrad_All() throws Exception {
        String url = "http://localhost/search/bydept/results?dept=CMPSC&quarter=20201&courseLevel=U";
        HtmlPage htmlPage = webClient.getPage(url);
    }

}
