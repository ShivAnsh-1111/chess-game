package com.online_chess.chess_game.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.online_chess.chess_game.dto.GameDto;

@FeignClient(name="chess-user", path="/user")
public interface ChessUserClient {
	
	@GetMapping("/update/activity/{id}")
	public ResponseEntity<?> updateUserActivity(@PathVariable Long id);
	
	@GetMapping("/name/{name}")
	public ResponseEntity<?> getUserByName(@PathVariable("name") String username);
	
	@GetMapping("/game/{id}")
	public ResponseEntity<GameDto> getUserGame(@PathVariable("id") Long id);
	
	@PostMapping("/game/save")
	public ResponseEntity<GameDto> saveUserGame(@RequestBody GameDto game);

}
