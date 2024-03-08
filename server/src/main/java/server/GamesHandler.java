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
        var gameName = new Gson().fromJson(req.body(), String.class);
        int gameID;
        try {
            gameID = (int) service.createGame(authToken, gameName);
        } catch(ResponseException resEx) {
            String message = "Error: unauthorized";
            res.status(401);
            return new Gson().toJson(Map.of("message", message));
        }
        return new Gson().toJson(gameID);
    }
    public static Object joinGame(Request req, Response res) {
        var authToken = req.headers("Authorization");
        var playerColor = new Gson().fromJson(req.body(), ChessGame.TeamColor.class);
        var gameID = new Gson().fromJson(req.body(), Integer.class);
        try {
            service.joinGame(authToken, playerColor, gameID);
        } catch(ResponseException resEx) {
            res.status(resEx.getStatusCode());
            return new Gson().toJson(Map.of("message", resEx.getMessage()));
        }
        return "";
    }
}
