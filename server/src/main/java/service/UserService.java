package service;

import dataAccess.AuthData;
import dataAccess.MemoryUserDAO;
import dataAccess.MemoryAuthDAO;
import dataAccess.UserData;

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
    public AuthData login(UserData userLogin) {//FIXME: EXCEPTION HANDLING, LMAO.
        if(userDAO.getLogin(userLogin)) {
            return authDAO.createAuth(userLogin.username());
        }
        return null;
    }
    public void logout(AuthData userLogout) {//FIXME: Y'ALREADY KNOW WHAT IT IS! NOW WATCH ME HANDLE; ALL MY EXCEPTIONS!
        if(authDAO.containsAuth(userLogout) != null) {
            authDAO.deleteAuth(userLogout.username());
        }
    }
}
