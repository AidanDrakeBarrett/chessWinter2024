package dataAccess;

public interface AuthDAO {
    public void clearData();
    public AuthData containsAuth(String userAuth);
    public void deleteAuth(String username);
    public AuthData createAuth(String username);
}
