package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.AuthData;
import dataAccess.GameData;
import service.GamesService;
import spark.Request;
import spark.Response;

import java.util.HashSet;

public class GamesHandler {
    private static GamesService service = new GamesService();

    public GamesHandler() {}

    public static Object listGames(Request req, Response res) {
        var userAuth = new Gson().fromJson(req.body(), AuthData.class);
        HashSet<GameData> games = (HashSet<GameData>) service.listGames(userAuth);
        res.status(200);
        return new Gson().toJson(games);//do the mapof thing
    }
    public static Object createGame(Request req, Response res) {
        String authToken = req.headers("Authorization");
        var userAuth = new Gson().fromJson(req.body(), AuthData.class);//this will need some editing, too. Embrace the mess.
        var gameName = new Gson().fromJson(req.body(), GameData.class);
        int gameID = (int) service.createGame(userAuth, gameName.gameName());
        res.status(200);
        return new Gson().toJson(gameID);
    }
    public static Object joinGame(Request req, Response res) {
        var userAuth = new Gson().fromJson(req.body(), AuthData.class);
        var playerColor = new Gson().fromJson(req.body(), ChessGame.TeamColor.class);
        var gameID = new Gson().fromJson(req.body(), Integer.class);
        service.joinGame(userAuth, playerColor, gameID);
        res.status(200);
        return "";
    }
}
