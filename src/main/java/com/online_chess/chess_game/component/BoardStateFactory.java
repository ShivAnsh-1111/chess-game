package com.online_chess.chess_game.component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BoardStateFactory {

    /**
     * Creates a standard chess board setup with pieces in their initial positions.
     * 
     * @return BoardState representing the initial setup of a standard chess game.
     */
    public static BoardState createStandardBoard() {
        Map<Square, Piece> board = new ConcurrentHashMap<>();

        // Initialize pawns
        for (int i = 0; i < 8; i++) {
            board.put(Square.builder().x(1).y(i).build(),
                    Piece.builder().piece(PieceType.PAWN).color(Color.BLACK).build());
            board.put(Square.builder().x(6).y(i).build(),
                    Piece.builder().piece(PieceType.PAWN).color(Color.WHITE).build());
        }

        // Initialize rooks
        board.put(Square.builder().x(0).y(0).build(),
                Piece.builder().piece(PieceType.ROOK).color(Color.BLACK).build());
        board.put(Square.builder().x(0).y(7).build(),
                Piece.builder().piece(PieceType.ROOK).color(Color.BLACK).build());
        board.put(Square.builder().x(7).y(0).build(),
                Piece.builder().piece(PieceType.ROOK).color(Color.WHITE).build());
        board.put(Square.builder().x(7).y(7).build(),
                Piece.builder().piece(PieceType.ROOK).color(Color.WHITE).build());

        // Initialize knights
        board.put(Square.builder().x(0).y(1).build(),
                Piece.builder().piece(PieceType.KNIGHT).color(Color.BLACK).build());
        board.put(Square.builder().x(0).y(6).build(),
                Piece.builder().piece(PieceType.KNIGHT).color(Color.BLACK).build());
        board.put(Square.builder().x(7).y(1).build(),
                Piece.builder().piece(PieceType.KNIGHT).color(Color.WHITE).build());
        board.put(Square.builder().x(7).y(6).build(),
                Piece.builder().piece(PieceType.KNIGHT).color(Color.WHITE).build());

        // Initialize bishops
        board.put(Square.builder().x(0).y(2).build(),
                Piece.builder().piece(PieceType.BISHOP).color(Color.BLACK).build());
        board.put(Square.builder().x(0).y(5).build(),
                Piece.builder().piece(PieceType.BISHOP).color(Color.BLACK).build());
        board.put(Square.builder().x(7).y(2).build(),
                Piece.builder().piece(PieceType.BISHOP).color(Color.WHITE).build());
        board.put(Square.builder().x(7).y(5).build(),
                Piece.builder().piece(PieceType.BISHOP).color(Color.WHITE).build());

        // Initialize queens
        board.put(Square.builder().x(0).y(3).build(),
                Piece.builder().piece(PieceType.QUEEN).color(Color.BLACK).build());
        board.put(Square.builder().x(7).y(3).build(),
                Piece.builder().piece(PieceType.QUEEN).color(Color.WHITE).build());

        // Initialize kings
        board.put(Square.builder().x(0).y(4).build(),
                Piece.builder().piece(PieceType.KING).color(Color.BLACK).build());
        board.put(Square.builder().x(7).y(4).build(),
                Piece.builder().piece(PieceType.KING).color(Color.WHITE).build());

        return new BoardState(board, Color.WHITE);
    }

    /**
     * Creates a custom chess board setup based on the provided configuration.
     * 
     * @param customSetup A map representing the custom setup of pieces on the board.
     * @param nextTurn The color of the player who has the next turn.
     * @return BoardState representing the custom setup of a chess game.
     */
    public static BoardState createCustomBoard(Map<Square, Piece> customSetup, Color nextTurn) {
        return new BoardState(customSetup, nextTurn);
    }
}