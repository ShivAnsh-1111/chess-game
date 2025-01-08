package com.online_chess.chess_game.dto;

import com.online_chess.chess_game.entity.Game;

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

    public Game toGame(){
        return Game.builder()
                .id(this.id)
                .status(this.status)
                .playerTurn(this.playerTurn)
                .boardState(this.boardState)
                .build();
    	
    }

	@Override
	public String toString() {
		return "GameDto [id=" + id + ", boardState=" + boardState + ", playerTurn=" + playerTurn + ", status=" + status
				+ "]";
	}
    
    
}
