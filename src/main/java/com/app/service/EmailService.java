package com.app.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.codec.binary.Base64;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.MessagingException;
import javax.mail.Session;
import com.app.model.Table;
import com.app.util.GoogleUtilApi;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.sheets.v4.Sheets;
import com.google.inject.Inject;

public class EmailService {
    Gmail service ;

    @Inject
    public EmailService(GoogleUtilApi api) throws IOException,GeneralSecurityException{
        this.service = api.getGmailService();
    }
    
    public List<String> compile(List<Table> tables,String message){
         String regex = "\\{\\{([^->]+)->([^}]+)\\}\\}";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(message);

        Map<String, List<Object>> replacements = new HashMap<>();

        // Gather all replacements needed
        while (matcher.find()) {
            String tableName = matcher.group(1);
            String columnName = matcher.group(2);

            for (Table table : tables) {
                if (table.getName().equals(tableName)) {
                    List<Object> columnData = table.getData().get(columnName);
                    if (columnData != null) {
                        replacements.put(matcher.group(0), columnData);
                    }
                }
            }
        }

        // Build the final list of results
        List<String> results = new ArrayList<>();
        results.add(message);

        for (Map.Entry<String, List<Object>> entry : replacements.entrySet()) {
            String placeholder = entry.getKey();
            List<Object> values = entry.getValue();
            List<String> newResults = new ArrayList<>();

            for (String result : results) {
                for (Object value : values) {
                    newResults.add(result.replace(placeholder, value.toString()));
                }
            }

            results = newResults;
        }

        return results;

    }
    public void sendEmail(Table destTable,String dest,List<Table> tables,String subject,String message )throws IOException,MessagingException {
        List<Object> destinations = destTable.getData().get(dest); 
        List<String> messages = this.compile(tables, message);
        for(Object destination :destinations){
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage email = new MimeMessage(session);
            email.setFrom(new InternetAddress("me"));
            email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(destination.toString()));
            email.setSubject(subject);
            email.setText(messages.get(destinations.indexOf(destination)));
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            email.writeTo(buffer);
            byte[] rawMessageBytes = buffer.toByteArray();
            String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
            Message messageEmail = new Message();
            messageEmail.setRaw(encodedEmail);
            this.service.users().messages().send("me", messageEmail).execute();


        }
    }
} 
