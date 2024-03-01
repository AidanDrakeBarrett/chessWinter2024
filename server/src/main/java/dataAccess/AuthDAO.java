package dataAccess;

public interface AuthDAO {
    public void clearData();
    public AuthData getAuth(String username);
    public void deleteAuth(String username);
    public AuthData createAuth(String username);
}
