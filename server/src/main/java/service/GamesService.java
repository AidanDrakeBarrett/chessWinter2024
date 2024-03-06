package service;

import chess.ChessGame;
import dataAccess.*;
import server.ResponseException;

import java.util.Collection;

public class GamesService {
    private static MemoryAuthDAO authDAO = new MemoryAuthDAO();
    private static MemoryGameDAO gameDAO = new MemoryGameDAO();

    public GamesService() {}

    public static Collection<GameData> listGames(String authToken) throws ResponseException {//FIXME: EXCEPTIONS
        try {
            if(authDAO.containsAuth(authToken)) {
                return gameDAO.listGames();
            }
        } catch(DataAccessException e) {
            throw new ResponseException(401, "");
        }
        /*if(authDAO.containsAuth(userAuth) == userAuth) {
            return gameDAO.listGames();
        }
        return null;*/
    }
    public static Object createGame(AuthData userAuth, String gameName) {//FIXME: EXCEPTIONS
        if(authDAO.containsAuth(userAuth) == userAuth) {
            return gameDAO.createGame(gameName);
        }
        return null;
    }
    public void joinGame(AuthData userAuth, ChessGame.TeamColor playerColor, int gameID) {//FIXME: EXCEPTIONS i do indeed despise the tediousness and poorly explained dynamics of building a chess server in the specs for this phase, but I must continue.
        if(authDAO.containsAuth(userAuth) == userAuth) {
            gameDAO.joinGame(userAuth.username(), playerColor, gameID);
        }
    }/*unique request class for the specific function. There's not a specific object to parse for this*/
}
