package edu.ucsb.cs56.ucsb_courses_search;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

public class NavigationTestHelper {

    public static void hasNavBar(MockMvc mvc, String url) throws Exception  {
        mvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.TEXT_HTML))
        .andExpect(status().isOk())
        .andExpect(xpath("//nav").exists());

    }

    public static void hasFooter(MockMvc mvc, String url) throws Exception  {
        mvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.TEXT_HTML))
        .andExpect(status().isOk())
        .andExpect(xpath("//footer").exists());

    }
}