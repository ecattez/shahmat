package dev.ecattez.shahmat.domain.board.piece;

public enum PieceType {

    PAWN {
        @Override
        public <T> T accept(PieceTypeVisitor<T> visitor) {
            return visitor.visitPawn();
        }
    },
    ROOK {
        @Override
        public <T> T accept(PieceTypeVisitor<T> visitor) {
            return visitor.visitRook();
        }
    },
    BISHOP {
        @Override
        public <T> T accept(PieceTypeVisitor<T> visitor) {
            return visitor.visitBishop();
        }
    },
    KNIGHT {
        @Override
        public <T> T accept(PieceTypeVisitor<T> visitor) {
            return visitor.visitKnight();
        }
    },
    QUEEN {
        @Override
        public <T> T accept(PieceTypeVisitor<T> visitor) {
            return visitor.visitQueen();
        }
    },
    KING {
        @Override
        public <T> T accept(PieceTypeVisitor<T> visitor) {
            return visitor.visitKing();
        }
    };

    public abstract <T> T accept(PieceTypeVisitor<T> visitor);

}
