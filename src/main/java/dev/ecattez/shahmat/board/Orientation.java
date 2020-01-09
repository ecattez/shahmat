package dev.ecattez.shahmat.board;

public enum Orientation {

    BLACK_VIEW {
        @Override
        public <T> T accept(OrientationVisitor<T> visitor) {
            return visitor.visitBlackView();
        }
    },
    WHITE_VIEW {
        @Override
        public <T> T accept(OrientationVisitor<T> visitor) {
            return visitor.visitWhiteView();
        }
    };

    public abstract <T> T accept(OrientationVisitor<T> visitor);

}
