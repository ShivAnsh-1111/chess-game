package com.online_chess.chess_game.component;

import java.io.Serializable;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardState implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<Square,Piece> board;
	private Color nextTurn;
	
	@Override
	public String toString() {
		return "BoardState [board=" + board + ", nextTurn=" + nextTurn + "]";
	}	

}
