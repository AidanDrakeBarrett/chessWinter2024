package serviceTests;

import dataAccess.AbbreviatedGameData;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import org.junit.jupiter.api.Test;
import server.ResponseException;
import service.GamesService;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class GamesServiceTest {
    private GamesService service = new GamesService();
    private MemoryGameDAO gameDAO = new MemoryGameDAO();
    private MemoryAuthDAO authDAO = new MemoryAuthDAO();

    @Test
    void listGames() throws ResponseException {//FIXME: LESDOTHIZZZ
        String gameName = "game";
        int id = gameDAO.createGame(gameName);
        AbbreviatedGameData abbreviatedGameData = new AbbreviatedGameData(id, null, null, gameName);
        ArrayList<AbbreviatedGameData> abbreviations = new ArrayList<>();
        abbreviations.add(abbreviatedGameData);

        String username = "username";
        String authToken = authDAO.createAuth(username).username();
        assertEquals(abbreviations, service.listGames(authToken));
    }

    @Test
    void createGame() {
    }

    @Test
    void joinGame() {
    }
}