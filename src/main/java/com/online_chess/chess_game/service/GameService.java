package com.online_chess.chess_game.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online_chess.chess_game.annotation.LogExecutionTime;
import com.online_chess.chess_game.client.ChessUserClient;
import com.online_chess.chess_game.component.BoardState;
import com.online_chess.chess_game.component.Color;
import com.online_chess.chess_game.dto.GameDto;
import com.online_chess.chess_game.dto.GameRequest;
import com.online_chess.chess_game.dto.MoveRequest;
import com.online_chess.chess_game.exception.CustomException;
import com.online_chess.chess_game.kafka.ChessKafkaPublisher;
import com.online_chess.chess_game.helper.ChessMoveValidator;
import com.online_chess.chess_game.helper.GameHelper;
import com.online_chess.chess_game.helper.ChessMoveCacheSingleton;

import lombok.extern.java.Log;

@Service
@Log
public class GameService {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String GAME_NOT_ONGOING = "Game is not ongoing";
    private static final String GAME_NOT_FOUND = "Game not found";

    private final ChessUserClient chessUserClient;
    private final ChessKafkaPublisher chessKafkaPublisher;
    private final GameHelper gameHelper;

    private final ChessMoveCacheSingleton chessMoveCache=ChessMoveCacheSingleton.getInstance();

    @Autowired
    public GameService(ChessMoveValidator chessMoveValidator, ChessUserClient chessUserClient,
                       ChessKafkaPublisher chessKafkaPublisher, GameHelper gameHelper) {
        this.chessUserClient = chessUserClient;
        this.chessKafkaPublisher = chessKafkaPublisher;
        this.gameHelper = gameHelper;
    }

    @LogExecutionTime
    public GameDto startGame(GameRequest gameRequest) {
        BoardState boardState = gameHelper.initializeBoard();
        boardState.setNextTurn(Color.WHITE);
        String boardStateStr = gameHelper.serializeBoardState(boardState);

        GameDto gameDto = GameDto.builder()
                .boardState(boardStateStr)
                .playerTurn(gameRequest.getPlayer1Id() + "VS" + gameRequest.getPlayer2Id())
                .status("ongoing")
                .build();

        ResponseEntity<GameDto> savedGame = chessUserClient.saveUserGame(gameDto);
        updatePlayersActivity(gameRequest);
        chessMoveCache.startInCache(savedGame.getBody());
        return savedGame.getBody();
    }

    @LogExecutionTime
    public GameDto makeMove(MoveRequest moveRequest) {
        GameDto gameDto = getGameDtoFromCache(moveRequest);

        if (!"ongoing".equals(gameDto.getStatus())) {
            throw new CustomException(GAME_NOT_ONGOING);
        }

        try {
            BoardState boardState = objectMapper.readValue(gameDto.getBoardState(), BoardState.class);
            List<String> moveDetails = gameHelper.getMoveDetails(moveRequest);

            gameHelper.validateTurn(boardState, moveDetails);
            gameHelper.validateMove(boardState, moveRequest);

            BoardState updatedBoardState = gameHelper.applyMove(boardState, moveDetails);
            gameHelper.updateGameStatus(gameDto, updatedBoardState);

            gameDto.setBoardState(objectMapper.writeValueAsString(updatedBoardState));
        } catch (Exception ex) {
            throw new CustomException(ex.getMessage());
        }

        return finalizeMove(gameDto, moveRequest);
    }

    @SuppressWarnings("null")
    public GameDto getChessGameById(Long id) {
        ResponseEntity<GameDto> game;
        try {
            game = chessUserClient.getUserGame(id);
            if (game.getBody() == null) {
                throw new CustomException(GAME_NOT_FOUND);
            }
            chessKafkaPublisher.streamMatch(game.getBody().toString());
        } catch (Exception ex) {
            throw new CustomException(GAME_NOT_FOUND);
        }
        return game.getBody();
    }

    private void updatePlayersActivity(GameRequest gameRequest) {
        chessUserClient.updateUserActivity(gameRequest.getPlayer1Id());
        chessUserClient.updateUserActivity(gameRequest.getPlayer2Id());
    }

    private GameDto getGameDtoFromCache(MoveRequest moveRequest) {
        if (moveRequest.getGameId() != null) {
            return chessMoveCache.moveInCache(moveRequest);
        }
        return GameDto.builder().build();
    }

    private GameDto finalizeMove(GameDto gameDto, MoveRequest moveRequest) {
        if (!"ongoing".equals(gameDto.getStatus())) {
            ResponseEntity<GameDto> game = chessUserClient.saveUserGame(gameDto);
            String[] players = moveRequest.getPlayer().split("VS");
            chessUserClient.updateUserActivity(Long.valueOf(players[1]));
            chessMoveCache.clearChessCache(moveRequest.getPlayer());
            return game.getBody();
        } else {
            chessMoveCache.startInCache(gameDto);
            return gameDto;
        }
    }
}