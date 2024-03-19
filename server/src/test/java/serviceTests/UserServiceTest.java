package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.ResponseException;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserService service = new UserService();
    private MemoryUserDAO userDAO = new MemoryUserDAO();
    private MemoryAuthDAO authDAO = new MemoryAuthDAO();

    @AfterEach
    void tearDown() {
        userDAO.clearData();
        authDAO.clearData();
    }

    @Test
    void register() throws ResponseException {
        String username = "user";
        String email = "email";
        String password = "password";
        UserData user = new UserData(username, password, email);
        service.register(user);
        assertTrue(userDAO.getUserDataHashSet().contains(user));

        String anotherUsername = "user";
        String anotherEmail = "Thou swine";
        String anotherPassword = "another password";
        UserData anotherUser = new UserData(anotherUsername, anotherPassword, anotherEmail);
        assertThrows(ResponseException.class, ()->service.register(anotherUser));
    }

    @Test
    void login() throws ResponseException, DataAccessException {
        String username = "user";
        String password = "password";
        String email = "email";
        UserData user = new UserData(username, password, email);

        userDAO.createUser(user);
        String userAuth = service.login(user).authToken();
        assertTrue(authDAO.containsAuth(userAuth));

        String badUsername = "bad";
        UserData badLogin = new UserData(badUsername, password, email);
        assertThrows(ResponseException.class, ()->service.login(badLogin));
    }

    @Test
    void logout() throws ResponseException, DataAccessException {
        String username = "user";
        String authToken = authDAO.createAuth(username).authToken();
        assertTrue(authDAO.containsAuth(authToken));
        service.logout(authToken);
        assertThrows(DataAccessException.class, ()->authDAO.containsAuth(authToken));
        assertThrows(ResponseException.class, ()->service.logout("fakeToken"));
    }
}