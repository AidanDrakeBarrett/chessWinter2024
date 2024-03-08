package dataAccess;

import chess.ChessGame;

import java.util.Collection;

public interface GameDAO {
    public void clearData();
    public GameData getGame(int gameID);
    public void joinGame(String username, ChessGame.TeamColor clientColor, int gameID) throws DataAccessException;
    public int createGame(String gameName);
    public Collection<GameData> listGames();
}
