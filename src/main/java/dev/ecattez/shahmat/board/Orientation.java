package dev.ecattez.shahmat.board;

public enum Orientation {

    DOWNWARD {
        @Override
        public <T> T accept(OrientationVisitor<T> visitor) {
            return visitor.visitDownward();
        }
    },
    UPWARD {
        @Override
        public <T> T accept(OrientationVisitor<T> visitor) {
            return visitor.visitUpward();
        }
    };

    public abstract <T> T accept(OrientationVisitor<T> visitor);

}
