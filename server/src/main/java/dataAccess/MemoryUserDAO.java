package dataAccess;

import org.eclipse.jetty.server.Authentication;

import java.util.HashSet;
import java.util.Objects;
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
        String password = login.password();
        for(UserData user: userDataHashSet) {
            if(Objects.equals(user.username(), username)) {
                if(Objects.equals(user.password(), password)) {
                    return true;
                }
                return false;
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
