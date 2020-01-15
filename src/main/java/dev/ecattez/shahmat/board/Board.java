package dev.ecattez.shahmat.board;

import dev.ecattez.shahmat.event.PawnPromoted;
import dev.ecattez.shahmat.event.PieceCaptured;
import dev.ecattez.shahmat.event.PiecePositioned;
import dev.ecattez.shahmat.event.PromotionProposed;
import dev.ecattez.shahmat.event.PieceMoved;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Board {

    private final Map<Square, Piece> boardState;

    public Board(Map<Square, Piece> boardState) {
        this.boardState = boardState;
    }

    public Board() {
        this.boardState = new HashMap<>();
    }

    public Optional<Piece> getPiece(Square location) {
        return Optional.ofNullable(boardState.get(location));
    }

    public Piece unsafeGetPiece(Square location) throws NoPieceOnSquare {
        Piece piece = boardState.get(location);
        if (piece == null) {
            throw new NoPieceOnSquare(location);
        }
        return piece;
    }

    public boolean isVacant(Square location) {
        return boardState.get(location) == null;
    }

    public boolean isOpponentOf(Square neighbour, Piece piece) {
        return getPiece(neighbour)
            .filter(piece::isOpponent)
            .isPresent();
    }

    public void apply(PiecePositioned event) {
        boardState.put(event.position, event.piece);
    }

    public void apply(PieceMoved event) {
        boardState.remove(event.from);
        boardState.put(event.to, event.piece);
    }

    public void apply(PieceCaptured event) {
        // Nothing more to do
    }

    public void apply(PromotionProposed event) {
        // Nothing more to do
    }

    public void apply(PawnPromoted event) {
        boardState.put(event.location, event.promotedBy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board1 = (Board) o;
        return Objects.equals(boardState, board1.boardState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardState);
    }

}
