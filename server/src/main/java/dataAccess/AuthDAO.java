package dataAccess;

public interface AuthDAO {
    public void clearData();
    public AuthData getAuth(String authToken);
    public void deleteAuth(String authToken);
    public void createAuth(String username);
}
