package edu.escuelaing.arep.secureweb.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;

public class UserService {

    private HashMap<String, String> users = new HashMap<>();
    private static UserService instance;

    public UserService() {
        users.put("admin", hashedPassword("password"));
        users.put("Pepe", hashedPassword("12345"));
        users.put("Samuel", hashedPassword("54321"));
    }

    public String hashedPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedPassword = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error al crear el hash");
            return null;
        }
    }

    public boolean validateUser(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(hashedPassword(password));
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

}
