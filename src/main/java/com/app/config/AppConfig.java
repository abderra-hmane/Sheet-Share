package com.app.config;



import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class AppConfig  extends AbstractModule{
    @Override
    public void configure(){
       
        
    }

    
    @Provides
    @Singleton
    JsonFactory provideJsonFactory() {
        return GsonFactory.getDefaultInstance();
    }

    @Provides
    @Singleton
    NetHttpTransport provideNetHttpTransport() throws GeneralSecurityException, IOException {
        return GoogleNetHttpTransport.newTrustedTransport();
    }
    
}
