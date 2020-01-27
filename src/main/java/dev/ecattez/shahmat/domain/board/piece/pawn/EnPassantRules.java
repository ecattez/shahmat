package dev.ecattez.shahmat.domain.board.piece.pawn;

import dev.ecattez.shahmat.domain.board.Direction;
import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.piece.PieceType;
import dev.ecattez.shahmat.domain.board.square.Square;

public class EnPassantRules {

    private static final EnPassantCaptureRankVisitor EN_PASSANT_CAPTURE_RANK_VISITOR = new EnPassantCaptureRankVisitor();

    private boolean isOnOpponentEnPassantRank(Piece piece, Square location) {
        return piece
            .color()
            .opposite()
            .accept(EN_PASSANT_CAPTURE_RANK_VISITOR)
            .equals(location.rank);
    }

    private boolean hasMovedTwoSquares(Piece piece, Square from, Square to) {
        return from.findNeighbour(Direction.FORWARD, piece.orientation(), 2)
            .filter(to::equals)
            .isPresent();
    }

    public boolean couldBeCapturedEnPassant(Piece piece, Square from, Square to) {
        return piece.isOfType(PieceType.PAWN)
            && isOnOpponentEnPassantRank(piece, to)
            && hasMovedTwoSquares(piece, from, to);
    }

}
