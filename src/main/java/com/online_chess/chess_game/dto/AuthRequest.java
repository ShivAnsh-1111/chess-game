package com.online_chess.chess_game.dto;

public class AuthRequest {
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
