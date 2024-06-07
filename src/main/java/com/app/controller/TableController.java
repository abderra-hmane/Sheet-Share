package com.app.controller;

import java.util.ArrayList;
import java.util.List;

import com.app.service.TableService;
import com.google.inject.Inject;

import spark.Request;
import spark.Response;
import spark.Spark;

public class TableController extends Controller{

    TableService service ;

    @Inject
    public TableController(TableService tableService){
        this.service = tableService;
    }


    @Override 
    public void initRoutes(){
        Spark.get("/table",this::index);
        Spark.get("/creatTable",this::creatTable);
    }

    public String index(Request request, Response response){
        return render(request, "table.ftl");
    }
    public Response creatTable(Request request,Response response){

        String spreadsheet = request.queryParams("spreadsheet");
        String sheet = request.queryParams("sheet");
        int start_row = Integer.parseInt(request.queryParams("start_row"));
        int start_col = Integer.parseInt(request.queryParams("start_col"));
        int end_row = Integer.parseInt(request.queryParams("end_row"));
        int end_col = Integer.parseInt(request.queryParams("end_col"));
        String table_name = request.queryParams("table_name");
        List<String> Col_names = new ArrayList<>();
        for(int i=start_col;i<=end_col;i++){
            Col_names.add(request.queryParams("column_"+i));
        }
       

         this.service.creatTable(request, table_name,spreadsheet, sheet, start_row, start_col, end_row, end_col, Col_names);



        return response;

    }
}