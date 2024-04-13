package ui;

import chess.ChessGame;
import chess.ChessPiece;
import com.google.gson.Gson;
import dataAccess.UserData;
import server.JoinRequests;

import java.io.*;
import java.net.*;
import java.util.Map;

public class ServerFacade {
    private String authToken = null;
    public enum ResponseType {
        AUTHORIZATION,
        EMPTY,
        GAME_LIST,
        GAME_ID
    }

    public ServerFacade() {
        /*URI uri = new URI(url);
        http = (HttpURLConnection) uri.toURL().openConnection();*/
    }
    public void register(UserData newUser) throws Exception {
        String url = "http://localhost:8080/user";
        var body = new Gson().toJson(newUser);
        String method = "POST";
        HttpURLConnection http = sendRequest(url, method, body, authToken);
        ResponseType responseType = ResponseType.AUTHORIZATION;
        receiveResponse(http, responseType);
    }
    public void login(UserData newLogin) throws Exception {
        String url = "http://localhost:8080/session";
        var body = new Gson().toJson(newLogin);
        String method = "POST";
        HttpURLConnection http = sendRequest(url, method, body, authToken);
        ResponseType responseType = ResponseType.AUTHORIZATION;
        receiveResponse(http, responseType);
    }
    public int create(String gameName) throws Exception {
        String url = "http://localhost:8080/game";
        var body = new Gson().toJson(Map.of("gameName: ", gameName));
        String method = "POST";
        HttpURLConnection http = sendRequest(url, method, body, authToken);
        ResponseType responseType = ResponseType.GAME_ID;
        receiveResponse(http, responseType);
    }
    public String list() throws Exception {
        //I am an idiot. I can't do anything right. I am retarded.
        String url = "http://localhost:8080/game";
        String body = null;
        String method = "GET";
        HttpURLConnection http = sendRequest(url, method, body, authToken);
        ResponseType responseType = ResponseType.GAME_LIST;
        receiveResponse(http, responseType);
    }
    public ChessPiece[][] join(String gameID, ChessGame.TeamColor color) throws Exception {
        String url = "http://localhost:8080/game";
        var body = new Gson().toJson(new JoinRequests(color, Integer.parseInt(gameID)));
        String method = "PUT";
        HttpURLConnection http = sendRequest(url, method, body, authToken);
        ResponseType responseType = ResponseType.EMPTY;
        receiveResponse(http, responseType);
    }
    public void logout() throws Exception {
        String url = "http://localhost:8080/session";
        String body = null;
        String method = "DELETE";
        HttpURLConnection http = sendRequest(url, method, body, authToken);
        ResponseType responseType = ResponseType.GAME_LIST;
        receiveResponse(http, responseType);
    }
    private static HttpURLConnection sendRequest(String url, String method, String body, String authToken) throws URISyntaxException, IOException {
        URI uri = new URI(url);
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        if(authToken != null) {
            http.setRequestProperty("Authorization", authToken);
        }
        http.setRequestMethod(method);
        writeRequestBody(body, http);
        http.connect();
        System.out.printf("= Request =========\n[%s] %s\n\n%s\n\n", method, url, body);
        return http;
    }
    private static void writeRequestBody(String body, HttpURLConnection http) throws IOException {
        if (!body.isEmpty()) {
            http.setDoOutput(true);
            try (var outputStream = http.getOutputStream()) {
                outputStream.write(body.getBytes());
            }
        }
    }
    private static void receiveResponse(HttpURLConnection http, ResponseType responseType) throws IOException {
        var statusCode = http.getResponseCode();
        var statusMessage = http.getResponseMessage();

        Object responseBody = readResponseBody(http);
        System.out.printf("= Response =========\n[%d] %s\n\n%s\n\n", statusCode, statusMessage, responseBody);
    }
    private static Object readResponseBody(HttpURLConnection http, ResponseType responseType) throws IOException {
        Object responseBody = "";
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            responseBody = new Gson().fromJson(inputStreamReader, Map.class);
        }
        return responseBody;
    }
}
