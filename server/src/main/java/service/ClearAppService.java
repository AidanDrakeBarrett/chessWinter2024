package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;

public class ClearAppService {
    private static MemoryAuthDAO authDAO;
    private static MemoryGameDAO gameDAO;
    private static MemoryUserDAO userDAO;
    public void clearApplication() {
        authDAO.clearData();
        gameDAO.clearData();
        userDAO.clearData();
    }
}
