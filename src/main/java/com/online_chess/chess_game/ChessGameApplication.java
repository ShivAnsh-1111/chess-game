package com.online_chess.chess_game;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EntityScan(basePackages = {"com.online_chess.chess_game.entity"})
public class ChessGameApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChessGameApplication.class, args);
	}

}
