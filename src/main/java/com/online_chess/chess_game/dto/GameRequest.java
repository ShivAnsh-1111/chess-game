package com.online_chess.chess_game.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameRequest {
    private Long player1Id;
    private Long player2Id;
    // getters and setters
	@Override
	public String toString() {
		return "GameRequest [player1Id=" + player1Id + ", player2Id=" + player2Id + "]";
	}
    
}
