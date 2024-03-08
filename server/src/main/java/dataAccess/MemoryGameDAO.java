package dataAccess;

import chess.ChessGame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class MemoryGameDAO implements GameDAO {
    private static HashSet<GameData> gameDataHashSet = new HashSet<>();
    @Override
    public void clearData() {
        gameDataHashSet.clear();
    }

    @Override
    public GameData getGame(int gameID) {
        for(GameData game:gameDataHashSet) {
            if(game.gameID() == gameID) {
                return game;
            }
        }
        return null;
    }

    @Override
    public void joinGame(String username, ChessGame.TeamColor clientColor, int gameID) throws DataAccessException {
        for(GameData game: gameDataHashSet) {
            if(game.gameID() == gameID) {
                if(clientColor == ChessGame.TeamColor.WHITE) {
                    if(game.whiteUsername() != null) {
                        throw new DataAccessException("Error: already taken");
                    }
                    GameData newGame = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.chessGame());
                    gameDataHashSet.remove(game);
                    gameDataHashSet.add(newGame);
                    return;
                }
                if(clientColor == ChessGame.TeamColor.BLACK) {
                    if(game.blackUsername() != null) {
                        throw new DataAccessException("Error: already taken");
                    }
                    GameData newGame = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.chessGame());
                    gameDataHashSet.remove(game);
                    gameDataHashSet.add(newGame);
                    return;
                }
            }
        }
        throw new DataAccessException("Error: bad request");
    }

    @Override
    public int createGame(String gameName) {
        int gameID = gameDataHashSet.size() + 1;
        ChessGame newGame = new ChessGame();
        GameData newGameData = new GameData(gameID, "", "", gameName, newGame);
        gameDataHashSet.add(newGameData);
        return gameID;
    }

    @Override
    public Collection<GameData> listGames() {
        HashSet<GameData> games = (HashSet<GameData>) gameDataHashSet.clone();
        return games;
    }
}
