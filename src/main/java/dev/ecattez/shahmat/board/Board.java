package dev.ecattez.shahmat.board;

import dev.ecattez.shahmat.board.violation.NoPieceOnSquare;
import dev.ecattez.shahmat.event.PawnPromoted;
import dev.ecattez.shahmat.event.PieceCaptured;
import dev.ecattez.shahmat.event.PieceMoved;
import dev.ecattez.shahmat.event.PiecePositioned;
import dev.ecattez.shahmat.event.PromotionProposed;

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

    public Optional<Piece> findPiece(Square location) {
        return Optional.ofNullable(boardState.get(location));
    }

    public Piece getPiece(Square location) throws NoPieceOnSquare {
        return findPiece(location)
            .orElseThrow(() -> new NoPieceOnSquare(location));
    }

    public boolean isVacant(Square location) {
        return boardState.get(location) == null;
    }

    public boolean isOpponentOf(Square neighbour, Piece piece) {
        return findPiece(neighbour)
            .filter(piece::isOpponent)
            .isPresent();
    }

    public void apply(PiecePositioned event) {
        boardState.put(event.position, event.piece);
    }

    public void apply(PieceCaptured event) {
        findPiece(event.location)
            .filter(event.captured::equals)
            .ifPresent(captured -> boardState.remove(event.location));
    }

    public void apply(PieceMoved event) {
        boardState.remove(event.from);
        boardState.put(event.to, event.piece);
    }

    public void apply(PromotionProposed event) {
        // Nothing more to do
    }

    public void apply(PawnPromoted event) {
        boardState.put(event.location, event.promotedTo);
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

    @Override
    public String toString() {
        return new BoardStringFormatter().format(this);
    }

}
