package dataAccess;

import chess.ChessGame;

import java.util.Collection;

public interface GameDAO {
    public void clearData();
    public GameData getGame(int gameID);
    public void addToGame(String username, ChessGame.TeamColor clientColor, int gameID);
    public void createGameID(String gameName);
    public Collection<GameData> getGames();
}
