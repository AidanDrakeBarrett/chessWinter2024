package service;

import chess.ChessGame;
import dataAccess.AuthData;
import dataAccess.GameData;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;

import java.util.Collection;

public class GamesService {
    private static MemoryAuthDAO authDAO;
    private static MemoryGameDAO gameDAO;

    public GamesService() {}

    public static Collection<GameData> listGames(AuthData userAuth) {//FIXME: EXCEPTIONS
        if(authDAO.containsAuth(userAuth) == userAuth) {
            return gameDAO.listGames();
        }
        return null;
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
    }
}
