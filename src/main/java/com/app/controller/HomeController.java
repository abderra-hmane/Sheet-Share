package com.app.controller;


import java.io.IOException;

import com.app.config.AppConfig;
import com.app.model.User;
import com.app.service.UserService;
import com.app.util.GoogleUtilApi;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import spark.Request;
import spark.Response;
import spark.Spark;

public class HomeController extends Controller{
    
    @Inject 
    public HomeController(){
        
        
    }


    @Override
    public  void initRoutes() {
        Spark.get("/",this::index);
        Spark.get("/auth",this::auth);
        Spark.get("/login",this::login);
    }


    public String index(Request request,Response response) throws IOException {
        if(request.session().attribute("user")!=null){
            return render(request,"home.ftl");
        }
        return render(request,"login.ftl");
    }

            
    public Response auth(Request request,Response response) throws IOException
    {     
        if(request.session().attribute("user")==null){
            Injector injector = Guice.createInjector(new AppConfig());
            UserService userService = injector.getInstance(UserService.class);
            User user = userService.creatUser(request);
            request.session().attribute("user",user);
            SpreadsheetController spreadsheetController= injector.getInstance(SpreadsheetController.class);
            EmailController emailController = injector.getInstance(EmailController.class);
            TableController tableController = injector.getInstance(TableController.class);
            spreadsheetController.initRoutes();
            emailController.initRoutes();
            tableController.initRoutes();
            request.session().removeAttribute("loggin");
        }
        
        response.redirect("/");  
        return response;
    }


    public String login(Request request ,Response response) throws IOException{
        Injector injector = Guice.createInjector(new AppConfig());
        GoogleUtilApi googleUtilApi = injector.getInstance(GoogleUtilApi.class);
        request.attribute("loggin",true);
        googleUtilApi.getCredentials(response);
        response.redirect("/auth");
        return null;
        
    }
    
}
