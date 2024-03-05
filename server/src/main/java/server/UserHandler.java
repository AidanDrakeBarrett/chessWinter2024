package server;

import com.google.gson.Gson;
import dataAccess.AuthData;
import dataAccess.UserData;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.Map;

public class UserHandler {
    private static UserService service = new UserService();
    public UserHandler() {}
    public static Object register(Request req, Response res) {
        var newUser = new Gson().fromJson(req.body(), UserData.class);
        AuthData newAuth = service.register(newUser);
        return new Gson().toJson(newAuth);
    }
    public static Object login(Request req, Response res) {
        var userLogin = new Gson().fromJson(req.body(), UserData.class);
        AuthData newAuth = null;
        try {
            newAuth = service.login(userLogin);
        } catch (ResponseException resEx) {
            String message = "Error: unauthorized";
            res.status(401);
            //res.type("application/json");
            return new Gson().toJson(Map.of("message", message));
        }
        return new Gson().toJson(newAuth);
    }
    public static Object logout(Request req, Response res) {
        var userLogout = new Gson().fromJson(req.body(), AuthData.class);
        service.logout(userLogout);
        res.status(200);
        return "";
    }
}
