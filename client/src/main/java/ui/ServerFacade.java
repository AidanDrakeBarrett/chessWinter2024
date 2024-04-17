package ui;

import chess.ChessGame;
import chess.ChessPiece;
import com.google.gson.Gson;
import dataAccess.AbbreviatedGameData;
import dataAccess.AuthData;
import dataAccess.UserData;
import server.JoinRequests;
import server.ResponseException;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Map;

public class ServerFacade {
    private String authToken = null;
    private final String serverURL;

    public ServerFacade(String url) {
        serverURL = url;
    }
    public void register(UserData newUser) throws ResponseException {
        var path = "/user";
        var body = new Gson().toJson(newUser);
        String method = "POST";
        this.authToken = sendRequest(path, method, body, authToken, AuthData.class).authToken();
    }
    public void login(UserData newLogin) throws ResponseException {
        var path = "/session";
        var body = new Gson().toJson(newLogin);
        String method = "POST";
        this.authToken = sendRequest(path, method, body, authToken, AuthData.class).authToken();
    }
    public int create(String gameName) throws ResponseException {
        String path = "/game";
        var body = new Gson().toJson(Map.of("gameName", gameName));
        String method = "POST";
        var newGame = sendRequest(path, method, body, authToken, Map.class);
        Double gameID = (Double) newGame.get("gameID");
        return gameID.intValue();
    }
    public ArrayList list() throws ResponseException {
        String path = "/game";
        String body = null;
        String method = "GET";
        var listMap = sendRequest(path, method, body, authToken, Map.class);
        ArrayList<AbbreviatedGameData> listArray = (ArrayList<AbbreviatedGameData>) listMap.get("games");
        return listArray;
    }
    public ChessPiece[][] join(String gameID, ChessGame.TeamColor color) throws ResponseException {
        String path = "/game";
        var body = new Gson().toJson(new JoinRequests(color, Integer.parseInt(gameID)));
        String method = "PUT";
        return sendRequest(path, method, body, authToken, ChessPiece[][].class);
    }
    public void logout() throws ResponseException {
        String path = "/session";
        String body = null;
        String method = "DELETE";
        sendRequest(path, method, body, authToken, null);
    }
    private <T> T sendRequest(String path, String method, String body, String authToken, Class<T> responseClass) throws ResponseException {
        try {
            URI uri = new URI(serverURL + path);
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            if (authToken != null) {
                http.setRequestProperty("Authorization", authToken);
            }
            http.setRequestMethod(method);
            http.setDoOutput(true);
            writeRequestBody(body, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readResponseBody(http, responseClass);
        } catch(Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
    private static void writeRequestBody(String body, HttpURLConnection http) throws IOException {
        if(body != null) {
            http.addRequestProperty("Content-Type", "application/json");
            try(OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(body.getBytes());
            }
        }

    }
    private static <T> T readResponseBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if(http.getContentLength() < 0) {
            try(InputStream resBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(resBody);
                if(responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }
    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if(!successful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }
    private boolean successful(int status) {
        return status / 100 == 2;
    }
}
