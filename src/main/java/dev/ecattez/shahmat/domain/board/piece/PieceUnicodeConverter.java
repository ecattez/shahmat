package dev.ecattez.shahmat.domain.board.piece;

public class PieceUnicodeConverter implements PieceColorVisitor<String> {

    private static final BlackPieceTypeUnicodeVisitor blackPieceTypeUnicodeVisitor = new BlackPieceTypeUnicodeVisitor();
    private static final WhitePieceTypeUnicodeVisitor whitePieceTypeUnicodeVisitor = new WhitePieceTypeUnicodeVisitor();

    private final Piece piece;

    private PieceUnicodeConverter(Piece piece) {
        this.piece = piece;
    }

    @Override
    public String visitBlack() {
        return piece.type().accept(blackPieceTypeUnicodeVisitor);
    }

    @Override
    public String visitWhite() {
        return piece.type().accept(whitePieceTypeUnicodeVisitor);
    }

    public static String convert(Piece piece) {
        return piece.color().accept(new PieceUnicodeConverter(piece));
    }

    private static class BlackPieceTypeUnicodeVisitor implements PieceTypeVisitor<String> {
        @Override
        public String visitPawn() {
            return "♟";
        }

        @Override
        public String visitRook() {
            return "♜";
        }

        @Override
        public String visitBishop() {
            return "♝";
        }

        @Override
        public String visitKnight() {
            return "♞";
        }

        @Override
        public String visitQueen() {
            return "♛";
        }

        @Override
        public String visitKing() {
            return "♚";
        }
    }

    private static class WhitePieceTypeUnicodeVisitor implements PieceTypeVisitor<String> {
        @Override
        public String visitPawn() {
            return "♙";
        }

        @Override
        public String visitRook() {
            return "♖";
        }

        @Override
        public String visitBishop() {
            return "♗";
        }

        @Override
        public String visitKnight() {
            return "♘";
        }

        @Override
        public String visitQueen() {
            return "♕";
        }

        @Override
        public String visitKing() {
            return "♔";
        }
    }

}
