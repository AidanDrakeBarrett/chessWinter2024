package dataAccess;

public interface UserDAO {
    public void clearData();
    public UserData getUser(String username);
    public void createUser(String username, String password, String email);
}
