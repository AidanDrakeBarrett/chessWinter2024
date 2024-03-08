package dataAccess;

public interface AuthDAO {
    public void clearData();
    public boolean containsAuth(String userAuth) throws DataAccessException;
    public void deleteAuth(String authToken);
    public AuthData createAuth(String username);
    public String getUsername(String authToken);
}
