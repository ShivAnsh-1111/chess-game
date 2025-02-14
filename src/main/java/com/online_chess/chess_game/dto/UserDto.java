package com.online_chess.chess_game.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {

    private Long id;
    private String username;
    private String password;
    private String email;
    private boolean isOnline;
    private LocalDateTime lastActivity;
    
}