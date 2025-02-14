package com.online_chess.chess_game.helper;

import com.online_chess.chess_game.component.BoardState;
import com.online_chess.chess_game.dto.MoveRequest;
import com.online_chess.chess_game.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameHelperTests {

    @Mock
    private ChessMoveValidator chessMoveValidator;

    @InjectMocks
    private GameHelper gameHelper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInitializeBoard() {
        BoardState boardState = gameHelper.initializeBoard();
        assertNotNull(boardState, "BoardState should not be null after initialization");
        // Add more assertions to verify the initial state of the board
    }

    @Test
    void testValidateMove_ValidMove() {
        MoveRequest moveRequest = new MoveRequest();
        // Set up a valid move request
        when(chessMoveValidator.isValidMove(any(), any(), any(), anyBoolean())).thenReturn(true);

        assertDoesNotThrow(() -> gameHelper.validateMove(new BoardState(), moveRequest));
    }

    @Test
    void testValidateMove_InvalidMove() {
        MoveRequest moveRequest = new MoveRequest();
        // Set up an invalid move request
        when(chessMoveValidator.isValidMove(any(), any(), any(), anyBoolean())).thenReturn(false);

        assertThrows(CustomException.class, () -> gameHelper.validateMove(new BoardState(), moveRequest));
    }

    // Add more test cases for other methods in GameHelper
}