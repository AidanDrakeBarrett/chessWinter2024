package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import dataAccess.AbbreviatedGameData;
import dataAccess.UserData;
import server.ResponseException;

import javax.management.Notification;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import static java.awt.Color.RED;

public class Client {
    private final ServerFacade serverFacade;
    private boolean loggedIn = false;
    public Client(String url) {
        serverFacade = new ServerFacade(url);
    }
    public void run() {
        System.out.println("Welcome Aidan's sadly-not-as-cool-as-the-garden-grove-chess-from-System-Shock chess serverFacade!\n");
        System.out.println("\t*Yes, really, and you can fight me about the name\n");
        System.out.println("Try typing 'help' for commands\n");
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }
    private void printPrompt() {
        System.out.print("\n\u001b[15;40;0m>>>");
    }
    public void notify(Notification notification) {
        System.out.println(RED + notification.getMessage());
        printPrompt();
    }
    public String help() {
        StringBuilder helpCommands = new StringBuilder();
        if(!loggedIn) {
            helpCommands.append("register <USERNAME> <PASSWORD> <EMAIL>\n");
            helpCommands.append("\tcreate an account\n");
            helpCommands.append("login <USERNAME> <PASSWORD>\n");
            helpCommands.append("\tlogin to a preexisting account\n");
            helpCommands.append("quit\n");
            helpCommands.append("\tleave the serverFacade\n");
            helpCommands.append("help\n");
            helpCommands.append("\tdisplay possible commands\n");
        }
        if(loggedIn) {
            helpCommands.append("create <NAME>\n");
            helpCommands.append("\tstart a new game\n");
            helpCommands.append("list\n");
            helpCommands.append("\tlist games that have already been created\n");
            helpCommands.append("join <GAME ID> <WHITE|BLACK|EMPTY>\n");
            helpCommands.append("\tjoin a game using its ID and which color you want to play, or leave blank to spectate\n");
            helpCommands.append("observe <GAME ID>\n");
            helpCommands.append("\tanother way to spectate a game\n");
            helpCommands.append("quit\n");
            helpCommands.append("\tleave the current game\n");
            helpCommands.append("logout\n");
            helpCommands.append("\tlog out of your account\n");
            helpCommands.append("help\n");
            helpCommands.append("\tdisplay possible commands\n");
        }
        return helpCommands.toString();
    }
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> join(params);
                case "logout" -> logout();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }
    public String register(String... params) throws ResponseException {
        if (params.length >= 3) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            UserData newUser = new UserData(username, password, email);
            serverFacade.register(newUser);
            loggedIn = true;
            return String.format("Registered and signed in as %s.", username);
        }
        throw new ResponseException(400, "Error: bad request");
    }
    public String login(String... params) throws ResponseException {
        if(params.length >= 2) {
            String username = params[0];
            String password = params[1];
            UserData newLogin = new UserData(username, password, null);
            serverFacade.login(newLogin);
            loggedIn = true;
            return String.format("Logged in as %s.", username);
        }
        throw new ResponseException(400, "Error: bad request");
    }
    public String create(String... params) throws ResponseException {
        if(params.length >= 1) {
            String gameName = params[0];
            int gameID = serverFacade.create(gameName);
            return String.format("Created game %s with ID %d.", gameName, gameID);
        }
        throw new ResponseException(400, "Error: bad request");
    }
    public String list() throws ResponseException {
        ArrayList<AbbreviatedGameData> gameList = serverFacade.list();
        StringBuilder returnList = new StringBuilder("Current chess games:\n");
        for(AbbreviatedGameData game:gameList) {
            returnList.append(String.format("gameID: %d, whiteUsername: %s, blackUsername: %s, gameName: %s",
                    game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
        }
        return returnList.toString();
    }
    public String join(String... params) throws ResponseException {
        if(params.length >= 1) {
            String gameID = params[0];
            ChessGame.TeamColor color = null;
            String position = "spectator";
            if(params.length >= 2) {
                if(Objects.equals(params[1], "WHITE")) {
                    color = ChessGame.TeamColor.WHITE;
                    position = "white";
                }
                if(Objects.equals(params[1], "BLACK")) {
                    color = ChessGame.TeamColor.BLACK;
                    position = "black";
                }
            }
            //ChessPiece[][] board = serverFacade.join(gameID, color);
            ChessBoard board = new ChessBoard();
            board.resetBoard();
            return String.format("joined game as " + position + "\n" + drawBoard(board.getBoard()));
        }
        throw new ResponseException(400, "Error: bad request");
    }
    public String logout() throws ResponseException {
        serverFacade.logout();
        this.loggedIn = false;
        return String.format("logged out");
    }
    public String drawBoard(ChessPiece[][] board) {
        StringBuilder whiteView = new StringBuilder();
        for(int i = 8; i >= 0; --i) {
            for(int j = 0; j < 9; ++j) {
                if(i == 0 && j == 0) {
                    whiteView.append("\u001b[30;107;1m    a  b  c  d  e  f  g  h \u001b[39;49;0m\n");
                }
                if( i > 0) {
                    if(j == 0) {
                        whiteView.append(String.format("\u001b[30;107;1m %d ", i));
                    }
                    if(j > 0) {
                        String background = null;
                        if((i % 2) == (j % 2)) {
                            background = "102;1m";
                        }
                        if((i % 2) != (j % 2)) {
                            background = "100;1m";
                        }
                        if(board[i - 1][j - 1] != null) {
                            ChessPiece piece = board[i - 1][j - 1];
                            String pieceColor = null;
                            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                                pieceColor = "\u001b[37;";
                            }
                            if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                                pieceColor = "\u001b[30;";
                            }
                            String pieceType = null;
                            switch (piece.getPieceType()) {
                                case PAWN -> pieceType = " P ";
                                case ROOK -> pieceType = " R ";
                                case KNIGHT -> pieceType = " N ";
                                case BISHOP -> pieceType = " B ";
                                case KING -> pieceType = " K ";
                                case QUEEN -> pieceType = " Q ";
                            }
                            whiteView.append(String.format(pieceColor + background + pieceType));
                        }
                        if(board[i - 1][j - 1] == null) {
                            whiteView.append(String.format("\u001b[" + background + "   "));
                        }
                        if(j == 8) {
                            whiteView.append("\u001b[39;49;0m\n");
                        }
                    }
                }
            }
        }
        StringBuilder blackView = new StringBuilder();
        for(int i = 0; i < 9; ++i) {
            for(int j = 8; j >= 0; --j) {
                if(i == 8 && j == 8) {
                    blackView.append("\u001b[30;107;1m    h  g  f  e  d  c  b  a \u001b[39;49;0m\n");
                }
                if( i < 8) {
                    if(j == 8) {
                        blackView.append(String.format("\u001b[30;107;1m %d ", (i + 1)));
                    }
                    if(j < 8) {
                        String background = null;
                        if((i % 2) == (j % 2)) {
                            background = "102;1m";
                        }
                        if((i % 2) != (j % 2)) {
                            background = "100;1m";
                        }
                        if(board[i][j] != null) {
                            ChessPiece piece = board[i][j];
                            String pieceColor = null;
                            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                                pieceColor = "\u001b[37;";
                            }
                            if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                                pieceColor = "\u001b[30;";
                            }
                            String pieceType = null;
                            switch (piece.getPieceType()) {
                                case PAWN -> pieceType = " P ";
                                case ROOK -> pieceType = " R ";
                                case KNIGHT -> pieceType = " N ";
                                case BISHOP -> pieceType = " B ";
                                case KING -> pieceType = " K ";
                                case QUEEN -> pieceType = " Q ";
                            }
                            blackView.append(String.format(pieceColor + background + pieceType));
                        }
                        if(board[i][j] == null) {
                            blackView.append(String.format("\u001b[" + background + "   "));
                        }
                        if(j == 0) {
                            blackView.append("\u001b[39;49;0m\n");
                        }
                    }
                }
            }
        }
        return String.format(whiteView + "\u001b[39;49;0m\n" + blackView);
    }

}
