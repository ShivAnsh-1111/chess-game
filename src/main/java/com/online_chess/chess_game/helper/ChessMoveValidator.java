package com.online_chess.chess_game.helper;

import org.springframework.stereotype.Service;

import com.online_chess.chess_game.component.BoardState;
import com.online_chess.chess_game.component.PieceType;
import com.online_chess.chess_game.component.Square;

@Service
public class ChessMoveValidator {

    public boolean isValidMove(PieceType pieceType, Square current, Square target, boolean isWhite) {
        switch (pieceType) {
            case PAWN:
                return isValidPawnMove(current, target, isWhite);
            case KNIGHT:
                return isValidKnightMove(current, target);
            case BISHOP:
                return isValidBishopMove(current, target);
            case ROOK:
                return isValidRookMove(current, target);
            case QUEEN:
                return isValidQueenMove(current, target);
            case KING:
                return isValidKingMove(current, target);
            default:
                return false;
        }
    }

    private boolean isValidPawnMove(Square current, Square target, boolean isWhite) {
        int direction = isWhite ? -1 : 1;
        if (current.getX() + direction == target.getX() && current.getY() == target.getY()) {
            return true; // Normal move
        }
        // Additional pawn logic: capture, double step, etc.
        return false;
    }

    private boolean isValidKnightMove(Square current, Square target) {
        int dx = Math.abs(current.getX() - target.getX());
        int dy = Math.abs(current.getY() - target.getY());
        return (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
    }

    private boolean isValidBishopMove(Square current, Square target) {
        return Math.abs(current.getX() - target.getX()) == Math.abs(current.getY() - target.getY());
    }

    private boolean isValidRookMove(Square current, Square target) {
        return current.getX() == target.getX() || current.getY() == target.getY();
    }

    private boolean isValidQueenMove(Square current, Square target) {
        return isValidRookMove(current, target) || isValidBishopMove(current, target);
    }

    private boolean isValidKingMove(Square current, Square target) {
        int dx = Math.abs(current.getX() - target.getX());
        int dy = Math.abs(current.getY() - target.getY());
        return dx <= 1 && dy <= 1;
    }
    
    public boolean isBlockingPiece(BoardState board, PieceType piece, Square source, Square target) {
    	
    	int srcRow = source.getX();
    	int srcCol = source.getY();
    	int destRow = target.getX();
    	int destCol = target.getY();
    	
    	int rowStep = Integer.compare(destRow, srcRow); // -1, 0, or 1
        int colStep = Integer.compare(destCol, srcCol);
        
        int currentRow = srcRow + rowStep;
        int currentCol = srcCol + colStep;
        
        Square current = Square.builder().build();
        while (currentRow != destRow || currentCol != destCol) {
        	current = Square.builder().x(currentRow).y(currentCol).build();
        	
        	if(!board.getBoard().get(current).getPiece().equals(PieceType.ABSENT)) {
        		return true;
        	}
        	
            currentRow += rowStep;
            currentCol += colStep;
        }
    	
    	return false;
    }
    
}
