package com.app.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.app.model.Spreadsheet;
import com.app.model.Table;
import com.app.model.User;
import com.app.repository.Repository;

import spark.Request;

public class TableService {
        public Table creatTable(Request request,String table_name,String id,String sheet,int start_row,int start_col,int end_row,int end_col,List<String> col_names){
            Table table = new Table();
            User user = request.session().attribute("user");
            Repository repo = user.getRepository();
            Spreadsheet spreadsheet = repo.getSpreadsheetById(id);
            List<List<Object>> data = spreadsheet.getData().get(sheet);
            Map<String,List<Object>> table_data = new HashMap<>();
            for(int i = start_col;i<=end_col;i++){
                List<Object> column  = new ArrayList<>();
               
                for(int j=start_row;j<=end_row;j++){
                     column.add(data.get(j).get(i));

                }
                table_data.put(col_names.get(i-start_col),column);


            }
            table.setId(id)
                 .setSheet(sheet)
                 .setName(table_name)
                 .setData(table_data);
           repo.addTable(table);
       




    


            return table;

        }
}
