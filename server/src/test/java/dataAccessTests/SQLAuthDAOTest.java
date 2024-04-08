package dataAccessTests;

import dataAccess.AuthData;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.SQLAuthDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.ResponseException;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SQLAuthDAOTest {
    SQLAuthDAO authDAO = new SQLAuthDAO();
    String username1 = "username1";
    String authToken1;
    
    @AfterEach
    void tearDown() {
        authDAO.clearData();
    }

    @Test
    void createAuthPositive() throws DataAccessException {
        authToken1 = authDAO.createAuth(username1).authToken();
        assertTrue(authDAO.containsAuth(authToken1));
    }
    @Test
    void createAuthNegative() {
        String longUsername = "aReallyReallyReallyReallyReallyReallyReallyReallyReallyReallyReallyReallyReallyReallyR" +
                "eallyReallyReallyReallyReallyReallyReallyReallyReallyReallyReallyReallyReallyReallyReallyReallyReall" +
                "yReallyReallyReallyReallyReallyReallyReallyReallyReallyReallyReallyReallyReallyReallyReallyReallyRea" +
                "llyReallyReallyLongString";
        authToken1 = authDAO.createAuth(longUsername).authToken();
        assertNotEquals(authDAO.getUsername(authToken1), longUsername);
    }
    @Test
    void clearData() throws DataAccessException {
        authToken1 = authDAO.createAuth(username1).authToken();
        assertTrue(authDAO.containsAuth(authToken1));
        authDAO.clearData();
        assertThrows(DataAccessException.class, ()->authDAO.containsAuth(authToken1));
    }

    @Test
    void containsAuthPositive() throws DataAccessException {
        authToken1 = authDAO.createAuth(username1).authToken();
        assertTrue(authDAO.containsAuth(authToken1));
    }
    @Test
    void containsAuthNegative() {
        authToken1 = authDAO.createAuth(username1).authToken();
        String badAuth = "bad";
        assertThrows(DataAccessException.class, ()->authDAO.containsAuth(badAuth));
    }
    @Test
    void deleteAuthPositive() throws DataAccessException {
        authToken1 = authDAO.createAuth(username1).authToken();
        assertTrue(authDAO.containsAuth(authToken1));
        authDAO.deleteAuth(authToken1);
        assertThrows(DataAccessException.class, ()->authDAO.containsAuth(authToken1));
    }
    @Test
    void deleteAuthNegative() throws DataAccessException {
        authToken1 = authDAO.createAuth(username1).authToken();
        assertTrue(authDAO.containsAuth(authToken1));
        String badAuth = "bad";
        authDAO.deleteAuth(badAuth);
        assertTrue(authDAO.containsAuth(authToken1));
    }
    @Test
    void getUsernamePositive() {
        authToken1 = authDAO.createAuth(username1).authToken();
        assertEquals(username1, authDAO.getUsername(authToken1));
    }
    @Test
    void getUsernameNegative() {
        authToken1 = authDAO.createAuth(username1).authToken();
        assertEquals(username1, authDAO.getUsername(authToken1));
        authDAO.deleteAuth(authToken1);
        assertNotEquals(username1, authDAO.getUsername(authToken1));
    }
}