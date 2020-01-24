package dev.ecattez.shahmat.domain.board.piece;

public enum PieceColor {

    BLACK {
        @Override
        public <T> T accept(PieceColorVisitor<T> visitor) {
            return visitor.visitBlack();
        }

        @Override
        public PieceColor opposite() {
            return WHITE;
        }
    },
    WHITE {
        @Override
        public <T> T accept(PieceColorVisitor<T> visitor) {
            return visitor.visitWhite();
        }

        @Override
        public PieceColor opposite() {
            return BLACK;
        }
    };

    public abstract <T> T accept(PieceColorVisitor<T> visitor);

    public abstract PieceColor opposite();

}
