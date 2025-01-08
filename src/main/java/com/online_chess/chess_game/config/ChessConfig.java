package com.online_chess.chess_game.config;

import org.springframework.context.annotation.Bean;

import feign.Logger;

public class ChessConfig {
	
	@Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL; // FULL logs the entire request and response, including URLs.
    }

}
