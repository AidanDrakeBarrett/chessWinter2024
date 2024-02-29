package dataAccess;

import java.util.HashSet;

public class MemoryAuthDAO implements AuthDAO {
    private static HashSet<AuthData> authDataHashSet;
    @Override
    public void clearData() {

    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public void createAuth(String username) {

    }
}
