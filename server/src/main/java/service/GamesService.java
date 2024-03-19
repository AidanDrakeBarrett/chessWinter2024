package service;

import chess.ChessGame;
import dataAccess.*;
import server.ResponseException;

import java.util.Collection;
import java.util.Objects;

public class GamesService {
    private static MemoryAuthDAO authDAO = new MemoryAuthDAO();
    private static MemoryGameDAO gameDAO = new MemoryGameDAO();

    public GamesService() {}

    public static Collection<AbbreviatedGameData> listGames(String authToken) throws ResponseException {
        try {
            if(authDAO.containsAuth(authToken)) {
                return gameDAO.listGames();
            }
        } catch(DataAccessException e) {
            throw new ResponseException(401, "");
        }
        return null;
    }
    public static Object createGame(String authToken, String gameName) throws ResponseException {
        try {
            if (authDAO.containsAuth(authToken)) {
                return gameDAO.createGame(gameName);
            }
        } catch(DataAccessException e) {
            throw new ResponseException(401, "");
        }
        return null;
    }
    public void joinGame(String authToken, ChessGame.TeamColor playerColor, int gameID) throws ResponseException {
        try {
            if (authDAO.containsAuth(authToken)) {
                String username = authDAO.getUsername(authToken);
                gameDAO.joinGame(username, playerColor, gameID);
            }
        } catch(DataAccessException e) {
            if(Objects.equals(e.getMessage(), "Error: unauthorized")) {
                throw new ResponseException(401, "Error: unauthorized");
            }
            if(Objects.equals(e.getMessage(), "Error: already taken")) {
                throw new ResponseException(403, "Error: already taken");
            }
            if(Objects.equals(e.getMessage(), "Error: bad request")) {
                throw new ResponseException(400, "Error: bad request");
            }
        }
    }/*unique request class for the specific function. There's not a specific object to parse for this*/
}
