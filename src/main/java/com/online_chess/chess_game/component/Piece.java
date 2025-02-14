package com.online_chess.chess_game.component;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Piece implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	PieceType piece;
	Color color;
	
}
