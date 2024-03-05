package service;

import dataAccess.*;
import server.ResponseException;


public class UserService {
    private static MemoryUserDAO userDAO = new MemoryUserDAO();
    private static MemoryAuthDAO authDAO = new MemoryAuthDAO();

    public UserService() {}
    public AuthData register(UserData newUser) {//FIXME: EXCEPTION HANDLING
        if(userDAO.getUser(newUser.username()) == null) {
            userDAO.createUser(newUser);
            return authDAO.createAuth(newUser.username());
        }
        return null;
    }
    public AuthData login(UserData userLogin) throws ResponseException {//FIXME: EXCEPTION HANDLING, LMAO.
        try {
            if(userDAO.getLogin(userLogin)) {
                return authDAO.createAuth(userLogin.username());
            }
        } catch(DataAccessException e) {
            throw new ResponseException(401, "unauthorized");
        }
        return null;
    }
    public void logout(AuthData userLogout) {//FIXME: Y'ALREADY KNOW WHAT IT IS! NOW WATCH ME HANDLE; ALL MY EXCEPTIONS!
        if(authDAO.containsAuth(userLogout) != null) {
            authDAO.deleteAuth(userLogout.username());
        }
    }
}
