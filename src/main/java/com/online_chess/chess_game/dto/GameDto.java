package com.online_chess.chess_game.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object for game-related data.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GameDto {

    @NotNull
    private Long id;

    @NotNull
    private String boardState;

    @NotNull
    private String playerTurn;

    @NotNull
    private String status;
}