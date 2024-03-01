package dataAccess;

import java.util.HashSet;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private static HashSet<AuthData> authDataHashSet;
    @Override
    public void clearData() {

    }

    @Override
    public AuthData getAuth(String username) {
        for(AuthData auth: authDataHashSet) {
            if(auth.username() == username) {
                return auth;
            }
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public AuthData createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, username);
        authDataHashSet.add(newAuth);
        return newAuth;
    }
}
