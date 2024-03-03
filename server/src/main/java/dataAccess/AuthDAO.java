package dataAccess;

public interface AuthDAO {
    public void clearData();
    public AuthData containsAuth(AuthData userAuth);
    public void deleteAuth(String username);
    public AuthData createAuth(String username);
}
