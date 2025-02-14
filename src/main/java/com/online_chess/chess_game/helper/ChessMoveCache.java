package com.online_chess.chess_game.helper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.online_chess.chess_game.dto.GameDto;
import com.online_chess.chess_game.dto.MoveRequest;

@Service
public class ChessMoveCache {
	
	Map<String, GameDto> chessCache = new ConcurrentHashMap<>();
	
	public void startInCache(GameDto game) {
		
        String gameId = game.getPlayerTurn();
        chessCache.put(gameId, game);
		
	}
	
	public GameDto moveInCache(MoveRequest moveRequest) {
		
		 return chessCache.get(moveRequest.getPlayer());
		
	}
	
	public void clearChessCache(String gameId) {
		chessCache.remove(gameId);
	}

}
