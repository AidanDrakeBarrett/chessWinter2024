package dataAccess;

import server.ResponseException;

import java.sql.*;
import java.util.Objects;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SQLUserDAO implements UserDAO{
    public SQLUserDAO() {
        try {
            configureDatabase();
        } catch(ResponseException resEx) {}
        catch(DataAccessException e) {}
    }
    @Override
    public void clearData() {
        try(var conn = DatabaseManager.getConnection()) {
            try(var preparedStatement = conn.prepareStatement("TRUNCATE TABLE UserData;")) {
                preparedStatement.executeUpdate();
            } catch(SQLException sqlEx) {}
        } catch(SQLException sqlEx) {}
        catch(DataAccessException e) {}
    }

    @Override
    public boolean containsUsername(String username) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()) {
            var statement = """
                    SELECT * FROM UserData 
                    WHERE username = ?;
                    """;
            try(var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                try(var rs = preparedStatement.executeQuery()) {
                    if(!rs.next()) {
                        return false;
                    }
                    if(Objects.equals(rs.getString("username"), username)) {
                        throw new DataAccessException("");
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
        return false;
    }

    @Override
    public boolean getLogin(UserData login) throws DataAccessException {
        String username = login.username();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        try(var conn = DatabaseManager.getConnection()) {
            var statement = """
                    SELECT password FROM UserData 
                    WHERE username = ?;
                    """;
            try(var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                try(var rs = preparedStatement.executeQuery()) {
                    String queriedPassword = null;
                    while(rs.next()) {
                        queriedPassword = rs.getString("password");
                    }
                    if(encoder.matches(login.password(), queriedPassword)) {
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
        throw new DataAccessException("unauthorized");
    }

    @Override
    public void createUser(UserData newUser) {
        String username = newUser.username();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(newUser.password());
        String email = newUser.email();
        var statement = """
                INSERT INTO UserData 
                (username, password, email) 
                VALUES (?, ?, ?);
                """;
        try(var conn = DatabaseManager.getConnection()) {
            try(var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, email);
                preparedStatement.executeUpdate();
            } catch(SQLException sqlEx) {}
        } catch(SQLException sqlEx) {}
        catch(DataAccessException e) {}
    }
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS UserData (
                username VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL,
                PRIMARY KEY (username)
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
