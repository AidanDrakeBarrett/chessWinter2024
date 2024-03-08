package dataAccess;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private static HashSet<AuthData> authDataHashSet = new HashSet<>();
    @Override
    public void clearData() {

    }

    @Override
    public boolean containsAuth(String userAuth) throws DataAccessException {//no need to deserialize for string authtokens
        for(AuthData auth: authDataHashSet) {//edit for it to work with Strings
            if(auth.authToken().equals(userAuth)) {
                return true;
            }
        }
        throw new DataAccessException("Error: unauthorized");
    }

    @Override
    public void deleteAuth(String authToken) {
        for(AuthData auth: authDataHashSet) {
            if(Objects.equals(auth.authToken(), authToken)) {
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

    @Override
    public String getUsername(String authToken) {
        for(AuthData auth:authDataHashSet) {
            if(Objects.equals(auth.authToken(), authToken)) {
                return auth.username();
            }
        }
        return null;
    }
}
