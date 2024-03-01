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
    public void logout(AuthData userLogout) {//FIXME: GETAUTH SHOULD BE FED A WHOLE AUTHDATA OBJECT, AND THEN THE AUTHDATA EQUALS METHOD WILL BE USED TO SEE IF IT SHOULD RETURN THE TOKEN. THEN CHECK IN HERE IF THE METHOD RETURNED A NOT NULL AND THEN DELETE THE AUTHDATA.
        AuthData compareAuth = authDAO.getAuth(userLogout.username());

    }
}
