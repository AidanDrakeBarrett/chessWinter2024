package dataAccess;

import dataAccess.AuthDAO;
import dataAccess.AuthData;

import java.util.HashSet;

public class MemoryAuthDao implements AuthDAO {
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
