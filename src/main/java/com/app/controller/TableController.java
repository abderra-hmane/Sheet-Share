package com.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.app.service.EmailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

public class TableController extends Controller {

    private static final Logger logger = LoggerFactory.getLogger(TableController.class);
    private static Map<String, List<List<Object>>> existingTables = new HashMap<>();

    // Renders the /tables page
    public static String index(Request request, Response response) {
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("existingTables", existingTables);
            logger.info("Rendering tables.ftl with model: {}", model);
            return render(model, "tables.ftl");
        } catch (Exception e) {
            logger.error("Error rendering tables.ftl", e);
            response.status(500);
            return "Internal Server Error: " + e.getMessage();
        }
    }

    // Handles form submission for creating a new table
    public static String createTable(Request request, Response response) {
        try {
            String tableName = request.queryParams("tableName");
            String spreadSheetId = request.queryParams("spreadSheetId");
            String sheetName = request.queryParams("sheetName");
            String range = request.queryParams("range");

            logger.info("Creating table with name: {}, spreadsheet ID: {}, sheet name: {}, range: {}", tableName,
                    spreadSheetId, sheetName, range);

            List<List<Object>> values = EmailService.getSheetValues(spreadSheetId, sheetName, range);

            if (values != null) {
                existingTables.put(tableName, values);
                logger.info("Table '{}' created successfully with data: {}", tableName, values);
            } else {
                logger.warn("No data found for table '{}'", tableName);
            }

            // Redirect to /tables after processing the form
            response.redirect("/tables");
            return null;
        } catch (Exception e) {
            logger.error("Error creating table", e);
            response.status(500);
            return "Internal Server Error: " + e.getMessage();
        }
    }

    public static Map<String, List<List<Object>>> getExistingTables() {
        return existingTables;
    }
}
