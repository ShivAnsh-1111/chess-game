package com.online_chess.chess_game.helper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.online_chess.chess_game.dto.GameDto;
import com.online_chess.chess_game.dto.MoveRequest;

//Singleton class
public final class ChessMoveCacheSingleton {
	
	private static ChessMoveCacheSingleton instance;
    private final Map<String, GameDto> chessCache;

    private ChessMoveCacheSingleton() {
        chessCache = new ConcurrentHashMap<>();
    }

    public static synchronized ChessMoveCacheSingleton getInstance() {
        if (instance == null) {
            instance = new ChessMoveCacheSingleton();
        }
        return instance;
    }
	
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
