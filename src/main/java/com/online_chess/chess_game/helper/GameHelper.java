package com.online_chess.chess_game.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.online_chess.chess_game.component.BoardState;
import com.online_chess.chess_game.component.Color;
import com.online_chess.chess_game.component.Piece;
import com.online_chess.chess_game.component.PieceType;
import com.online_chess.chess_game.component.Square;
import com.online_chess.chess_game.dto.GameDto;
import com.online_chess.chess_game.dto.MoveRequest;
import com.online_chess.chess_game.exception.CustomException;

import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Log
public class GameHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final ChessMoveValidator chessMoveValidator;

    public GameHelper(ChessMoveValidator chessMoveValidator) {
        this.chessMoveValidator = chessMoveValidator;
    }

    public BoardState initializeBoard() {
        Map<Square, Piece> board = new ConcurrentHashMap<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i == 1) {
                    board.put(Square.builder().x(i).y(j).build(),
                            Piece.builder().piece(PieceType.PAWN).color(Color.BLACK).build());
                } else if (i == 6) {
                    board.put(Square.builder().x(i).y(j).build(),
                            Piece.builder().piece(PieceType.PAWN).color(Color.WHITE).build());
                } else if (1 < i && i < 6) {
                    board.put(Square.builder().x(i).y(j).build(),
                            Piece.builder().piece(PieceType.ABSENT).color(Color.ABSENT).build());
                } else {
                    if (i == 0 && (j == 0 || j == 7)) {
                        board.put(Square.builder().x(i).y(j).build(),
                                Piece.builder().piece(PieceType.ROOK).color(Color.BLACK).build());

                    } else if (i == 7 && (j == 0 || j == 7)) {
                        board.put(Square.builder().x(i).y(j).build(),
                                Piece.builder().piece(PieceType.ROOK).color(Color.WHITE).build());
                    }

                    if (i == 0 && (j == 1 || j == 6)) {
                        board.put(Square.builder().x(i).y(j).build(),
                                Piece.builder().piece(PieceType.KNIGHT).color(Color.BLACK).build());

                    } else if (i == 7 && (j == 1 || j == 6)) {
                        board.put(Square.builder().x(i).y(j).build(),
                                Piece.builder().piece(PieceType.KNIGHT).color(Color.WHITE).build());
                    }

                    if (i == 0 && (j == 2 || j == 5)) {
                        board.put(Square.builder().x(i).y(j).build(),
                                Piece.builder().piece(PieceType.BISHOP).color(Color.BLACK).build());

                    } else if (i == 7 && (j == 2 || j == 5)) {
                        board.put(Square.builder().x(i).y(j).build(),
                                Piece.builder().piece(PieceType.BISHOP).color(Color.WHITE).build());
                    }

                    if (i == 0 && j == 3) {
                        board.put(Square.builder().x(i).y(j).build(),
                                Piece.builder().piece(PieceType.QUEEN).color(Color.BLACK).build());

                    } else if (i == 7 && j == 3) {
                        board.put(Square.builder().x(i).y(j).build(),
                                Piece.builder().piece(PieceType.QUEEN).color(Color.WHITE).build());
                    }

                    if (i == 0 && j == 4) {
                        board.put(Square.builder().x(i).y(j).build(),
                                Piece.builder().piece(PieceType.KING).color(Color.BLACK).build());

                    } else if (i == 7 && j == 4) {
                        board.put(Square.builder().x(i).y(j).build(),
                                Piece.builder().piece(PieceType.KING).color(Color.WHITE).build());
                    }
                }

            }

        }
        log.info("Initialized board: " + board);

        BoardState boardState = BoardState.builder().board(board).build();
        return boardState;
    }

    public String serializeBoardState(BoardState boardState) {
        try {
            return objectMapper.writeValueAsString(boardState);
        } catch (JsonProcessingException e) {
            throw new CustomException(e.getMessage());
        }
    }

    public List<String> getMoveDetails(MoveRequest moveRequest) {
        PieceType p = PieceType.ABSENT;
        Color c = Color.ABSENT;
        String[] move = moveRequest.getMove().split("");
        String m = move[0];

        switch (m) {
            case "p":
                p = PieceType.PAWN;
                c = Color.BLACK;
                break;
            case "r":
                p = PieceType.ROOK;
                c = Color.BLACK;
                break;
            case "n":
                p = PieceType.KNIGHT;
                c = Color.BLACK;
                break;
            case "b":
                p = PieceType.BISHOP;
                c = Color.BLACK;
                break;
            case "q":
                p = PieceType.QUEEN;
                c = Color.BLACK;
                break;
            case "k":
                p = PieceType.KING;
                c = Color.BLACK;
                break;
            case "P":
                p = PieceType.PAWN;
                c = Color.WHITE;
                break;
            case "R":
                p = PieceType.ROOK;
                c = Color.WHITE;
                break;
            case "N":
                p = PieceType.KNIGHT;
                c = Color.WHITE;
                break;
            case "B":
                p = PieceType.BISHOP;
                c = Color.WHITE;
                break;
            case "Q":
                p = PieceType.QUEEN;
                c = Color.WHITE;
                break;
            case "K":
                p = PieceType.KING;
                c = Color.WHITE;
                break;
            default:
        }

        boolean isWhite = c == Color.WHITE;
        List<String> moveDetails = new ArrayList<>();
        try {
            moveDetails.add(objectMapper.writeValueAsString(p));
            moveDetails.add(objectMapper.writeValueAsString(isWhite));
            moveDetails.add(moveRequest.getMove());
        } catch (JsonProcessingException e) {
            throw new CustomException(e.getMessage());
        }

        return moveDetails;
    }

    public BoardState applyMove(BoardState boardState, List<String> moveDetails) {
        String[] move = moveDetails.get(2).split("");
        Square current = Square.builder().x(Integer.valueOf(move[1])).y(Integer.valueOf(move[2])).build();
        int t1 = 3, t2 = 4;
        if (move.length > 5) {
            t1 = 4;
            t2 = 5;
        }
        Square target = Square.builder().x(Integer.valueOf(move[t1])).y(Integer.valueOf(move[t2])).build();
        boardState = updateBoardState(boardState, current, target);

        return boardState;
    }

    private BoardState updateBoardState(BoardState board, Square source, Square destination) {
        Map<Square, Piece> currentBoard = board.getBoard();

        Piece pieceToUpdate = currentBoard.remove(source);
        Piece newPiece = Piece.builder().piece(PieceType.ABSENT).color(Color.ABSENT).build();
        currentBoard.put(source, newPiece);

        currentBoard.remove(destination);
        currentBoard.put(destination, pieceToUpdate);
        log.info("after" + currentBoard);

        Color nextTurn = board.getNextTurn() == Color.WHITE ? Color.BLACK : Color.WHITE;
        board.setNextTurn(nextTurn);

        board.setBoard(currentBoard);
        return board;
    }

    public void validateTurn(BoardState boardState, List<String> moveDetails) throws JsonProcessingException {
        Color currentColor = objectMapper.readValue(moveDetails.get(1), boolean.class) ? Color.WHITE : Color.BLACK;
        if (currentColor != boardState.getNextTurn()) {
            throw new CustomException("It's not your turn");
        }
    }

    public void validateMove(BoardState boardState, MoveRequest moveRequest) {
        if (!isValidMove(boardState, moveRequest)) {
            throw new CustomException("Invalid move");
        }
    }

    private boolean isValidMove(BoardState boardState, MoveRequest move) {
        List<String> moveDetails = getMoveDetails(move);
        PieceType piece;
        try {
            piece = objectMapper.readValue(moveDetails.get(0), PieceType.class);
            boolean isWhite = objectMapper.readValue(moveDetails.get(1), Boolean.class);

            String[] moves = move.getMove().split("");
            Square current = Square.builder().x(Integer.valueOf(moves[1])).y(Integer.valueOf(moves[2])).build();
            int t1 = 3, t2 = 4;
            if (moves.length > 5) {
                t1 = 4;
                t2 = 5;
            }
            Square target = Square.builder().x(Integer.valueOf(moves[t1])).y(Integer.valueOf(moves[t2])).build();

            // Basic validation
            if (!chessMoveValidator.isValidMove(piece, current, target, isWhite)) {
                return false;
            }

            // Blocking piece validation
            if (piece == PieceType.BISHOP || piece == PieceType.ROOK || piece == PieceType.QUEEN) {
                if (chessMoveValidator.isBlockingPiece(boardState, piece, current, target)) {
                    return false;
                }
            }

        } catch (Exception ex) {
            throw new CustomException(ex.getMessage());
        }

        return true;
    }

    public void updateGameStatus(GameDto gameDto, BoardState updatedBoardState) {
        if (isGameOver(updatedBoardState)) {
            gameDto.setStatus("finished");
        }
    }

    private boolean isGameOver(BoardState boardState) {
        boolean isCheckmate = checkForCheckmate(boardState);
        boolean isStalemate = checkForStalemate(boardState);

        return isCheckmate || isStalemate;
    }

    private boolean checkForCheckmate(BoardState boardState) {
        Square kingSquare = findKingSquare(boardState, boardState.getNextTurn());

        if (!isSquareUnderAttack(boardState, kingSquare, boardState.getNextTurn())) {
            return false; // King is not in check
        }

        List<Square> allSquares = getAllSquares(boardState);

        for (Square target : allSquares) {
            boolean isWhite = boardState.getNextTurn() == Color.WHITE;
            if (chessMoveValidator.isValidMove(PieceType.KING, kingSquare, target, isWhite)
                    && isSquareVacant(boardState.getBoard(), target)) {
                BoardState simulatedState = simulateMove(boardState, kingSquare, target);
                if (!isSquareUnderAttack(simulatedState, findKingSquare(simulatedState, boardState.getNextTurn()),
                        boardState.getNextTurn())) {
                    return false; // Found a move that gets out of check
                }
            }
        }

        return true; // No moves to escape check
    }

    private Square findKingSquare(BoardState boardState, Color playerColor) {
        for (Map.Entry<Square, Piece> entry : boardState.getBoard().entrySet()) {
            if (entry.getValue().getPiece() == PieceType.KING && entry.getValue().getColor() == playerColor) {
                return entry.getKey();
            }
        }

        throw new RuntimeException("King not found on the board.");
    }

    private boolean isSquareUnderAttack(BoardState boardState, Square target, Color playerColor) {
        boolean isWhite = getOpponentColor(playerColor) == Color.WHITE;
        Map<Square, Piece> squares = boardState.getBoard();
        Set<Square> sqSet = squares.keySet();
        for (Square sq : sqSet) {
            PieceType piece = squares.get(sq).getPiece();
            Color clr = squares.get(sq).getColor();
            if (chessMoveValidator.isValidMove(piece, sq, target, isWhite) && playerColor != clr) {
                if (piece == PieceType.BISHOP || piece == PieceType.ROOK || piece == PieceType.QUEEN) {
                    if (!chessMoveValidator.isBlockingPiece(boardState, piece, sq, target)) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }

        return false;
    }

    private List<Square> getAllSquares(BoardState boardState) {
        return new ArrayList<>(boardState.getBoard().keySet());
    }

    private Color getOpponentColor(Color playerColor) {
        return playerColor == Color.WHITE ? Color.BLACK : Color.WHITE;
    }

    private BoardState simulateMove(BoardState boardState, Square source, Square target) {
        BoardState simulatedState = BoardState.builder().board(boardState.getBoard()).build(); // Copy constructor
        simulatedState = updateBoardState(simulatedState, source, target);
        return simulatedState;
    }

    private boolean checkForStalemate(BoardState boardState) {
        Color currentPlayer = boardState.getNextTurn();
        if (isKingInCheck(boardState, currentPlayer)) {
            return false; // Cannot be stalemate if king is in check
        }

        List<Square> allSquares = getAllSquares(boardState);
        boolean isWhite = getOpponentColor(boardState.getNextTurn()) == Color.WHITE;
        for (Square source : allSquares) {
            for (Square target : allSquares) {
                if (chessMoveValidator.isValidMove(PieceType.KING, source, target, isWhite)) {
                    return false; // Found a valid move
                }
            }
        }

        return true; // No valid moves and not in check
    }

    private boolean isKingInCheck(BoardState boardState, Color playerColor) {
        Square kingSquare = findKingSquare(boardState, playerColor);
        return isSquareUnderAttack(boardState, kingSquare, playerColor);
    }

    private boolean isSquareVacant(Map<Square, Piece> board, Square square) {
        return board.get(square).getPiece() == PieceType.ABSENT;
    }
}