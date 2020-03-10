package edu.ucsb.cs56.ucsb_courses_search.model;

import org.springframework.beans.factory.annotation.Value;

public class ClientCredentials {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    public String getClientId(){
        return this.clientId;
    }
    public String getClientSecret(){
        return this.clientSecret;
    }
}

