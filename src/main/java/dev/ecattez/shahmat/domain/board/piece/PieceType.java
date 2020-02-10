package dev.ecattez.shahmat.domain.board.piece;

public enum PieceType {

    PAWN {
        @Override
        public <T> T accept(PieceTypeVisitor<T> visitor) {
            return visitor.visitPawn();
        }

        public String toString() {
            return "P";
        }
    },
    ROOK {
        @Override
        public <T> T accept(PieceTypeVisitor<T> visitor) {
            return visitor.visitRook();
        }

        public String toString() {
            return "R";
        }
    },
    BISHOP {
        @Override
        public <T> T accept(PieceTypeVisitor<T> visitor) {
            return visitor.visitBishop();
        }

        public String toString() {
            return "B";
        }
    },
    KNIGHT {
        @Override
        public <T> T accept(PieceTypeVisitor<T> visitor) {
            return visitor.visitKnight();
        }

        public String toString() {
            return "N";
        }
    },
    QUEEN {
        @Override
        public <T> T accept(PieceTypeVisitor<T> visitor) {
            return visitor.visitQueen();
        }

        public String toString() {
            return "Q";
        }
    },
    KING {
        @Override
        public <T> T accept(PieceTypeVisitor<T> visitor) {
            return visitor.visitKing();
        }

        public String toString() {
            return "K";
        }
    };

    public abstract <T> T accept(PieceTypeVisitor<T> visitor);

}
