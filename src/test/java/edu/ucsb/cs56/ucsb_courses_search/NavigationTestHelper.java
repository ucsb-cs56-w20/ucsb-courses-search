package edu.ucsb.cs56.ucsb_courses_search;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class NavigationTestHelper {

    
    public static void hasNavBar(MockMvc mvc, String url) throws Exception  {
        mvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.TEXT_HTML))
        .andExpect(status().isOk())
        .andExpect(xpath("//nav").exists());

    }

    
    public static void hasCorrectTextInNavBarBrand(MockMvc mvc, String url) throws Exception  {
        String findBrandLinkXPath = "//nav/a[@id='find-brand-link']";
        mvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.TEXT_HTML))
        .andExpect(status().isOk())
        .andExpect(xpath(findBrandLinkXPath).exists())
        .andExpect(xpath(findBrandLinkXPath).string("UCSB Courses Search"));
    }

    
    public static void hasFooter(MockMvc mvc, String url) throws Exception  {
        mvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.TEXT_HTML))
        .andExpect(status().isOk())
        .andExpect(xpath("//footer").exists());

    }
}