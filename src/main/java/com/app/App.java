package com.app;

import spark.Spark;
import java.io.File;
import com.app.controller.AuthController;
import com.app.controller.EmailController;
import com.app.controller.HomeController;
import com.app.controller.SpreadSheetController;
import com.app.controller.TableController;

public class App extends Spark {
    public static void main(String[] args) {
        App.run();
    }

    private static void run() {
        port(8080);
        Spark.staticFiles.location("/public");

        before((req, res) -> {
            File f = new File("tokens/StoredCredential");
            if (!f.exists() && !req.pathInfo().equals("/") && !req.pathInfo().equals("/login")) {
                res.redirect("/");
            }
        });

        // Route definitions
        get("/", HomeController::index);
        get("/login", AuthController::login);
        get("/logout", AuthController::logOut);
        post("/spreadSheet", SpreadSheetController::index);
        get("/tables", TableController::index);
        post("/createTable", TableController::createTable);
        get("/email", EmailController::index);
        post("/sendEmails", EmailController::sendEmails);

        // Logging for debugging
        after((req, res) -> {
            System.out.println("Requested route: " + req.pathInfo());
        });
    }
}
