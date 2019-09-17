package hello;

// import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HomePageTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getHomePage_ContentType() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }


    @Test
    public void getHomePage_BootstrapLoaded() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(xpath(BootstrapLiterals.bootstrapCSSXpath).exists());
        for (String s: BootstrapLiterals.bootstrapJSurls) {
            String jsXPath = String.format("//script[@src='%s']",s);
            mvc.perform(MockMvcRequestBuilders.get("/greeting").accept(MediaType.TEXT_HTML))
              .andExpect(status().isOk())
              .andExpect(xpath(jsXPath).exists());
        }
    }


    @Test
    public void getHomePage_hasCorrectTitle() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(xpath("//title").exists())
                .andExpect(xpath("//title").string("Getting Started: Serving Web Content"));
    }
}