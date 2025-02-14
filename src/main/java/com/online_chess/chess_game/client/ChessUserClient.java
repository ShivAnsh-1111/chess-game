package com.online_chess.chess_game.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.online_chess.chess_game.dto.GameDto;
import com.online_chess.chess_game.dto.UserDto;

/**
 * Feign client for interacting with the Chess User Service.
 */
@FeignClient(name = "chess-user", path = "/user")
public interface ChessUserClient {

    /**
     * Updates the user activity based on the user ID.
     * 
     * @param id the ID of the user
     * @return a ResponseEntity indicating the result of the operation
     */
    @GetMapping("/update/activity/{id}")
    ResponseEntity<Void> updateUserActivity(@PathVariable Long id);

    /**
     * Retrieves a user by their username.
     * 
     * @param username the username of the user
     * @return a ResponseEntity containing the user details
     */
    @GetMapping("/name/{name}")
    ResponseEntity<UserDto> getUserByName(@PathVariable("name") String username);

    /**
     * Retrieves the game details for a user based on the game ID.
     * 
     * @param id the ID of the game
     * @return a ResponseEntity containing the game details
     */
    @GetMapping("/game/{id}")
    ResponseEntity<GameDto> getUserGame(@PathVariable("id") Long id);

    /**
     * Saves the game details for a user.
     * 
     * @param game the game details to save
     * @return a ResponseEntity containing the saved game details
     */
    @PostMapping("/game/save")
    ResponseEntity<GameDto> saveUserGame(@RequestBody GameDto game);
}