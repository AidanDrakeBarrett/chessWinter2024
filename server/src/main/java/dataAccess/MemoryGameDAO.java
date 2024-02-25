package dataAccess;

import chess.ChessGame;

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
    public void createGameID(String gameName) {

    }

    @Override
    public Collection<GameData> getGames() {
        return null;
    }
}
