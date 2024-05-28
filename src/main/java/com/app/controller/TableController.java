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

    public static String index(Request request, Response response) {
        try {
            // Ensure existingTables is in the session
            if (request.session().attribute("existingTables") == null) {
                request.session().attribute("existingTables", existingTables);
            }

            Map<String, Object> model = new HashMap<>();
            model.put("existingTables", request.session().attribute("existingTables"));
            logger.info("Rendering tables.ftl with model: {}", model);
            return render(model, "tables.ftl");
        } catch (Exception e) {
            logger.error("Error rendering tables.ftl", e);
            response.status(500);
            return "Internal Server Error: " + e.getMessage();
        }
    }

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
                request.session().attribute("existingTables", existingTables);
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
