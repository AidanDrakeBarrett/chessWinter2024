package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.AuthData;
import dataAccess.GameData;
import service.GamesService;
import spark.Request;
import spark.Response;

import java.util.HashSet;
import java.util.Map;

public class GamesHandler {
    private static GamesService service = new GamesService();

    public GamesHandler() {}

    public static Object listGames(Request req, Response res) {
        var authToken = req.headers("Authorization");
        HashSet<GameData> games = null;
        try {
            games = (HashSet<GameData>) service.listGames(authToken);
        } catch(ResponseException resEx) {
            String message = "Error: unauthorized";
            res.status(401);
            return new Gson().toJson(Map.of("message", message));
        }
        return new Gson().toJson(games);//do the mapof thing
    }
    public static Object createGame(Request req, Response res) {
        var authToken = req.headers("Authorization");
        var gameRequest = new Gson().fromJson(req.body(), Map.class);
        String gameName = (String) gameRequest.get("gameName");
        int gameID;
        try {
            gameID = (int) service.createGame(authToken, gameName);
        } catch(ResponseException resEx) {
            String message = "Error: unauthorized";
            res.status(401);
            return new Gson().toJson(Map.of("message", message));
        }
        return new Gson().toJson(Map.of("gameID", gameID));
    }
    public static Object joinGame(Request req, Response res) {
        var authToken = req.headers("Authorization");
        var reqMap = new Gson().fromJson(req.body(), Map.class);
        var playerColor = (ChessGame.TeamColor) reqMap.get("playerColor");
        var doubleGameID = (double) reqMap.get("gameID");
        var gameID = (int) doubleGameID;
        try {
            service.joinGame(authToken, playerColor, gameID);
        } catch(ResponseException resEx) {
            res.status(resEx.getStatusCode());
            return new Gson().toJson(Map.of("message", resEx.getMessage()));
        }
        res.status(200);
        return new Gson().toJson(Map.of(200, ""));
    }
}
