package com.app.controller;

import java.util.HashMap;
import java.util.Map;

import com.app.App;


import freemarker.template.Configuration;
import spark.ModelAndView;
import spark.Request;
import spark.template.freemarker.FreeMarkerEngine;

public abstract class Controller {

    public static  FreeMarkerEngine FreeMarkerEngine() {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setClassForTemplateLoading(App.class, "/public/templates");
        return new FreeMarkerEngine(cfg);
    }

    
    public static String render(Request request,Map<String,Object> param,String file){
        param.put("user",request.session().attribute("user"));  
        return FreeMarkerEngine().render(new ModelAndView(param, file));
    }


    public static String render(Request request,String file){
        Map<String,Object> param = new HashMap<>();
        param.put("user",request.session().attribute("user"));
        return FreeMarkerEngine().render(new ModelAndView(param, file));
    }


    public void initRoutes() {

    }

}
