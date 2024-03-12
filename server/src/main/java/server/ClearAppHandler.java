package server;

import com.google.gson.Gson;
import service.ClearAppService;
import spark.Request;
import spark.Response;

import java.util.Map;


public class ClearAppHandler {
    private static ClearAppService service = new ClearAppService();
    public ClearAppHandler() {}

    public static Object clearApplication(Request req, Response res) {
        /*var authToken = req.headers("Authorization");
        try {
            service.clearApplication(authToken);
        } catch(ResponseException resEx) {
            res.status(resEx.getStatusCode());
            return "{}";
        }*/
        service.clearApplication();
        res.status(200);
        return "{}";
    }
}
