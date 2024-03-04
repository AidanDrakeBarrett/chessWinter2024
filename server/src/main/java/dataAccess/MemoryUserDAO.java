package dataAccess;

import org.eclipse.jetty.server.Authentication;

import java.util.HashSet;
import java.util.UUID;

public class MemoryUserDAO implements UserDAO {
    private static HashSet<UserData> userDataHashSet = new HashSet<>();
    @Override
    public void clearData() {
        userDataHashSet.clear();
    }

    @Override
    public UserData getUser(String username) {
        for(UserData user: userDataHashSet) {
            if(user.username() == username) {
                return user;
            }
        }
        return null;
    }

    @Override
    public boolean getLogin(UserData login) {
        String username = login.username();
        for(UserData user: userDataHashSet) {
            if(user.username() == username) {
                if(user.password().equals(login.password())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void createUser(UserData newUser) {
        userDataHashSet.add(newUser);
        String authToken = UUID.randomUUID().toString();
    }
}
