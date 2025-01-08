package com.online_chess.chess_game.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.online_chess.chess_game.entity.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    Optional<Game> findTopByOrderByIdDesc();

	Game getGameById(Long id);
}
