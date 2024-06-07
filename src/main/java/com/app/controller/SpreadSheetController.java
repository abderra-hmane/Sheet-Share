package com.app.controller;



import java.io.IOException;

import com.app.model.User;
import com.app.service.SpreadsheetService;
import com.google.inject.Inject;

import spark.Request;
import spark.Response;
import spark.Spark;

public class SpreadsheetController extends Controller{

    SpreadsheetService service ;

    @Inject
    public SpreadsheetController(SpreadsheetService spreadsheetService){
        this.service = spreadsheetService;
    }


    @Override 
    public void initRoutes(){
        Spark.get("/spreadsheet", this::index);
        Spark.get("/spreadsheet/addSpreadsheet",this::importSpreadsheet);
    }

    public String index(Request request ,Response response){
        return render(request, "spreadsheet.ftl");
    }

    public Response importSpreadsheet(Request request, Response response) throws IOException{
        String id = request.queryParams("spreadsheetId");
        User user = request.session().attribute("user");
        this.service.addSpreadsheet(id,user);
        return response;

    }
}
