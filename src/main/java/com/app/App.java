package com.app;

import com.app.config.AppConfig;
import com.app.controller.EmailController;
import com.app.controller.HomeController;
import com.app.controller.SpreadsheetController;
import com.app.controller.TableController;
import com.app.model.User;
import com.app.repository.Repository;
import com.google.inject.Guice;
import com.google.inject.Injector;
import static spark.debug.DebugScreen.enableDebugScreen;
import spark.Spark;

/**
 * the app entry
 */
public class App 
{
    public static void main( String[] args )
    {
        
        App.run();
    }
    public static void run(){
        enableDebugScreen();
        Injector injector = Guice.createInjector(new AppConfig());
        Spark.port(8080);
        Spark.staticFiles.location("/public");
        HomeController  homeController = injector.getInstance(HomeController.class);
        
        
        Spark.before((req,res)->{
           
           
            
        });
                
        homeController.initRoutes();
        
        
       


        
        
    }
}
