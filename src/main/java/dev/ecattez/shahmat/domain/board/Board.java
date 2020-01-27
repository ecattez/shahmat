package dev.ecattez.shahmat.domain.board;

import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.piece.PieceBox;
import dev.ecattez.shahmat.domain.board.piece.PieceType;
import dev.ecattez.shahmat.domain.board.piece.pawn.EnPassantRules;
import dev.ecattez.shahmat.domain.board.square.Square;
import dev.ecattez.shahmat.domain.board.violation.NoPieceOnSquare;
import dev.ecattez.shahmat.domain.event.BoardInitialized;
import dev.ecattez.shahmat.domain.event.ChessEvent;
import dev.ecattez.shahmat.domain.event.EventListener;
import dev.ecattez.shahmat.domain.event.PawnPromoted;
import dev.ecattez.shahmat.domain.event.PieceCaptured;
import dev.ecattez.shahmat.domain.event.PieceCapturedEnPassant;
import dev.ecattez.shahmat.domain.event.PieceMoved;
import dev.ecattez.shahmat.domain.event.PiecePositioned;
import dev.ecattez.shahmat.domain.event.PromotionProposed;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Board extends EventListener {

    private final Map<Square, Piece> piecePerSquare;
    private boolean playing;
    private Square enPassantPieceLocation;
    private Square promotionLocation;

    public Board() {
        this.piecePerSquare = new HashMap<>();

        onEvent(PiecePositioned.class, this::apply);
        onEvent(BoardInitialized.class, this::apply);
        onEvent(PieceCapturedEnPassant.class, this::apply);
        onEvent(PieceCaptured.class, this::apply);
        onEvent(PieceMoved.class, this::apply);
        onEvent(PromotionProposed.class, this::apply);
        onEvent(PawnPromoted.class, this::apply);
    }

    public Optional<Square> findEnPassantPieceLocation() {
        return Optional.ofNullable(enPassantPieceLocation);
    }

    public Optional<Piece> findPiece(Square location) {
        return Optional.ofNullable(piecePerSquare.get(location));
    }

    public Piece getPiece(Square location) throws NoPieceOnSquare {
        return findPiece(location)
            .orElseThrow(() -> new NoPieceOnSquare(location));
    }

    public boolean isPromoting() {
        return promotionLocation != null;
    }

    public boolean isPromotingIn(Square location) {
        return location.equals(promotionLocation);
    }

    public boolean isVacant(Square location) {
        return piecePerSquare.get(location) == null;
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean isOpponentOf(Square neighbour, Piece piece) {
        return findPiece(neighbour)
            .filter(piece::isOpponent)
            .isPresent();
    }

    @Override
    public void apply(ChessEvent event) {
        enPassantPieceLocation = null;
        super.apply(event);
    }

    public void apply(PiecePositioned event) {
        Piece piece = event.piece;
        Square position = event.position;

        piecePerSquare.put(position, piece);
    }

    public void apply(BoardInitialized event) {
        this.playing = true;
    }

    public void apply(PieceCapturedEnPassant event) {
        this.apply((PieceCaptured) event);

        Piece captured = event.captured;
        Square endingTo = event.to;

        Square opponentSquare = endingTo.getNeighbour(
            Direction.FORWARD,
            captured.orientation()
        );

        piecePerSquare.remove(opponentSquare);
    }

    public void apply(PieceCaptured event) {
        Piece capturedBy = event.capturedBy;
        Square from = event.from;
        Square end = event.to;

        piecePerSquare.remove(from);
        piecePerSquare.put(end, capturedBy);
    }

    public void apply(PieceMoved event) {
        Piece piece = event.piece;
        Square from = event.from;
        Square to = event.to;

        piecePerSquare.remove(from);
        piecePerSquare.put(to, piece);

        if (new EnPassantRules().couldBeCapturedEnPassant(piece, from, to)) {
            enPassantPieceLocation = to;
        }
    }

    public void apply(PromotionProposed event) {
        this.promotionLocation = event.location;
    }

    public void apply(PawnPromoted event) {
        Square location = event.location;
        PieceType promotedTo = event.promotedTo;
        Piece pawn = getPiece(location);
        Piece promotion = PieceBox
            .getInstance()
            .createPiece(promotedTo, pawn.color());

        piecePerSquare.put(
            location,
            promotion
        );

        promotionLocation = null;
    }

}
