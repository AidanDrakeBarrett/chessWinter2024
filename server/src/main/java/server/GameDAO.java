package server;

import chess.ChessGame;

import java.util.Collection;

public interface GameDAO {
    public void clearData();
    public GameData getGame(String gameID);
    public void addToGame(String authToken, ChessGame.TeamColor clientColor, String gameID);
    public void createGameID(String gameName);
    public Collection<GameData> getGames();
}
