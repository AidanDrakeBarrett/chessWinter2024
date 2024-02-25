package dataAccess;

import java.util.HashSet;

public class MemoryUserDAO implements UserDAO {
    private static HashSet<UserData> userDataHashSet;
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
    public void createUser(String username, String password, String email) {
        UserData newUser = new UserData(username, password, email);
        userDataHashSet.add(newUser);
    }
}
