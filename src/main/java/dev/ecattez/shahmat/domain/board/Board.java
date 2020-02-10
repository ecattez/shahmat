package dev.ecattez.shahmat.domain.board;

import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.piece.PieceBox;
import dev.ecattez.shahmat.domain.board.piece.PieceColor;
import dev.ecattez.shahmat.domain.board.piece.PieceType;
import dev.ecattez.shahmat.domain.board.piece.king.King;
import dev.ecattez.shahmat.domain.board.piece.pawn.EnPassantRules;
import dev.ecattez.shahmat.domain.board.square.Square;
import dev.ecattez.shahmat.domain.board.violation.NoPieceOnSquare;
import dev.ecattez.shahmat.domain.event.BoardInitialized;
import dev.ecattez.shahmat.domain.event.ChessEvent;
import dev.ecattez.shahmat.domain.event.EventListener;
import dev.ecattez.shahmat.domain.event.KingChecked;
import dev.ecattez.shahmat.domain.event.PawnPromoted;
import dev.ecattez.shahmat.domain.event.PieceCaptured;
import dev.ecattez.shahmat.domain.event.PieceCapturedEnPassant;
import dev.ecattez.shahmat.domain.event.PieceMoved;
import dev.ecattez.shahmat.domain.event.PiecePositioned;
import dev.ecattez.shahmat.domain.event.PromotionProposed;
import dev.ecattez.shahmat.domain.event.TurnChanged;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Board extends EventListener {

    private final Map<Square, Piece> piecePerSquare;
    private boolean playing;
    private Square enPassantPieceLocation;
    private Square promotionLocation;
    private Square checkedLocation;
    private PieceColor turnOf;

    public Board() {
        this.piecePerSquare = new HashMap<>();
        this.turnOf = PieceColor.WHITE;
        this.startListening();
    }

    public Board(Board board) {
        this.piecePerSquare = new HashMap<>(board.piecePerSquare);
        this.playing = board.playing;
        this.enPassantPieceLocation = board.enPassantPieceLocation;
        this.promotionLocation = board.promotionLocation;
        this.turnOf = board.turnOf;
        this.startListening();
    }

    private void startListening() {
        onEvent(PiecePositioned.class, this::apply);
        onEvent(BoardInitialized.class, this::apply);
        onEvent(PieceCapturedEnPassant.class, this::apply);
        onEvent(PieceCaptured.class, this::apply);
        onEvent(PieceMoved.class, this::apply);
        onEvent(KingChecked.class, this::apply);
        onEvent(PromotionProposed.class, this::apply);
        onEvent(PawnPromoted.class, this::apply);
        onEvent(TurnChanged.class, this::apply);
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

    public Optional<Square> findLocationOfKing(PieceColor color) {
        King toFound = new King(color);

        return piecePerSquare
            .entrySet()
            .stream()
            .filter(entry -> toFound.equals(entry.getValue()))
            .map(Map.Entry::getKey)
            .findFirst();
    }

    public Map<Square, Piece> getOpponentsOf(Piece piece) {
        PieceColor opposite = piece.color().opposite();

        return piecePerSquare
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue().isOfColor(opposite))
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
            ));
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

    public boolean hasOpponent(Square location, Piece piece) {
        return findPiece(location)
            .filter(piece::isOpponent)
            .isPresent();
    }

    public boolean hasAlly(Square location, Piece piece) {
        return findPiece(location)
            .filter(piece::isAlly)
            .isPresent();
    }

    public PieceColor turnOf() {
        return turnOf;
    }

    public boolean isTurnOf(PieceColor color) {
        return turnOf.equals(color);
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
        enPassantPieceLocation = null;
        checkedLocation = null;
    }

    public void apply(PieceCaptured event) {
        Piece capturedBy = event.piece;
        Square from = event.from;
        Square end = event.to;

        piecePerSquare.remove(from);
        piecePerSquare.put(end, capturedBy);
        enPassantPieceLocation = null;
        checkedLocation = null;
    }

    public void apply(PieceMoved event) {
        Piece piece = event.piece;
        Square from = event.from;
        Square to = event.to;

        piecePerSquare.remove(from);
        piecePerSquare.put(to, piece);

        checkedLocation = null;
        enPassantPieceLocation = null;

        if (new EnPassantRules().couldBeCapturedEnPassant(piece, from, to)) {
            enPassantPieceLocation = to;
        }
    }

    public void apply(KingChecked event) {
        ChessEvent move = event.event;
        apply(move);

        checkedLocation = event.kingLocation;
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

    public void apply(TurnChanged event) {
        turnOf = event.color;
    }
}
