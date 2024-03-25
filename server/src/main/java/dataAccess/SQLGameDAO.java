/*package dataAccess;

import chess.ChessGame;

import com.google.gson.Gson;
import server.ResponseException;

import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;
public class SQLGameDAO implements GameDAO{
    public SQLGameDAO() {
        configureDatabase();
    }
    @Override
    public void clearData() {

    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public void joinGame(String username, ChessGame.TeamColor clientColor, int gameID) throws DataAccessException {

    }

    @Override
    public int createGame(String gameName) {
        return 0;
    }

    @Override
    public Collection<AbbreviatedGameData> listGames() {
        return null;
    }
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS game(
                '
            """
    }
    private void configureDatabase() throws ResponseException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}*/
