package com.online_chess.chess_game.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.online_chess.chess_game.client.ChessUserClient;
import com.online_chess.chess_game.component.BoardState;
import com.online_chess.chess_game.component.Color;
import com.online_chess.chess_game.component.Piece;
import com.online_chess.chess_game.component.PieceType;
import com.online_chess.chess_game.component.Square;
import com.online_chess.chess_game.dto.GameDto;
import com.online_chess.chess_game.dto.GameRequest;
import com.online_chess.chess_game.dto.MoveRequest;
import com.online_chess.chess_game.exception.CustomException;
import com.online_chess.chess_game.kafka.ChessKafkaPublisher;

import lombok.extern.java.Log;

@Service
@Log
public class GameService {
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
   
    @Autowired
    private ChessMoveValidator chessMoveValidator;
    
    @Autowired
    private ChessUserClient chessUserClient;
    
    @Autowired
    private ChessMoveCache chessMoveCache;
    
    
    @Autowired
    private ChessKafkaPublisher chessKafkaPublisher;

    public GameDto startGame(GameRequest gameRequest) {
    
        BoardState boardState = initializeBoard();
        boardState.setNextTurn(Color.WHITE);
        String boardStateStr = "";
		try {
			boardStateStr = objectMapper.writeValueAsString(boardState);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			throw new CustomException(e.getMessage());
			
		}
        GameDto gameDto = GameDto.builder().boardState(boardStateStr).playerTurn(
        		gameRequest.getPlayer1Id().toString()+"VS"+gameRequest.getPlayer2Id()).status("ongoing").build();
        
		ResponseEntity<GameDto> savedGame =chessUserClient.saveUserGame(gameDto);
		chessUserClient.updateUserActivity(gameRequest.getPlayer1Id());
		chessUserClient.updateUserActivity(gameRequest.getPlayer2Id()); 
		chessMoveCache.startInCache(savedGame.getBody());
		return savedGame.getBody();
		 
    }

    

	public GameDto makeMove(MoveRequest moveRequest) {
		
		GameDto gameDto = GameDto.builder().build();
		if(moveRequest.getGameId() != null) {
			gameDto = chessMoveCache.moveInCache(moveRequest);
		}
        
		
		if (!gameDto.getStatus().equals("ongoing")) { 
			throw new CustomException("Game is not ongoing"); 
		}
		
        try {
	        	
        	BoardState boardstateObj = objectMapper.readValue(gameDto.getBoardState(), BoardState.class);
        	List<String> moveDet = getMoveDetails(moveRequest);

        	Color curCol = objectMapper.readValue(moveDet.get(1), boolean.class)? Color.WHITE : Color.BLACK;
	        if (curCol != boardstateObj.getNextTurn()) {
	            throw new CustomException("It's not your turn");
	        }
        
        
	        
	        boolean moveValidation = isValidMove(boardstateObj, moveRequest);
	        if(!moveValidation) {
	        	throw new CustomException("Invalid move");
	        }
		        
	        BoardState updatedBoardState = applyMove(boardstateObj, moveDet);
	        
	        // Check if the game is over (e.g., checkmate, stalemate, etc.)
	        if (isGameOver(updatedBoardState)) {
	            gameDto.setStatus("finished");
	        }
	        
	        log.info("UpdatedBoard:"+updatedBoardState);
	        gameDto.setBoardState(objectMapper.writeValueAsString(updatedBoardState));
	        
        } catch (Exception ex) {
        	throw new CustomException(ex.getMessage());
        }
        
        if(!gameDto.getStatus().equals("ongoing")) {
        	ResponseEntity<GameDto> game = chessUserClient.saveUserGame(gameDto);
        	String[] players = moveRequest.getPlayer().split("VS");
        	chessUserClient.updateUserActivity(Long.valueOf(players[1]));
        	chessMoveCache.clearChessCache(moveRequest.getPlayer());
        	gameDto = game.getBody();
        } else {
        	chessMoveCache.startInCache(gameDto);
        }

        return gameDto;
    }

    private BoardState applyMove(BoardState boardState, List<String> moveDetails) {

		String[] move=moveDetails.get(2).split("");
        Square current = Square.builder().x(Integer.valueOf(move[1])).y(Integer.valueOf(move[2])).build();
        int t1=3, t2=4;
        if(move.length > 5) {
        	t1=4; t2=5;
        }
        Square target = Square.builder().x(Integer.valueOf(move[t1])).y(Integer.valueOf(move[t2])).build();
		boardState =updateBoardState(boardState, current, target);
		
        return boardState;
    }

    private boolean isGameOver(BoardState boardState) {

        // Check for checkmate, stalemate, or insufficient material
        boolean isCheckmate = checkForCheckmate(boardState);
        boolean isStalemate = checkForStalemate(boardState);

        return isCheckmate || isStalemate;
    }

    private BoardState updateBoardState(BoardState board, Square source, Square destination) {
        // Implement logic to update the board state
    	
    	Map<Square,Piece> currentBoard= board.getBoard();
    	
    	//source update
    	Piece pieceToUpdate = currentBoard.remove(source);
    	Piece newPiece = Piece.builder().piece(PieceType.ABSENT).color(Color.ABSENT).build();
    	currentBoard.put(source, newPiece);
    	
    	//destination update
    	currentBoard.remove(destination);
    	currentBoard.put(destination, pieceToUpdate);
    	log.info("after"+currentBoard);
    	
    	Color nextTurn = board.getNextTurn() == Color.WHITE ? Color.BLACK : Color.WHITE;
    	board.setNextTurn(nextTurn);
    	
    	board.setBoard(currentBoard);
    	return board;
    	
    }
    
    private BoardState initializeBoard() {
		// TODO Auto-generated method stub
    	Map<Square,Piece> board = new ConcurrentHashMap<>();
    	
    	for(int i=0; i < 8; i++) {
    		for(int j=0; j < 8; j++) {
    			if(i == 1 ) {
    				board.put(Square.builder().x(i).y(j).build(), 
    						Piece.builder().piece(PieceType.PAWN).color(Color.BLACK).build());
    			} else if(i == 6 ){
    				board.put(Square.builder().x(i).y(j).build(), 
    						Piece.builder().piece(PieceType.PAWN).color(Color.WHITE).build());
    			} else if(1< i && i < 6) {
    				board.put(Square.builder().x(i).y(j).build(), 
    						Piece.builder().piece(PieceType.ABSENT).color(Color.ABSENT).build());
    			} else {
    				if(i==0 && (j == 0  || j == 7)) {
    					board.put(Square.builder().x(i).y(j).build(), 
        							Piece.builder().piece(PieceType.ROOK).color(Color.BLACK).build()); 
    						
    				} else if(i==7 && (j == 0  || j == 7)){
    					board.put(Square.builder().x(i).y(j).build(), 
	    						Piece.builder().piece(PieceType.ROOK).color(Color.WHITE).build());
    				}
    				
    				if(i==0 && (j == 1  || j == 6)) {
    					board.put(Square.builder().x(i).y(j).build(), 
        							Piece.builder().piece(PieceType.KNIGHT).color(Color.BLACK).build()); 
    						
    				} else if(i==7 && (j == 1  || j == 6)){
    					board.put(Square.builder().x(i).y(j).build(), 
	    						Piece.builder().piece(PieceType.KNIGHT).color(Color.WHITE).build());
    				}
    				
    				if(i==0 && (j == 2  || j == 5)) {
    					board.put(Square.builder().x(i).y(j).build(), 
        							Piece.builder().piece(PieceType.BISHOP).color(Color.BLACK).build()); 
    						
    				} else if(i==7 && (j == 2  || j == 5)){
    					board.put(Square.builder().x(i).y(j).build(), 
	    						Piece.builder().piece(PieceType.BISHOP).color(Color.WHITE).build());
    				}
    				
    				if(i==0 && j==3) {
    					board.put(Square.builder().x(i).y(j).build(), 
        							Piece.builder().piece(PieceType.QUEEN).color(Color.BLACK).build()); 
    						
    				} else if(i==7 && j==3){
    					board.put(Square.builder().x(i).y(j).build(), 
	    						Piece.builder().piece(PieceType.QUEEN).color(Color.WHITE).build());
    				}
    				
    				if(i==0 && j==4) {
    					board.put(Square.builder().x(i).y(j).build(), 
        							Piece.builder().piece(PieceType.KING).color(Color.BLACK).build()); 
    						
    				} else if(i==7 && j==4){
    					board.put(Square.builder().x(i).y(j).build(), 
	    						Piece.builder().piece(PieceType.KING).color(Color.WHITE).build());
    				}
    			}
    			
    		}
    		
    	}
    	log.info("Initialized board: "+board);
    	
    	BoardState boardState = BoardState.builder().board(board).build(); 
		return boardState;
	}
    
public List<String> getMoveDetails(MoveRequest moveRequest) {
		
		//Optional<Game> game = gameRepository.findById();
		ResponseEntity<GameDto> game = chessUserClient.getUserGame(moveRequest.getGameId());
		PieceType p = PieceType.ABSENT;
		Color c = Color.ABSENT;
		String[] move = moveRequest.getMove().split("");
		String m = move[0];
		
		if(game.hasBody() ) {
			
			switch(m) {
				case "p" : p = PieceType.PAWN;
							c = Color.BLACK;
				break;
				case "r": p = PieceType.ROOK;
							c = Color.BLACK;
				break;
				
				case "n": p = PieceType.KNIGHT;
							c = Color.BLACK;
				break;
				
				case "b": p = PieceType.BISHOP;
							c = Color.BLACK;
				break;
				
				case "q": p = PieceType.QUEEN;
							c = Color.BLACK;
				break;
				
				case "k": p = PieceType.KING;
							c = Color.BLACK;
				break;
				
				case "P" : p = PieceType.PAWN;
							c = Color.WHITE;
				break;
				case "R": p = PieceType.ROOK;
							c = Color.WHITE;
				break;
				
				case "N": p = PieceType.KNIGHT;
							c = Color.WHITE;
				break;
				
				case "B": p = PieceType.BISHOP;
							c = Color.WHITE;
				break;
				
				case "Q": p = PieceType.QUEEN;
							c = Color.WHITE;
				break;
				
				case "K": p = PieceType.KING;
							c = Color.WHITE;
				break;
				default:

			}
		}
		boolean isWhite = c == Color.WHITE ? true : false;
		List<String> moveDetails = new ArrayList<>();
		try {
			moveDetails.add(objectMapper.writeValueAsString(p));
			moveDetails.add(objectMapper.writeValueAsString(isWhite));
			moveDetails.add(moveRequest.getMove());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			throw new CustomException(e.getMessage());
		}
		
		
		return moveDetails;
	}

		private boolean checkForCheckmate(BoardState boardState) {
			
			Square kingSquare = findKingSquare(boardState, boardState.getNextTurn());
		
		    if (!isSquareUnderAttack(boardState, kingSquare, boardState.getNextTurn())) {
		        return false; // King is not in check
		    }
		
		    List<Square> allSquares = getAllSquares(boardState);
		
		    
	        for (Square target : allSquares) {
	        	boolean isWhite = boardState.getNextTurn() == Color.WHITE ? true : false;
	            if (chessMoveValidator.isValidMove(PieceType.KING, kingSquare, target, isWhite) && isSquareVacant(boardState.getBoard(), target)) {
	                BoardState simulatedState = simulateMove(boardState, kingSquare, target);
	                if (!isSquareUnderAttack(simulatedState, findKingSquare(simulatedState,  boardState.getNextTurn()),  boardState.getNextTurn())) {
	                    return false; // Found a move that gets out of check
	                }
	            }
	        }
		    	
		    return true; // No moves to escape check
		}
		
		// Helper Methods
		private Square findKingSquare(BoardState boardState, Color playerColor) {
		    
		        for (Map.Entry<Square, Piece> entry : boardState.getBoard().entrySet()) {
		            if (entry.getValue().getPiece() == PieceType.KING && entry.getValue().getColor() == playerColor) {
		                return entry.getKey();
		            }
		        }
		    
		    throw new RuntimeException("King not found on the board.");
		}
		
		private boolean isSquareUnderAttack(BoardState boardState, Square target, Color playerColor) {
		
			boolean isWhite = getOpponentColor(playerColor) == Color.WHITE ? true : false;
		    Map<Square,Piece> squares = boardState.getBoard();
		    Set<Square> sqSet = squares.keySet();
		    for(Square sq : sqSet) {
		    	PieceType piece = squares.get(sq).getPiece();
		    	Color clr = squares.get(sq).getColor();
		    	if(chessMoveValidator.isValidMove(piece, sq, target, isWhite)
		    			&& playerColor != clr) {
		    		if(piece == PieceType.BISHOP || piece == PieceType.ROOK || piece == PieceType.QUEEN) {
				        if(!chessMoveValidator.isBlockingPiece(boardState, piece, sq, target)) {
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
			List<Square> allSquares = new ArrayList<>();
		    for (Square sq : boardState.getBoard().keySet()) {
		        allSquares.add(sq);
		    }
		    return allSquares;
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
		    boolean isWhite = getOpponentColor(boardState.getNextTurn()) == Color.WHITE ? true : false;
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
		

		private boolean isValidMove(BoardState boardState, MoveRequest move){
			
			List<String> moveDetails = getMoveDetails(move);
	        PieceType piece;
			try {
				
				piece = objectMapper.readValue(moveDetails.get(0), PieceType.class);
		        boolean isWhite = objectMapper.readValue(moveDetails.get(1), Boolean.class);
		        
		        String[] moves=move.getMove().split("");
		        Square current = Square.builder().x(Integer.valueOf(moves[1])).y(Integer.valueOf(moves[2])).build();
		        int t1=3, t2=4;
		        if(moves.length > 5) {
		        	t1=4; t2=5;
		        }
		        Square target = Square.builder().x(Integer.valueOf(moves[t1])).y(Integer.valueOf(moves[t2])).build();
		        
		        //Basic validation
		        if(!chessMoveValidator.isValidMove(piece, current, target, isWhite)) {
		        	return false;
		        }
		        
		        //Blocking piece validation
		        if(piece == PieceType.BISHOP || piece == PieceType.ROOK || piece == PieceType.QUEEN) {
			        if(chessMoveValidator.isBlockingPiece(boardState, piece, current, target)) {
			        	return false;
			        }
		        }
		        
		        
		        
			} catch(Exception ex) {
				throw new CustomException(ex.getMessage());
			}
			
			return true;
		}
		
		private boolean isSquareVacant(Map<Square,Piece> board, Square square) {
			return board.get(square).getPiece() == PieceType.ABSENT;
		}
		
		public GameDto getChessGameById(Long id) {
			ResponseEntity<GameDto> game;
			try {
			game = chessUserClient.getUserGame(id);
			chessKafkaPublisher.streamMatch(game.getBody().toString());
			} catch(Exception ex) {
				throw new CustomException("Game not found");
			}
			return game.getBody();
		}

}
