package com.online_chess.chess_game.security;

import com.online_chess.chess_game.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to handle user authentication and JWT token generation.
 */
@RestController
public class AuthenticationController {

    @Autowired
    private JwtUtil jwtUtil;

    // Dummy credentials for demo: username "user", password "password"
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        if ("user".equals(authRequest.getUsername()) && "password".equals(authRequest.getPassword())) {
            // Generate token
            final String token = jwtUtil.generateToken(authRequest.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));
        } else {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Invalid Credentials");
        }
    }

    // DTO for authentication request
    public static class AuthRequest {
        private String username;
        private String password;
        
        // Getters and setters
        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    }
    
    // DTO for authentication response
    public static class AuthResponse {
        private String token;
        public AuthResponse(String token) {
            this.token = token;
        }
        public String getToken() {
            return token;
        }
        public void setToken(String token) {
            this.token = token;
        }
    }
}