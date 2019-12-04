package edu.ucsb.cs56.ucsb_courses_search;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

public class BootstrapTestHelper {

    public static final String bootstrapCSSXpath = "//head/link[@rel='stylesheet' and @href='https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css']";

    public static final String [] bootstrapJSurls = {
        "https://code.jquery.com/jquery-3.3.1.slim.min.js", 
        "https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js", 
        "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js",
    };

    public static void bootstrapIsLoaded(MockMvc mvc, String url) throws Exception  {
        mvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(xpath(bootstrapCSSXpath).exists());
        for (String s: bootstrapJSurls) {
            String jsXPath = String.format("//script[@src='%s']",s);
            mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.TEXT_HTML))
              .andExpect(status().isOk())
              .andExpect(xpath(jsXPath).exists());
        }

    }
}