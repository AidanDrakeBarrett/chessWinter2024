package serviceTests;

import chess.ChessGame;
import dataAccess.AbbreviatedGameData;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import server.ResponseException;
import service.GamesService;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class GamesServiceTest {
    private final GamesService service = new GamesService();
    private final MemoryGameDAO gameDAO = new MemoryGameDAO();
    private final MemoryAuthDAO authDAO = new MemoryAuthDAO();

    @AfterEach
    void tearDown() {
        gameDAO.clearData();
        authDAO.clearData();
    }

    @Test
    void listGames() throws ResponseException {
        String gameName = "game";
        int id = gameDAO.createGame(gameName);
        AbbreviatedGameData abbreviatedGameData = new AbbreviatedGameData(id, null, null, gameName);
        ArrayList<AbbreviatedGameData> abbreviations = new ArrayList<>();
        abbreviations.add(abbreviatedGameData);

        String username = "username";
        String authToken = authDAO.createAuth(username).authToken();
        assertEquals(abbreviations, GamesService.listGames(authToken));
        assertThrows(ResponseException.class, ()->GamesService.listGames("username"));
    }

    @Test
    void createGame() {
        try {
            String gameName = "game";
            String newGameName = "game2";
            String username = "user";
            String authToken = authDAO.createAuth(username).authToken();

            assertEquals(1, GamesService.createGame(authToken, gameName));
            assertNotEquals(1, GamesService.createGame(authToken, newGameName));
        } catch(ResponseException resEx) {
            fail("Caught unexpected response exception");
        }
    }

    @Test
    void joinGame() throws ResponseException {
        String user1 = "user1";
        String user2 = "user2";
        String gameName = "game";
        String authToken1 = authDAO.createAuth(user1).authToken();
        String authToken2 = authDAO.createAuth(user2).authToken();
        int gameID = gameDAO.createGame(gameName);

        service.joinGame(authToken1, ChessGame.TeamColor.WHITE, gameID);
        assertEquals(user1, ((ArrayList<AbbreviatedGameData>) gameDAO.listGames()).getFirst().whiteUsername());
        assertThrows(ResponseException.class, ()->service.joinGame(authToken2, ChessGame.TeamColor.WHITE, gameID));
    }
}