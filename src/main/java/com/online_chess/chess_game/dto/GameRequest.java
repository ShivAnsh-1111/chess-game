package com.online_chess.chess_game.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object for game request data.
 */
@Getter
@Setter
@ToString
public class GameRequest {

    @NotNull(message = "Player 1 ID cannot be null")
    private Long player1Id;

    @NotNull(message = "Player 2 ID cannot be null")
    private Long player2Id;

}
