package edu.escuelaing.arep.secureweb;

import static spark.Spark.*;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import edu.escuelaing.arep.secureweb.service.SecureURLReader;
import edu.escuelaing.arep.secureweb.service.UserService;

public class LoginApp {

    public static void main(String[] args) {
        UserService userService = UserService.getInstance();
        staticFiles.location("/public");
        port(getPort());
        secure("certificates/ecikeystore.p12", "123456", null, null);
        post("/login", (req, res) -> {
            String body = req.body();
            Gson gson = new Gson();
            Map<String, String> json = gson.fromJson(body, Map.class);
            JsonObject jsonResponse = new JsonObject();
            if (userService.validateUser(json.get("username"), json.get("password"))) {
                res.status(200);
                jsonResponse.addProperty("status", "success");
                return gson.toJson(jsonResponse);
            } else {
                jsonResponse.addProperty("status", "error");
                res.status(401);
                return gson.toJson(jsonResponse);
            }
        });
        get("/sin", (req, res) -> {
            String value = req.queryParams("value");
            return SecureURLReader.readURLSecure(getURL() + "/sin?value=" + value);
        });

        get("/cos", (req, res) -> {
            String value = req.queryParams("value");
            return SecureURLReader.readURLSecure(getURL() + "/cos?value=" + value);
        });
    }

    private static String getURL() {
        if (System.getenv("URL") != null) {
            return System.getenv("URL");
        }
        return "https://localhost:4568";
    }

    public static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }

    static String getTrustStore() {
        if(System.getenv("TrustStore") != null){
            return System.getenv("TrustStore");
        }
        return "certificates/myTrustStore.p12";
    }

    static String getKeyPassword() {
        if(System.getenv("KeyPassword") != null){
            return System.getenv("KeyPassword");
        }
        return "654321";
    }
}
