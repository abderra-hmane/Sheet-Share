package com.app.controller;

import com.app.service.EmailService;
import spark.Request;
import spark.Response;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailController extends Controller {

    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    public static String index(Request request, Response response) {
        try {
            // Ensure existingTables is in the session
            if (request.session().attribute("existingTables") == null) {
                request.session().attribute("existingTables", TableController.getExistingTables());
            }

            Map<String, Object> model = new HashMap<>();
            model.put("existingTables", request.session().attribute("existingTables"));
            logger.info("Rendering email.ftl with model: {}", model);
            return render(model, "email.ftl");
        } catch (Exception e) {
            logger.error("Error rendering email.ftl", e);
            response.status(500);
            return "Internal Server Error: " + e.getMessage();
        }
    }

    public static String sendEmails(Request request, Response response) {
        try {
            String selectedTable = request.queryParams("emailTable");
            String subjectTemplate = request.queryParams("subject");
            String bodyTemplate = request.queryParams("body");

            if (selectedTable == null || subjectTemplate == null || bodyTemplate == null) {
                response.status(400);
                return "Missing parameters";
            }

            Map<String, List<List<Object>>> existingTables = request.session().attribute("existingTables");
            if (existingTables == null || !existingTables.containsKey(selectedTable)) {
                response.status(400);
                return "Selected table not found";
            }

            List<List<Object>> tableData = existingTables.get(selectedTable);
            EmailService emailService = new EmailService();

            for (List<Object> emailObject : tableData) {
                String studentEmail = emailObject.get(0).toString();

                // Skip invalid or placeholder email addresses
                if ("Email".equalsIgnoreCase(studentEmail)) {
                    System.out.println("Skipping placeholder email address");
                    continue;
                }

                // Log email address for debugging
                System.out.println("Attempting to send email to: " + studentEmail);

                // Replace placeholders with actual values
                String subject = subjectTemplate.replace("{tableName}", selectedTable);
                String body = bodyTemplate.replace("{tableName}", selectedTable);

                // Send email
                try {
                    emailService.sendEmail(studentEmail, subject, body);
                } catch (MessagingException e) {
                    System.err.println("Failed to send email to " + studentEmail + ": " + e.getMessage());
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
