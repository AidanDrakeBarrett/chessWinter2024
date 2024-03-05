package dataAccess;

import java.util.HashSet;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private static HashSet<AuthData> authDataHashSet = new HashSet<>();
    @Override
    public void clearData() {

    }

    @Override
    public AuthData containsAuth(String userAuth) {//no need to deserialize for string authtokens
        for(AuthData auth: authDataHashSet) {//edit for it to work with Strings
            if(auth.authToken().equals(userAuth)) {
                return auth;
            }
        }
        return null;
    }

    @Override
    public void deleteAuth(String username) {
        for(AuthData auth: authDataHashSet) {
            if(auth.username() == username) {
                authDataHashSet.remove(auth);
            }
        }
    }

    @Override
    public AuthData createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, username);
        authDataHashSet.add(newAuth);
        return newAuth;
    }
}
