package com.online_chess.chess_game.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object for move request data.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MoveRequest {

    @NotNull(message = "Game ID cannot be null")
    private Long gameId;

    @NotNull(message = "Move cannot be null")
    private String move;

    @NotNull(message = "Player cannot be null")
    private String player;
}