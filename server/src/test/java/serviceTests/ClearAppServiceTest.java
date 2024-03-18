package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearAppService;

import static org.junit.jupiter.api.Assertions.*;

class ClearAppServiceTest {
    private ClearAppService service = new ClearAppService();
    private MemoryGameDAO gameDAO = new MemoryGameDAO();
    private MemoryAuthDAO authDAO = new MemoryAuthDAO();
    private MemoryUserDAO userDAO = new MemoryUserDAO();
    @BeforeEach
    void setUp() {
        String gameName = "game";
        gameDAO.createGame(gameName);

        String username = "username";
        String password = "password";
        String email = "email";
        UserData newUser = new UserData(username, password, email);
        userDAO.createUser(newUser);

        authDAO.createAuth(username);
    }

    @Test
    void clearApplication() {
        service.clearApplication();
        assertTrue(gameDAO.listGames().isEmpty());
        assertTrue(userDAO.getUserDataHashSet().isEmpty());
        assertTrue(authDAO.getAuthDataHashSet().isEmpty());
    }
}