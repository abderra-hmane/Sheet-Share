package com.app.service;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.app.api.GmailAPI;
import com.app.api.SpreadSheetAPI;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Properties;
import java.util.List;
import java.util.regex.Pattern;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.codec.binary.Base64;

public class EmailService {

    private Gmail service;

    public EmailService() throws GeneralSecurityException, IOException {
        this.service = GmailAPI.getService();
    }

    public static List<List<Object>> getSheetValues(String id, String sheetName, String range)
            throws IOException, GeneralSecurityException {
        Sheets service = SpreadSheetAPI.getService();
        String rangeSpec = sheetName + "!" + range;
        ValueRange response = service.spreadsheets().values().get(id, rangeSpec).execute();
        return response.getValues();
    }

    public void sendEmail(String to, String subject, String bodyText) throws MessagingException, IOException {
        if (to == null || !isValidEmail(to)) {
            throw new MessagingException("Invalid email address: " + to);
        }

        MimeMessage email = createEmail(to, "me", subject, bodyText);
        sendMessage(service, "me", email);
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pat = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        return pat.matcher(email).matches();
    }

    private static MimeMessage createEmail(String to, String from, String subject, String bodyText)
            throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }

    private static void sendMessage(Gmail service, String userId, MimeMessage email)
            throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        service.users().messages().send(userId, message).execute();
    }
}
