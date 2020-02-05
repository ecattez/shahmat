package dev.ecattez.shahmat.infra.projection;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.ecattez.shahmat.domain.board.piece.PieceColor;
import dev.ecattez.shahmat.infra.controller.HalBoardSerializer;

import java.util.Collections;
import java.util.List;

@JsonSerialize(using = HalBoardSerializer.class)
public class HalBoard {

    private String id;
    private List<HalSquare> squares;
    private List<HalPiece> capturedByBlacks;
    private List<HalPiece> capturedByWhites;
    private String turnOf;
    private String victoryOf;

    public HalBoard(
        String id,
        List<HalSquare> squares,
        List<HalPiece> capturedByBlacks,
        List<HalPiece> capturedByWhites,
        String turnOf,
        String victoryOf
    ) {
        this.id = id;
        this.squares = squares;

        this.capturedByBlacks = capturedByBlacks;
        this.capturedByWhites = capturedByWhites;
        this.turnOf = turnOf;
        this.victoryOf = victoryOf;
    }

    public String getId() {
        return id;
    }

    public List<HalSquare> getSquares() {
        return squares;
    }

    public List<HalPiece> getCapturedByBlacks() {
        return capturedByBlacks;
    }

    public List<HalPiece> getCapturedByWhites() {
        return capturedByWhites;
    }

    public long getLivingWhitePieces() {
        return squares
            .stream()
            .filter(square -> !square.isVacant())
            .filter(square -> PieceColor.WHITE.name().equals(square.getPiece().getColor()))
            .count();
    }

    public long getLivingBlackPieces() {
        return squares
            .stream()
            .filter(square -> !square.isVacant())
            .filter(square -> PieceColor.BLACK.name().equals(square.getPiece().getColor()))
            .count();
    }

    public String getTurnOf() {
        return turnOf;
    }

    public String getVictoryOf() {
        return victoryOf;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String id;
        private String turnOf;
        private List<HalSquare> squares;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder turnOf(String turnOf) {
            this.turnOf = turnOf;
            return this;
        }

        public Builder squares(List<HalSquare> pieces) {
            this.squares = pieces;
            return this;
        }

        public HalBoard build() {
            return new HalBoard(
                id,
                squares,
                Collections.emptyList(),
                Collections.emptyList(),
                turnOf,
                null
            );
        }

    }

}
