package dev.ecattez.shahmat.domain.board;

public enum Orientation {

    DOWNWARD {
        @Override
        public <T> T accept(OrientationVisitor<T> visitor) {
            return visitor.visitDownward();
        }

        @Override
        public Orientation reverse() {
            return UPWARD;
        }
    },
    UPWARD {
        @Override
        public <T> T accept(OrientationVisitor<T> visitor) {
            return visitor.visitUpward();
        }

        @Override
        public Orientation reverse() {
            return DOWNWARD;
        }
    };

    public abstract <T> T accept(OrientationVisitor<T> visitor);

    public abstract Orientation reverse();

}
