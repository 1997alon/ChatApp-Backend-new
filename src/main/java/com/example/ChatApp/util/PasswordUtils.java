package com.example.ChatApp.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordUtils {

    // Hash the password (use this when saving new user passwords)
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Verify password against hashed password
    public static boolean verifyPassword(String password, String hashed) {
        if (password == null || hashed == null) {
            return false;
        }
        return BCrypt.checkpw(password, hashed);
    }
}
