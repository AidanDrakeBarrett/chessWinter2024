package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearAppService;

import static org.junit.jupiter.api.Assertions.*;

class ClearAppServiceTest {
    private ClearAppService service = new ClearAppService();
    private SQLGameDAO gameDAO = new SQLGameDAO();
    private SQLAuthDAO authDAO = new SQLAuthDAO();
    private SQLUserDAO userDAO = new SQLUserDAO();

    @Test
    void clearApplication() {
        String gameName = "game";
        gameDAO.createGame(gameName);

        String username = "username";
        String password = "password";
        String email = "email";
        UserData newUser = new UserData(username, password, email);
        userDAO.createUser(newUser);

        String authToken = authDAO.createAuth(username).authToken();

        service.clearApplication();
        assertTrue(gameDAO.listGames().isEmpty());
        assertThrows(DataAccessException.class, ()->userDAO.getLogin(newUser));
        assertNull(authDAO.getUsername(authToken));
    }
}