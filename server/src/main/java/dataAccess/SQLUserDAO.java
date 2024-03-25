/*package dataAccess;

import com.google.gson.Gson;
import server.ResponseException;

import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLUserDAO implements UserDAO{
    public SQLUserDAO() throws ResponseException {
        configureDatabase();
    }
    @Override
    public void clearData() {

    }

    @Override
    public boolean containsUsername(String username) throws DataAccessException {
        return false;
    }

    @Override
    public boolean getLogin(UserData login) throws DataAccessException {
        return false;
    }

    @Override
    public void createUser(UserData newUser) {
        //if not already in database
        //parse record
        //create object
        //profit, ig.
    }
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS user (
                'username' VARCHAR(256) NOT NULL,
                'password' VARCHAR(256) NOT NULL,
                'email' VARCHAR(256) NOT NULL,
                PRIMARY KEY ('username')
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
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
