package dataAccess;

import chess.ChessGame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class MemoryGameDAO implements GameDAO {
    private static HashSet<GameData> gameDataHashSet;
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
    public void addToGame(String username, ChessGame.TeamColor clientColor, int gameID) {//FIXME: not done

        /*for(GameData game:gameDataHashSet) {
            if(game.gameID() == gameID) {

            }
        }*/
    }

    @Override
    public int createGame(String gameName) {
        int gameID = gameDataHashSet.size() + 1;
        ChessGame newGame = new ChessGame();
        GameData newGameData = new GameData(gameID, "", "", gameName, newGame);
        return gameID;
    }

    @Override
    public Collection<GameData> listGames() {
        HashSet<GameData> games = (HashSet<GameData>) gameDataHashSet.clone();
        return games;
    }
}
