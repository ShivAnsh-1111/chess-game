package com.online_chess.chess_game.component;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.online_chess.chess_game.serialize.SquareKeyDeserializer;
import com.online_chess.chess_game.serialize.SquareKeySerializer;

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
@JsonSerialize(keyUsing = SquareKeySerializer.class)
@JsonDeserialize(keyUsing = SquareKeyDeserializer.class)
public class Square implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x; // 0-7 for rows
    private int y; // 0-7 for columns
	
    @Override
    public String toString() {
        return x + "," + y; // Consistent "x,y" format
    }

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Square other = (Square) obj;
		return x == other.x && y == other.y;
	}
    
    
    
}
