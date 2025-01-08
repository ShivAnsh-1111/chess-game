package com.online_chess.chess_game.serialize;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.online_chess.chess_game.component.Square;

public class SquareKeySerializer extends com.fasterxml.jackson.databind.JsonSerializer<Square> {
    @Override
    public void serialize(Square square, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeFieldName(square.toString()); // Serialize key as "x,y"
    }
}