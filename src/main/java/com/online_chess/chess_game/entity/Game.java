package com.online_chess.chess_game.entity;

import com.online_chess.chess_game.dto.GameDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String boardState;
    private String playerTurn;
    private String status;
    // getters and setters

    public GameDto toGameDto(){
        return new GameDto(this.id, this.boardState, this.playerTurn, this.status);
    }
}
