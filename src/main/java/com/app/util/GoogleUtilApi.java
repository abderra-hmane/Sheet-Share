package com.app.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;

import com.app.exeption.NotLoggedException;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;

import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.sheets.v4.Sheets;
import com.google.inject.Inject;

import spark.Response;



public class GoogleUtilApi{
  
    private final JsonFactory jsonFactory;
    private final NetHttpTransport httpTransport;

    @Inject
    public GoogleUtilApi(JsonFactory jsonFactory, NetHttpTransport httpTransport) {
        this.jsonFactory = jsonFactory;
        this.httpTransport = httpTransport;
    }
       
   
    public  Gmail getGmailService() throws IOException,GeneralSecurityException{
            Gmail service = new Gmail.Builder(this.httpTransport, jsonFactory, getCredentials())
                .setApplicationName(GoogleApiConfig.APPLICATION_NAME)
                .build();
            return service;

    }
    public  Sheets getSheetsService() throws IOException,GeneralSecurityException{
            Sheets service = new Sheets.Builder(this.httpTransport, jsonFactory, getCredentials())
                .setApplicationName(GoogleApiConfig.APPLICATION_NAME)
                .build();
            return service;

    }
    public PeopleService getPeopleService()throws IOException,GeneralSecurityException{
        PeopleService peopleService = new PeopleService.Builder(this.httpTransport, this.jsonFactory, this.getCredentials())
        .setApplicationName(GoogleApiConfig.APPLICATION_NAME)
        .build();
        return peopleService;
    }




    public   Credential getCredentials() throws IOException{
        InputStream in = GoogleUtilApi.class.getResourceAsStream(GoogleApiConfig.CREDENTIALS_FILE_PATH);
        if (in == null) {
             throw new FileNotFoundException("Resource not found: " + GoogleApiConfig.CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        this.httpTransport, jsonFactory, clientSecrets, GoogleApiConfig.SCOPES)
            .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(GoogleApiConfig.TOKENS_DIRECTORY_PATH)))
            .setAccessType("offline")
            .build();

        LocalServerReceiver receiver= new LocalServerReceiver
                .Builder()
                .setPort(8888)
                .setCallbackPath("/auth")
                .setLandingPages("http://localhost:8080/auth", "http://localhost:8080/")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
 
        return credential;

}
    public   Credential getCredentials(Response response) throws IOException{
        InputStream in = GoogleUtilApi.class.getResourceAsStream(GoogleApiConfig.CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + GoogleApiConfig.CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        this.httpTransport, jsonFactory, clientSecrets, GoogleApiConfig.SCOPES)
            .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(GoogleApiConfig.TOKENS_DIRECTORY_PATH)))
            .setAccessType("offline")
            .build();

        LocalServerReceiver receiver= new LocalServerReceiver
                .Builder()
                .setPort(8888)
                .setCallbackPath("/auth")
                .setLandingPages("http://localhost:8080/auth", "http://localhost:8080/")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver){
            @Override
            protected void onAuthorization(AuthorizationCodeRequestUrl authorizationUrl) {
                try {
                    response.redirect(authorizationUrl.build());
                    
                    }
                catch (Exception e) {
                    throw new RuntimeException("Failed to open browser", e);
                }
            }
        }.authorize("user");

        return credential;

    }

    
}
