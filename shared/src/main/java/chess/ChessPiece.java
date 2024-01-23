package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor color;
    private PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = color;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if(type == PieceType.PAWN) {
            return pawnMoves(board, myPosition);
        }
        if(type == PieceType.ROOK) {
            return rookMoves(board, myPosition);
        }
        if(type == PieceType.KNIGHT) {
            return knightMoves(board, myPosition);
        }
        if(type == PieceType.BISHOP) {
            return  bishopMoves(board, myPosition);
        }
        if(type == PieceType.QUEEN) {
            return queenMoves(board, myPosition);
        }
        if(type == PieceType.KING) {
            return kingMoves(board, myPosition);
        }
        return null;
    }
    private HashSet<ChessMove> pawnMoves(ChessBoard board, ChessPosition start) {
        HashSet<ChessMove> moves = new HashSet<>();
        if(color == ChessGame.TeamColor.WHITE) {
            if(start.getRow() < 8) {//check if it can move forward
                int newRow = start.getRow() + 1;
                ChessPosition newPosition = new ChessPosition(newRow, start.getColumn());
                if(board.getPiece(newPosition) == null && newRow < 8) {//check for an empty space
                    ChessMove possibleMove = new ChessMove(start, newPosition);
                    moves.add(possibleMove);
                }
                if(board.getPiece(newPosition) == null && newRow == 8) {//promotion cases
                    pawnUpgrades(start, newPosition, moves);
                }
                //look for capture cases
                if(start.getColumn() < 8) {//make sure it can go right
                    ChessPosition rightDiag = new ChessPosition(newRow, start.getColumn() + 1);
                    if(board.getPiece(rightDiag) != null) {//check for enemy piece
                        if(board.getPiece(rightDiag).getTeamColor() != ChessGame.TeamColor.WHITE) {
                            if(newRow < 8) {
                                ChessMove captureRight = new ChessMove(start, rightDiag);
                                moves.add(captureRight);
                            }
                            if(newRow == 8) {// upgrade
                                pawnUpgrades(start, rightDiag, moves);
                            }
                        }

                    }
                }
                if(start.getColumn() > 1) {//check to move left
                    ChessPosition leftDiag = new ChessPosition(newRow, start.getColumn() - 1);
                    if(board.getPiece(leftDiag) != null) {//check for enemies
                        if(board.getPiece(leftDiag).getTeamColor() != ChessGame.TeamColor.WHITE) {
                            if(newRow < 8) {
                                ChessMove captureLeft = new ChessMove(start, leftDiag);
                                moves.add(captureLeft);
                            }
                            if(newRow == 8) {//upgrade
                                pawnUpgrades(start, leftDiag, moves);
                            }
                        }
                    }
                }
                if(start.getRow() == 2) {//double jump
                    ChessPosition doubleJump = new ChessPosition(4, start.getColumn());
                    if(board.getPiece(doubleJump) == null) {
                        ChessMove possibleMove = new ChessMove(start, doubleJump);
                        moves.add(possibleMove);
                    }
                }
            }
        }
        if(color == ChessGame.TeamColor.BLACK) {
            if(start.getRow() > 1) {//check if it can move forward
                int newRow = start.getRow() - 1;
                ChessPosition newPosition = new ChessPosition(newRow, start.getColumn());
                if(board.getPiece(newPosition) == null && newRow > 1) {//check for an empty space
                    ChessMove possibleMove = new ChessMove(start, newPosition);
                    moves.add(possibleMove);
                }
                if(board.getPiece(newPosition) == null && newRow == 1) {//promotion cases
                    pawnUpgrades(start, newPosition, moves);
                }
                //look for capture cases
                if(start.getColumn() < 8) {//make sure it can go right
                    ChessPosition rightDiag = new ChessPosition(newRow, start.getColumn() + 1);
                    if(board.getPiece(rightDiag) != null) {//check for enemy piece
                        if(board.getPiece(rightDiag).getTeamColor() != ChessGame.TeamColor.BLACK) {
                            if(newRow > 1) {
                                ChessMove captureRight = new ChessMove(start, rightDiag);
                                moves.add(captureRight);
                            }
                            if(newRow == 1) {// upgrade
                                pawnUpgrades(start, rightDiag, moves);
                            }
                        }

                    }
                }
                if(start.getColumn() > 1) {//check to move left
                    ChessPosition leftDiag = new ChessPosition(newRow, start.getColumn() - 1);
                    if(board.getPiece(leftDiag) != null) {//check for enemies
                        if(board.getPiece(leftDiag).getTeamColor() != ChessGame.TeamColor.BLACK) {
                            if(newRow > 1) {
                                ChessMove captureLeft = new ChessMove(start, leftDiag);
                                moves.add(captureLeft);
                            }
                            if(newRow == 1) {//upgrade
                                pawnUpgrades(start, leftDiag, moves);
                            }
                        }
                    }
                }
                if(start.getRow() == 7) {//double jump
                    ChessPosition doubleJump = new ChessPosition(5, start.getColumn());
                    if(board.getPiece(doubleJump) == null) {
                        ChessMove possibleMove = new ChessMove(start, doubleJump);
                        moves.add(possibleMove);
                    }
                }
            }
        }
        return moves;
    }
    private void pawnUpgrades(ChessPosition start, ChessPosition end, HashSet<ChessMove> moves) {
        ChessMove upgrade1 = new ChessMove(start, end, PieceType.ROOK);
        ChessMove upgrade2 = new ChessMove(start, end, PieceType.KNIGHT);
        ChessMove upgrade3 = new ChessMove(start, end, PieceType.BISHOP);
        ChessMove upgrade4 = new ChessMove(start, end, PieceType.QUEEN);
        moves.add(upgrade1);
        moves.add(upgrade2);
        moves.add(upgrade3);
        moves.add(upgrade4);
        return;
    }
    private HashSet<ChessMove> rookMoves(ChessBoard board, ChessPosition start) {
        HashSet<ChessMove> moves = new HashSet<>();
        if(start.getRow() < 8) {//check move forward
            int newRow = start.getRow() + 1;
            while(newRow <= 8) {
                ChessPosition newPosition = new ChessPosition(newRow, start.getColumn());
                if(board.getPiece(newPosition) == null) {
                    ChessMove possibleMove = new ChessMove(start, newPosition);
                    moves.add(possibleMove);
                }
                if(board.getPiece(newPosition) != null) {//capture scenario
                    pieceConfrontations(board, start, newPosition, moves);
                    break;
                }
                newRow += 1;
            }
        }
        if(start.getRow() > 1) {//check move back
            int newRow = start.getRow() - 1;
            while(newRow >= 1) {
                ChessPosition newPosition = new ChessPosition(newRow, start.getColumn());
                if(board.getPiece(newPosition) == null) {
                    ChessMove possibleMove = new ChessMove(start, newPosition);
                    moves.add(possibleMove);
                }
                if(board.getPiece(newPosition) != null) {//capture scenario
                    pieceConfrontations(board, start, newPosition, moves);
                    break;
                }
                newRow -= 1;
            }
        }
        if(start.getColumn() < 8) {//check move right
            int newCol = start.getColumn() + 1;
            while(newCol <= 8) {
                ChessPosition newPosition = new ChessPosition(start.getRow(), newCol);
                if(board.getPiece(newPosition) == null) {
                    ChessMove possibleMove = new ChessMove(start, newPosition);
                    moves.add(possibleMove);
                }
                if(board.getPiece(newPosition) != null) {//capture scenario
                    pieceConfrontations(board, start, newPosition, moves);
                    break;
                }
                newCol += 1;
            }
        }
        if(start.getColumn() > 1) {//check move left
            int newCol = start.getColumn() - 1;
            while(newCol >= 1) {
                ChessPosition newPosition = new ChessPosition(start.getRow(), newCol);
                if(board.getPiece(newPosition) == null) {
                    ChessMove possibleMove = new ChessMove(start, newPosition);
                    moves.add(possibleMove);
                }
                if(board.getPiece(newPosition) != null) {//capture scenario
                    pieceConfrontations(board, start, newPosition, moves);
                    break;
                }
                newCol -= 1;
            }
        }
        return moves;
    }
    private HashSet<ChessMove> knightMoves(ChessBoard board, ChessPosition start) {
        HashSet<ChessMove> moves = new HashSet<>();
        if(start.getRow() < 8) {//go up 1
            if(start.getColumn() > 2) {//left 2
                int newRow = start.getRow() + 1;
                int newCol = start.getColumn() - 2;
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                if(board.getPiece(newPosition) == null) {
                    ChessMove possibleMove = new ChessMove(start, newPosition);
                    moves.add(possibleMove);
                }
                if(board.getPiece(newPosition) != null) {//capture
                    pieceConfrontations(board, start, newPosition, moves);
                }
            }
            if(start.getColumn() < 7) {//right 2
                int newRow = start.getRow() + 1;
                int newCol = start.getColumn() + 2;
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                if(board.getPiece(newPosition) == null) {
                    ChessMove possibleMove = new ChessMove(start, newPosition);
                    moves.add(possibleMove);
                }
                if(board.getPiece(newPosition) != null) {//capture
                    pieceConfrontations(board, start, newPosition, moves);
                }
            }
        }
        if(start.getRow() < 7) {//go up 2
            if(start.getColumn() > 1) {//left 1
                int newRow = start.getRow() + 2;
                int newCol = start.getColumn() - 1;
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                if(board.getPiece(newPosition) == null) {
                    ChessMove possibleMove = new ChessMove(start, newPosition);
                    moves.add(possibleMove);
                }
                if(board.getPiece(newPosition) != null) {//capture
                    pieceConfrontations(board, start, newPosition, moves);
                }
            }
            if(start.getColumn() < 8) {//right 1
                int newRow = start.getRow() + 2;
                int newCol = start.getColumn() + 1;
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                if(board.getPiece(newPosition) == null) {
                    ChessMove possibleMove = new ChessMove(start, newPosition);
                    moves.add(possibleMove);
                }
                if(board.getPiece(newPosition) != null) {//capture
                    pieceConfrontations(board, start, newPosition, moves);
                }
            }
        }
        if(start.getRow() > 1) {//go down 1
            if(start.getColumn() > 2) {//left 2
                int newRow = start.getRow() - 1;
                int newCol = start.getColumn() - 2;
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                if(board.getPiece(newPosition) == null) {
                    ChessMove possibleMove = new ChessMove(start, newPosition);
                    moves.add(possibleMove);
                }
                if(board.getPiece(newPosition) != null) {//capture
                    pieceConfrontations(board, start, newPosition, moves);
                }
            }
            if(start.getColumn() < 7) {//right 2
                int newRow = start.getRow() - 1;
                int newCol = start.getColumn() + 2;
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                if(board.getPiece(newPosition) == null) {
                    ChessMove possibleMove = new ChessMove(start, newPosition);
                    moves.add(possibleMove);
                }
                if(board.getPiece(newPosition) != null) {//capture
                    pieceConfrontations(board, start, newPosition, moves);
                }
            }
        }
        if(start.getRow() > 2) {//go down 2
            if(start.getColumn() > 1) {//left 1
                int newRow = start.getRow() - 2;
                int newCol = start.getColumn() - 1;
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                if(board.getPiece(newPosition) == null) {
                    ChessMove possibleMove = new ChessMove(start, newPosition);
                    moves.add(possibleMove);
                }
                if(board.getPiece(newPosition) != null) {//capture
                    pieceConfrontations(board, start, newPosition, moves);
                }
            }
            if(start.getColumn() < 8) {//right 1
                int newRow = start.getRow() - 2;
                int newCol = start.getColumn() + 1;
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                if(board.getPiece(newPosition) == null) {
                    ChessMove possibleMove = new ChessMove(start, newPosition);
                    moves.add(possibleMove);
                }
                if(board.getPiece(newPosition) != null) {//capture
                    pieceConfrontations(board, start, newPosition, moves);
                }
            }
        }
        return moves;
    }
    private HashSet<ChessMove> bishopMoves(ChessBoard board, ChessPosition start) {
        HashSet<ChessMove> moves = new HashSet<>();
        if(start.getRow() < 8) {//move up
            if(start.getColumn() < 8) {//move right
                int newRow = start.getRow() + 1;
                int newCol = start.getColumn() + 1;
                while(newRow <= 8 && newCol <= 8) {
                    ChessPosition newPosition = new ChessPosition(newRow, newCol);
                    if(board.getPiece(newPosition) == null) {
                        ChessMove possibleMove = new ChessMove(start, newPosition);
                        moves.add(possibleMove);
                    }
                    if(board.getPiece(newPosition) != null) {//capture
                        pieceConfrontations(board, start, newPosition, moves);
                        break;
                    }
                    newRow += 1;
                    newCol += 1;
                }
            }
            if(start.getColumn() > 1) {//move left
                int newRow = start.getRow() + 1;
                int newCol = start.getColumn() - 1;
                while(newRow <= 8 && newCol >= 1) {
                    ChessPosition newPosition = new ChessPosition(newRow, newCol);
                    if(board.getPiece(newPosition) == null) {
                        ChessMove possibleMove = new ChessMove(start, newPosition);
                        moves.add(possibleMove);
                    }
                    if(board.getPiece(newPosition) != null) {//capture
                        pieceConfrontations(board, start, newPosition, moves);
                        break;
                    }
                    newRow += 1;
                    newCol -= 1;
                }
            }
        }
        if(start.getRow() > 1) {//move down
            if(start.getColumn() < 8) {//move right
                int newRow = start.getRow() - 1;
                int newCol = start.getColumn() + 1;
                while(newRow >= 1 && newCol <= 8) {
                    ChessPosition newPosition = new ChessPosition(newRow, newCol);
                    if(board.getPiece(newPosition) == null) {
                        ChessMove possibleMove = new ChessMove(start, newPosition);
                        moves.add(possibleMove);
                    }
                    if(board.getPiece(newPosition) != null) {//capture
                        pieceConfrontations(board, start, newPosition, moves);
                        break;
                    }
                    newRow -= 1;
                    newCol += 1;
                }
            }
            if(start.getColumn() > 1) {//move left
                int newRow = start.getRow() - 1;
                int newCol = start.getColumn() - 1;
                while(newRow >= 1 && newCol >= 1) {
                    ChessPosition newPosition = new ChessPosition(newRow, newCol);
                    if(board.getPiece(newPosition) == null) {
                        ChessMove possibleMove = new ChessMove(start, newPosition);
                        moves.add(possibleMove);
                    }
                    if(board.getPiece(newPosition) != null) {//capture
                        pieceConfrontations(board, start, newPosition, moves);
                        break;
                    }
                    newRow -= 1;
                    newCol -= 1;
                }
            }
        }
        return moves;
    }
    private HashSet<ChessMove> queenMoves(ChessBoard board, ChessPosition start) {
        HashSet<ChessMove> rooklikeMoves = rookMoves(board, start);
        HashSet<ChessMove> bishoplikeMoves = bishopMoves(board, start);
        HashSet<ChessMove> queenMoves = new HashSet<>();
        queenMoves.addAll(rooklikeMoves);
        queenMoves.addAll(bishoplikeMoves);
        return queenMoves;
    }
    private HashSet<ChessMove> kingMoves(ChessBoard board, ChessPosition start) {
        HashSet<ChessMove> moves = new HashSet<>();
        if(start.getRow() < 8) {
            ChessPosition up = new ChessPosition(start.getRow() + 1, start.getColumn());
            if(board.getPiece(up) == null) {
                ChessMove possibleMove = new ChessMove(start, up);
                moves.add(possibleMove);
            }
            if(board.getPiece(up) != null) {
                pieceConfrontations(board, start, up, moves);
            }
            if(start.getColumn() < 8) {
                int newRow = start.getRow() + 1;
                int newCol = start.getColumn() + 1;
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                if(board.getPiece(newPosition) == null) {
                    ChessMove possibleMove = new ChessMove(start, newPosition);
                    moves.add(possibleMove);
                }
                if(board.getPiece(newPosition) != null) {//capture
                    pieceConfrontations(board, start, newPosition, moves);
                }
            }
            if(start.getColumn() > 1) {
                int newRow = start.getRow() + 1;
                int newCol = start.getColumn() - 1;
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                if(board.getPiece(newPosition) == null) {
                    ChessMove possibleMove = new ChessMove(start, newPosition);
                    moves.add(possibleMove);
                }
                if(board.getPiece(newPosition) != null) {//capture
                    pieceConfrontations(board, start, newPosition, moves);
                }
            }
        }
        if(start.getRow() > 1) {
            ChessPosition down = new ChessPosition(start.getRow() - 1, start.getColumn());
            if(board.getPiece(down) == null) {
                ChessMove possibleMove = new ChessMove(start, down);
                moves.add(possibleMove);
            }
            if(board.getPiece(down) != null) {
                pieceConfrontations(board, start, down, moves);
            }
            if(start.getColumn() < 8) {
                int newRow = start.getRow() - 1;
                int newCol = start.getColumn() + 1;
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                if(board.getPiece(newPosition) == null) {
                    ChessMove possibleMove = new ChessMove(start, newPosition);
                    moves.add(possibleMove);
                }
                if(board.getPiece(newPosition) != null) {//capture
                    pieceConfrontations(board, start, newPosition, moves);
                }
            }
            if(start.getColumn() > 1) {
                int newRow = start.getRow() - 1;
                int newCol = start.getColumn() - 1;
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                if(board.getPiece(newPosition) == null) {
                    ChessMove possibleMove = new ChessMove(start, newPosition);
                    moves.add(possibleMove);
                }
                if(board.getPiece(newPosition) != null) {//capture
                    pieceConfrontations(board, start, newPosition, moves);
                }
            }
        }
        if(start.getColumn() < 8) {
            ChessPosition right = new ChessPosition(start.getRow(), start.getColumn() + 1);
            if(board.getPiece(right) == null) {
                ChessMove possibleMove = new ChessMove(start, right);
                moves.add(possibleMove);
            }
            if(board.getPiece(right) != null) {
                pieceConfrontations(board, start, right, moves);
            }
        }
        if(start.getColumn() > 1) {
            ChessPosition left = new ChessPosition(start.getRow(), start.getColumn() - 1);
            if(board.getPiece(left) == null) {
                ChessMove possibleMove = new ChessMove(start, left);
                moves.add(possibleMove);
            }
            if(board.getPiece(left) != null) {
                pieceConfrontations(board, start, left, moves);
            }
        }
        return moves;
    }
    private void pieceConfrontations(ChessBoard board, ChessPosition start, ChessPosition end, HashSet<ChessMove> moves) {
        if(board.getPiece(end).getTeamColor() != color) {
            ChessMove capture = new ChessMove(start, end);
            moves.add(capture);
        }
        return;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return color == that.color && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }
}
