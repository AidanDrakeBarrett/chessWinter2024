package dataAccess;

import server.ResponseException;

import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO{
    public SQLAuthDAO() {
        try {
            configureDatabase();
        } catch(ResponseException resEx) {}
        catch(DataAccessException e) {}
    }
    @Override
    public void clearData() {
        try(var conn = DatabaseManager.getConnection()) {
            try(var preparedStatement = conn.prepareStatement("TRUNCATE TABLE AuthData;")) {
                preparedStatement.executeUpdate();
            } catch(SQLException sqlEx) {}
        } catch(SQLException sqlEx) {}
        catch(DataAccessException e) {}
    }

    @Override
    public boolean containsAuth(String userAuth) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()) {
            var statement = """
                    SELECT FROM AuthData 
                    WHERE authToken = ?;
                    """;
            try(var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, userAuth);
                try(var rs = preparedStatement.executeQuery()) {
                    if(Objects.equals(rs.getString("authToken"), userAuth)) {
                        return true;
                    }
                } catch(SQLException sqlEx) {
                    throw new DataAccessException("");
                }
            } catch(SQLException sqlEx) {
                throw new DataAccessException("");
            }
        } catch(SQLException sqlEx) {
            throw  new DataAccessException("");
        }
        throw new DataAccessException("Error: unauthorized");
    }

    @Override
    public void deleteAuth(String authToken) {
        try(var conn = DatabaseManager.getConnection()) {
            var statement = """
                    DELETE FROM AuthData 
                    WHERE authToken = ?;
                    """;
            try(var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            } catch(SQLException sqlEx) {}
        } catch(SQLException sqlEx) {}
        catch(DataAccessException e) {}
    }

    @Override
    public AuthData createAuth(String username) {
        String authToken = UUID.randomUUID().toString().replace("-", "");
        AuthData newAuth = new AuthData(username, authToken);
        try(var conn = DatabaseManager.getConnection()) {
            var statement = """
                    INSERT INTO AuthData (username, authToken) 
                    VALUES(?, ?);
                    """;
            try(var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, authToken);
                preparedStatement.executeUpdate();
            } catch(SQLException sqlEx) {}
        } catch(SQLException sqlEx) {}
        catch(DataAccessException e) {}
        return newAuth;
    }

    @Override
    public String getUsername(String authToken) {
        try(var conn = DatabaseManager.getConnection()) {
            var statement = """
                    SELECT FROM AuthData 
                    WHERE authToken = ?;
                    """;
            try(var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                try(var rs = preparedStatement.executeQuery()) {
                    return rs.getString("username");
                } catch(SQLException sqlEx) {}
            } catch(SQLException sqlEx) {}
        } catch(SQLException sqlEx) {}
        catch(DataAccessException e) {}
        return null;
    }
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS AuthData (
                username VARCHAR(255) NOT NULL,
                authToken VARCHAR(255) NOT NULL,
                PRIMARY KEY (authToken)
            );
            """
    };
    private void configureDatabase() throws ResponseException, DataAccessException {
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
}
