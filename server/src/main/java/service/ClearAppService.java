package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import server.ResponseException;

public class ClearAppService {
    private static MemoryAuthDAO authDAO = new MemoryAuthDAO();
    private static MemoryGameDAO gameDAO = new MemoryGameDAO();
    private static MemoryUserDAO userDAO = new MemoryUserDAO();
    public void clearApplication(/*String authToken*/) /*throws ResponseException*/ {
        /*try {
            if(authDAO.containsAuth(authToken)) {
                authDAO.clearData();
                gameDAO.clearData();
                userDAO.clearData();
            }
        } catch(DataAccessException e) {
            throw new ResponseException(401, "Error: unauthorized");
        }*/
        authDAO.clearData();
        gameDAO.clearData();
        userDAO.clearData();
    }
}
