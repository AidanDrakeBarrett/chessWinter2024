package dataAccess;

public interface UserDAO {
    public void clearData();
    public UserData getUser(String username);
    public boolean getLogin(UserData login);
    public void createUser(UserData newUser);
}
