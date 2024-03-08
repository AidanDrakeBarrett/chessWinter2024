package dataAccess;

import chess.ChessGame;

import java.util.ArrayList;
import java.util.HashSet;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame chessGame, HashSet<String> spectators) {
}
