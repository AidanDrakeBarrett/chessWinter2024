package dataAccess;

public interface UserDAO {
    public void clearData();
    public boolean containsUsername(String username) throws DataAccessException;
    public boolean getLogin(UserData login) throws DataAccessException;
    public void createUser(UserData newUser);
}
