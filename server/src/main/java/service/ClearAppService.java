package service;

import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;

public class ClearAppService {
    private static SQLAuthDAO authDAO = new SQLAuthDAO();
    private static SQLGameDAO gameDAO = new SQLGameDAO();
    private static SQLUserDAO userDAO = new SQLUserDAO();
    public void clearApplication() {
        authDAO.clearData();
        gameDAO.clearData();
        userDAO.clearData();
    }
}
