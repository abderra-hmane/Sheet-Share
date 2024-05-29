package com.app.controller;

import com.app.service.EmailService;
import spark.Request;
import spark.Response;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
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

            List<List<Object>> emailTableData = existingTables.get(selectedTable);
            EmailService emailService = new EmailService();

            // Email validation pattern
            Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,6}$",
                    Pattern.CASE_INSENSITIVE);

            // Loop through each row in the email table, starting from the second row (index
            // 1)
            for (int i = 0; i < emailTableData.size(); i++) {
                List<Object> emailRow = emailTableData.get(i);
                String studentEmail = emailRow.get(0).toString(); // Each row has only one element which is the email

                // Validate email address
                if (!emailPattern.matcher(studentEmail).matches()) {
                    logger.info("Skipping invalid email address: {}", studentEmail);
                    continue;
                }

                // Log email address for debugging
                logger.info("Attempting to send email to: {}", studentEmail);

                // Initialize subject and body for this email
                String subject = subjectTemplate;
                String body = bodyTemplate;

                // Iterate over all tables to replace placeholders
                for (String tableName : existingTables.keySet()) {
                    List<List<Object>> tableData = existingTables.get(tableName);

                    // Ensure there are enough rows in the table
                    if (i < tableData.size()) {
                        List<Object> rowData = tableData.get(i);

                        // Replace placeholders in the subject and body
                        for (int j = 0; j < rowData.size(); j++) {
                            String placeholder = "{" + tableName + "}";
                            String replacement = rowData.get(j).toString();

                            subject = subject.replace(placeholder, replacement);
                            body = body.replace(placeholder, replacement);
                        }
                    }
                }

                // Send email
                try {
                    emailService.sendEmail(studentEmail, subject, body);
                } catch (MessagingException e) {
                    logger.error("Failed to send email to {}: {}", studentEmail, e.getMessage());
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
