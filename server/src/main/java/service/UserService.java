package service;

import dataAccess.*;
import server.ResponseException;


public class UserService {
    private static SQLUserDAO userDAO = new SQLUserDAO();
    private static SQLAuthDAO authDAO = new SQLAuthDAO();

    public UserService() {}
    public AuthData register(UserData newUser) throws ResponseException {
        try {
            userDAO.containsUsername(newUser.username());
        } catch(DataAccessException e) {
            throw new ResponseException(403, "already taken");
        }
        userDAO.createUser(newUser);
        return authDAO.createAuth(newUser.username());
    }
    public AuthData login(UserData userLogin) throws ResponseException {
        try {
            if(userDAO.getLogin(userLogin)) {
                return authDAO.createAuth(userLogin.username());
            }
        } catch(DataAccessException e) {
            throw new ResponseException(401, "unauthorized");
        }
        return null;
    }
    public void logout(String authtoken) throws ResponseException {
        try {
            if(authDAO.containsAuth(authtoken)) {
                authDAO.deleteAuth(authtoken);
            }
        } catch(DataAccessException e) {
            throw new ResponseException(401, "unauthorized");
        }
    }
}
