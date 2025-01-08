package com.online_chess.chess_game.controller;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_chess.chess_game.client.ChessUserClient;
import com.online_chess.chess_game.dto.GameDto;
import com.online_chess.chess_game.dto.GameRequest;
import com.online_chess.chess_game.dto.MoveRequest;
import com.online_chess.chess_game.entity.Game;
import com.online_chess.chess_game.service.GameService;

import lombok.extern.java.Log;

@RestController
@CrossOrigin
@RequestMapping("/game")
@Log
public class GameController {

    @Autowired
    private GameService gameService;
    
    @Autowired
    private ChessUserClient chessUserClient;
    

    @PostMapping("/start")
    public ResponseEntity<?> startGame(@RequestBody GameRequest gameRequest) {
    	
        Game game = gameService.startGame(gameRequest);
        chessUserClient.updateUserActivity(gameRequest.getPlayer1Id());
        chessUserClient.updateUserActivity(gameRequest.getPlayer2Id());
        
        return ResponseEntity.ok(game.toGameDto());
    }

    @PostMapping("/move")
    public ResponseEntity<?> makeMove(@RequestBody MoveRequest moveRequest) {
    	
    	Game game = gameService.makeMove(moveRequest);
    	
    	ResponseEntity<?> user =chessUserClient.getUserByName(moveRequest.getPlayer());
    	Long id = Long.valueOf(((LinkedHashMap<?, ?>)user.getBody()).get("id").toString());
    	chessUserClient.updateUserActivity(id);
    	
    	return ResponseEntity.ok(game.toGameDto());

    }
    
    @GetMapping("/publish/{id}")
    public ResponseEntity<?> getGameById(@PathVariable Long id) {
    	GameDto game = gameService.getChessGameById(id);
    	return ResponseEntity.ok(game);
    }
}		
