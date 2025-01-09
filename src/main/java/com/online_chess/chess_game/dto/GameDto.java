package com.online_chess.chess_game.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDto {

    private Long id;
    private String boardState;
    private String playerTurn;
    private String status;

	@Override
	public String toString() {
		return "GameDto [id=" + id + ", boardState=" + boardState + ", playerTurn=" + playerTurn + ", status=" + status
				+ "]";
	}
    
    
}
