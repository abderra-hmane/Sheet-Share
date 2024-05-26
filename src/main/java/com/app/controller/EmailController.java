package com.app.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;

import com.app.service.EmailService;
import com.app.service.SpreadSheetService;
import spark.Request;
import spark.Response;

public class EmailController extends Controller {
    public static String index(Request request, Response response) throws IOException, GeneralSecurityException{
        return render("email.ftl");
    }

    public static String sendEmails(Request request, Response response) throws IOException, GeneralSecurityException {
        String spreadSheetId = request.queryParams("spreadSheetId");
        Map<String, List<List<Object>>> data = SpreadSheetService.getSheetsValues(spreadSheetId);

        String subjectTemplate = request.queryParams("subjectTemplate");
        String bodyTemplate = request.queryParams("bodyTemplate");

        EmailService emailService = new EmailService();

        data.forEach((sheetName, sheetData) -> {
            for (List<Object> row : sheetData) {
                if (row.size() < 3) {
                    continue; // Skip rows that don't have enough data
                }

                String studentName = (String) row.get(2); // Assuming name is in the first column
                String studentEmail = (String) row.get(4); // Assuming email is in the second column
                String grade = (String) row.get(5); // Assuming grade is in the third column

                String subject = subjectTemplate.replace("{studentName}", studentName);
                String body = bodyTemplate.replace("{studentName}", studentName).replace("{grade}", grade);

                try {
                    emailService.sendEmail(studentEmail, subject, body);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        });

        return "Emails sent successfully";
    }
}
