package com.online_chess.chess_game.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChessKafkaPublisher {
	
    private static final String TOPIC = "chess.match";
    private static final String GroupId = "game";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
	
	public void streamMatch(String msg) {
		kafkaTemplate.send(TOPIC, msg, GroupId);
		
	}

}
