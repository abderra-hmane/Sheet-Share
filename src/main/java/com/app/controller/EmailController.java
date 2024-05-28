package com.app.controller;

import com.app.service.EmailService;
import com.app.service.SpreadSheetService;
import spark.Request;
import spark.Response;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;

public class EmailController extends Controller {

    public static String index(Request request, Response response) {
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("existingTables", TableController.getExistingTables());
            return render(model, "email.ftl");
        } catch (Exception e) {
            response.status(500);
            return "Internal Server Error: " + e.getMessage();
        }
    }

    public static String sendEmails(Request request, Response response) {
        try {
            String spreadSheetId = request.queryParams("spreadSheetId");
            String subjectTemplate = request.queryParams("subject");
            String bodyTemplate = request.queryParams("body");

            if (spreadSheetId == null || subjectTemplate == null || bodyTemplate == null) {
                response.status(400);
                return "Missing parameters";
            }

            Map<String, List<List<Object>>> data = SpreadSheetService.getSheetsValues(spreadSheetId);
            EmailService emailService = new EmailService();

            for (Map.Entry<String, List<List<Object>>> entry : data.entrySet()) {
                String tableName = entry.getKey();
                List<List<Object>> sheetData = entry.getValue();

                for (List<Object> row : sheetData) {
                    if (row.size() < 5) {
                        continue; // Skip rows that don't have enough data
                    }

                    String studentName = row.get(2).toString(); // Assuming name is in the third column
                    String studentEmail = row.get(4).toString(); // Assuming email is in the fifth column

                    // Skip invalid or placeholder email addresses
                    if ("Email".equalsIgnoreCase(studentEmail)) {
                        System.out.println("Skipping placeholder email address for student: " + studentName);
                        continue;
                    }

                    // Log email address for debugging
                    System.out.println("Attempting to send email to: " + studentEmail);

                    // Replace placeholders with actual values
                    String subject = subjectTemplate.replace("{studentName}", studentName)
                            .replace("{tableName}", tableName);
                    String body = bodyTemplate.replace("{studentName}", studentName)
                            .replace("{tableName}", tableName);

                    // Send email
                    try {
                        emailService.sendEmail(studentEmail, subject, body);
                    } catch (MessagingException e) {
                        System.err.println("Failed to send email to " + studentEmail + ": " + e.getMessage());
                    }
                }
            }
            response.redirect("/email"); // Redirect to the email form page after sending emails
            return "Emails sent successfully";
        } catch (IOException | GeneralSecurityException e) {
            response.status(500);
            return "Internal Server Error: " + e.getMessage();
        }
    }
}
