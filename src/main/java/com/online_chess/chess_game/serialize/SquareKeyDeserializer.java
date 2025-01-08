package com.online_chess.chess_game.serialize;

import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.online_chess.chess_game.component.Square;

public class SquareKeyDeserializer extends KeyDeserializer {
    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        String[] parts = key.split(",");
        return new Square(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])); // Deserialize "x,y" back to Square
    }
}
