package com.online_chess.chess_game.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_chess.chess_game.dto.GameDto;
import com.online_chess.chess_game.dto.GameRequest;
import com.online_chess.chess_game.dto.MoveRequest;
import com.online_chess.chess_game.service.GameService;

import lombok.extern.java.Log;

@RestController
@RequestMapping("/game")
@Log
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/start")
    public ResponseEntity<GameDto> startGame(@RequestBody GameRequest gameRequest) {
    	
        GameDto game = gameService.startGame(gameRequest);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/move")
    public ResponseEntity<?> makeMove(@RequestBody MoveRequest moveRequest) {
    	
    	GameDto game = gameService.makeMove(moveRequest);
    	return ResponseEntity.ok(game);

    }
    
    @GetMapping("/publish/{id}")
    public ResponseEntity<?> getGameById(@PathVariable Long id) {
    	GameDto game = gameService.getChessGameById(id);
    	return ResponseEntity.ok(game);
    }
}		
