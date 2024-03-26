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
        color = pieceColor;
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
            return bishopMoves(board, myPosition);
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
            if(start.getRow() == 2) {
                ChessPosition hurdle = new ChessPosition(3, start.getColumn());
                if(board.getPiece(hurdle) == null) {
                    ChessPosition doubleJump = new ChessPosition(4, start.getColumn());
                    if(board.getPiece(doubleJump) == null) {
                        pawnMoveAdder(start, doubleJump, moves);
                    }
                }
            }
            if(start.getRow() < 7) {
                ChessPosition forward = new ChessPosition(start.getRow() + 1, start.getColumn());
                if(board.getPiece(forward) == null) {
                    pawnMoveAdder(start, forward, moves);
                }
                whiteDiags(board, start, moves);
            }
            if(start.getRow() == 7) {
                ChessPosition forward = new ChessPosition(start.getRow() + 1, start.getColumn());
                if(board.getPiece(forward) == null) {
                    pawnUpgrades(start, forward, moves);
                }
                whiteDiags(board, start, moves);
            }
        }
        if(color == ChessGame.TeamColor.BLACK) {
            if(start.getRow() == 7) {
                ChessPosition hurdle = new ChessPosition(6, start.getColumn());
                if(board.getPiece(hurdle) == null) {
                    ChessPosition doubleJump = new ChessPosition(5, start.getColumn());
                    if(board.getPiece(doubleJump) == null) {
                        pawnMoveAdder(start, doubleJump, moves);
                    }
                }
            }
            if(start.getRow() > 2) {
                ChessPosition forward = new ChessPosition(start.getRow() - 1, start.getColumn());
                if(board.getPiece(forward) == null) {
                    pawnMoveAdder(start, forward, moves);
                }
                blackDiags(board, start, moves);
            }
            if(start.getRow() == 2) {
                ChessPosition forward = new ChessPosition(start.getRow() - 1, start.getColumn());
                if(board.getPiece(forward) == null) {
                    pawnUpgrades(start, forward, moves);
                }
                blackDiags(board, start, moves);
            }
        }
        return moves;
    }
    private void whiteDiags(ChessBoard board, ChessPosition start, HashSet<ChessMove> moves) {
        if(start.getColumn() < 8) {
            ChessPosition rightDiag = new ChessPosition(start.getRow() + 1, start.getColumn() + 1);
            pawnConfrontations(start, rightDiag, board, moves);
        }
        if(start.getColumn() > 1) {
            ChessPosition leftDiag = new ChessPosition(start.getRow() + 1, start.getColumn() - 1);
            pawnConfrontations(start, leftDiag, board, moves);
        }
    }
    private void blackDiags(ChessBoard board, ChessPosition start, HashSet<ChessMove> moves) {
        if(start.getColumn() < 8) {
            ChessPosition rightDiag = new ChessPosition(start.getRow() - 1, start.getColumn() + 1);
            pawnConfrontations(start, rightDiag, board, moves);
        }
        if(start.getColumn() > 1) {
            ChessPosition leftDiag = new ChessPosition(start.getRow() - 1, start.getColumn() - 1);
            pawnConfrontations(start, leftDiag, board, moves);
        }
    }
    private void pawnMoveAdder(ChessPosition start, ChessPosition end, HashSet<ChessMove> moves) {
        ChessMove newMove = new ChessMove(start, end);
        moves.add(newMove);
    }
    private void pawnConfrontations(ChessPosition start, ChessPosition end, ChessBoard board, HashSet<ChessMove> moves) {
        if(board.getPiece(end) != null) {
            if(board.getPiece(end).getTeamColor() != color) {
                if(end.getRow() == 8 || end.getRow() == 1) {
                    pawnUpgrades(start, end, moves);
                    return;
                }
                pawnMoveAdder(start, end, moves);
            }
        }
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
    }
    private HashSet<ChessMove> rookMoves(ChessBoard board, ChessPosition start) {
        HashSet<ChessMove> moves = new HashSet<>();
        int newRow = start.getRow() + 1;
        while(newRow <= 8) {
            ChessPosition up = new ChessPosition(newRow, start.getColumn());
            boolean confrontation = moveAdder(start, up, board, moves);
            newRow += 1;
            if(confrontation) {
                break;
            }
        }
        newRow = start.getRow() - 1;
        while (newRow >= 1) {
            ChessPosition down = new ChessPosition(newRow, start.getColumn());
            boolean confrontation = moveAdder(start, down, board, moves);
            newRow -= 1;
            if(confrontation) {
                break;
            }
        }
        int newCol = start.getColumn() + 1;
        while(newCol <= 8) {
            ChessPosition right = new ChessPosition(start.getRow(), newCol);
            boolean confrontation = moveAdder(start, right, board, moves);
            newCol += 1;
            if(confrontation) {
                break;
            }
        }
        newCol = start.getColumn() - 1;
        while(newCol >= 1) {
            ChessPosition left = new ChessPosition(start.getRow(), newCol);
            boolean confrontation = moveAdder(start, left, board, moves);
            newCol -= 1;
            if(confrontation) {
                break;
            }
        }
        return moves;
    }
    private HashSet<ChessMove> knightMoves(ChessBoard board, ChessPosition start) {
        HashSet<ChessMove> moves = new HashSet<>();
        if(start.getRow() < 8) {
            if(start.getRow() < 7) {
                if(start.getColumn() < 8) {
                    ChessPosition newPosition = new ChessPosition(start.getRow() + 2, start.getColumn() + 1);
                    moveAdder(start, newPosition, board, moves);
                }
                if(start.getColumn() > 1) {
                    ChessPosition newPosition = new ChessPosition(start.getRow() + 2, start.getColumn() - 1);
                    moveAdder(start, newPosition, board, moves);
                }
            }
            if(start.getColumn() < 7) {
                ChessPosition newPosition = new ChessPosition(start.getRow() + 1, start.getColumn() + 2);
                moveAdder(start, newPosition, board, moves);
            }
            if(start.getColumn() > 2) {
                ChessPosition newPosition = new ChessPosition(start.getRow() + 1, start.getColumn() - 2);
                moveAdder(start, newPosition, board, moves);
            }
        }
        if(start.getRow() > 1) {
            if(start.getRow() > 2) {
                if(start.getColumn() < 8) {
                    ChessPosition newPosition = new ChessPosition(start.getRow() - 2, start.getColumn() + 1);
                    moveAdder(start, newPosition, board, moves);
                }
                if(start.getColumn() > 1) {
                    ChessPosition newPosition = new ChessPosition(start.getRow() - 2, start.getColumn() - 1);
                    moveAdder(start, newPosition, board, moves);
                }
            }
            if(start.getColumn() < 7) {
                ChessPosition newPosition = new ChessPosition(start.getRow() - 1, start.getColumn() + 2);
                moveAdder(start, newPosition, board, moves);
            }
            if(start.getColumn() > 2) {
                ChessPosition newPosition = new ChessPosition(start.getRow() - 1, start.getColumn() - 2);
                moveAdder(start, newPosition, board, moves);
            }
        }
        return moves;
    }
    private HashSet<ChessMove> bishopMoves(ChessBoard board, ChessPosition start) {
        HashSet<ChessMove> moves = new HashSet<>();
        int newRow = start.getRow() + 1;
        int newCol = start.getColumn() + 1;
        while(newRow <= 8 && newCol <= 8) {
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            boolean confrontation = moveAdder(start, newPosition, board, moves);
            if(confrontation) {
                break;
            }
            newRow += 1;
            newCol += 1;
        }
        newRow = start.getRow() + 1;
        newCol = start.getColumn() - 1;
        while(newRow <= 8 && newCol >= 1) {
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            boolean confrontation = moveAdder(start, newPosition, board, moves);
            if(confrontation) {
                break;
            }
            newRow += 1;
            newCol -= 1;
        }
        newRow = start.getRow() - 1;
        newCol = start.getColumn() - 1;
        while(newRow >= 1 && newCol >= 1) {
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            boolean confrontation = moveAdder(start, newPosition, board, moves);
            if(confrontation) {
                break;
            }
            newRow -= 1;
            newCol -= 1;
        }
        newRow = start.getRow() - 1;
        newCol = start.getColumn() + 1;
        while(newRow >= 1 && newCol <= 8) {
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            boolean confrontation = moveAdder(start, newPosition, board, moves);
            if(confrontation) {
                break;
            }
            newRow -= 1;
            newCol += 1;
        }
        return moves;
    }
    private HashSet<ChessMove> queenMoves(ChessBoard board, ChessPosition start) {
        HashSet<ChessMove> rookLike = rookMoves(board, start);
        HashSet<ChessMove> bishopLike = bishopMoves(board, start);
        HashSet<ChessMove> queenMoves = new HashSet<>();
        queenMoves.addAll(rookLike);
        queenMoves.addAll(bishopLike);
        return queenMoves;
    }
    private HashSet<ChessMove> kingMoves(ChessBoard board, ChessPosition start) {
        HashSet<ChessMove> moves = new HashSet<>();
        if(start.getRow() < 8) {
            ChessPosition newPosition = new ChessPosition(start.getRow() + 1, start.getColumn());
            moveAdder(start, newPosition, board, moves);
        }
        if(start.getRow() < 8 && start.getColumn() < 8) {
            ChessPosition newPosition = new ChessPosition(start.getRow() + 1, start.getColumn() + 1);
            moveAdder(start, newPosition, board, moves);
        }
        if(start.getColumn() < 8) {
            ChessPosition newPosition = new ChessPosition(start.getRow(), start.getColumn() + 1);
            moveAdder(start, newPosition, board, moves);
        }
        if(start.getRow() > 1 && start.getColumn() < 8) {
            ChessPosition newPosition = new ChessPosition(start.getRow() - 1, start.getColumn() + 1);
            moveAdder(start, newPosition, board, moves);
        }
        if(start.getRow() > 1) {
            ChessPosition newPosition = new ChessPosition(start.getRow() - 1, start.getColumn());
            moveAdder(start, newPosition, board, moves);
        }
        if(start.getRow() > 1 && start.getColumn() > 1) {
            ChessPosition newPosition = new ChessPosition(start.getRow() - 1, start.getColumn() - 1);
            moveAdder(start, newPosition, board, moves);
        }
        if(start.getColumn() > 1) {
            ChessPosition newPosition = new ChessPosition(start.getRow(), start.getColumn() - 1);
            moveAdder(start, newPosition, board, moves);
        }
        if(start.getRow() < 8 && start.getColumn() > 1) {
            ChessPosition newPosition = new ChessPosition(start.getRow() + 1, start.getColumn() - 1);
            moveAdder(start, newPosition, board, moves);
        }
        return moves;
    }
    private boolean moveAdder(ChessPosition start, ChessPosition end, ChessBoard board, HashSet<ChessMove> moves) {
        boolean confrontation = false;
        if(board.getPiece(end) == null) {
            ChessMove possibleMove = new ChessMove(start, end);
            moves.add(possibleMove);
        }
        if(board.getPiece(end) != null) {
            pieceConfrontations(start, end, board, moves);
            confrontation = true;
        }
        return confrontation;
    }
    private void pieceConfrontations(ChessPosition start, ChessPosition end, ChessBoard board, HashSet<ChessMove> moves) {
        if(board.getPiece(end).getTeamColor() != color) {
            ChessMove capture = new ChessMove(start, end);
            moves.add(capture);
        }
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
