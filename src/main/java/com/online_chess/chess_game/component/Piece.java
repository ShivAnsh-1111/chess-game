package com.online_chess.chess_game.component;

import java.io.Serializable;

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
public class Piece implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	PieceType piece;
	Color color;
	
	@Override
	public String toString() {
		return "Piece [piece=" + piece + ", color=" + color + "]";
	}
	
}
