package edu.escuelaing.arep.secureweb;

import static spark.Spark.*;

public class CalculatorApp {

    public static void main(String... args) {
        
        port(getPort());
        secure("certificates/ecikeystore.p12", "123456", null, null);
        get("sin", (req, res) -> {
            String value = req.queryParams("value");
            return "The sin of " + value + " is " + Math.sin(Double.parseDouble(value));
        });

        get("cos", (req, res) -> {
            String value = req.queryParams("value");
            return "The cos of " + value + " is " + Math.cos(Double.parseDouble(value));
        });

    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4568;
    }

}
