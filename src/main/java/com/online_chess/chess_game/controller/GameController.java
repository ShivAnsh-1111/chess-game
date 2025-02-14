package com.online_chess.chess_game.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.online_chess.chess_game.dto.GameDto;
import com.online_chess.chess_game.dto.GameRequest;
import com.online_chess.chess_game.dto.MoveRequest;
import com.online_chess.chess_game.service.GameService;

import jakarta.validation.Valid;
import lombok.extern.java.Log;


@RestController
@RequestMapping("/game")
@Log
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Starts a new game.
     * @param gameRequest the game request details
     * @return the created game details
     */
    @PostMapping("/start")
    public ResponseEntity<GameDto> startGame(@Valid @RequestBody GameRequest gameRequest) {
        GameDto game = gameService.startGame(gameRequest);
        return ResponseEntity.ok(game);
    }

    /**
     * Makes a move in the game.
     * @param moveRequest the move request details
     * @return the updated game details
     */
    @PostMapping("/move")
    public ResponseEntity<GameDto> makeMove(@Valid @RequestBody MoveRequest moveRequest) {
        GameDto game = gameService.makeMove(moveRequest);
        return ResponseEntity.ok(game);
    }

    /**
     * Retrieves a game by its ID.
     * @param id the game ID
     * @return the game details
     */
    @GetMapping("/publish/{id}")
    public ResponseEntity<GameDto> getGameById(@PathVariable Long id) {
        GameDto game = gameService.getChessGameById(id);
        return ResponseEntity.ok(game);
    }
}