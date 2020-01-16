package dev.ecattez.shahmat.board;

public enum Direction {

    FORWARD {
        @Override
        public <T> T accept(DirectionVisitor<T> visitor) {
            return visitor.visitForward();
        }
    },
    FORWARD_LEFT {
        @Override
        public <T> T accept(DirectionVisitor<T> visitor) {
            return visitor.visitForwardLeft();
        }
    },
    FORWARD_RIGHT {
        @Override
        public <T> T accept(DirectionVisitor<T> visitor) {
            return visitor.visitForwardRight();
        }
    },
    SHIFT_LEFT {
        @Override
        public <T> T accept(DirectionVisitor<T> visitor) {
            return visitor.visitShiftLeft();
        }
    },
    SHIFT_RIGHT {
        @Override
        public <T> T accept(DirectionVisitor<T> visitor) {
            return visitor.visitShiftRight();
        }
    },
    BACKWARD {
        @Override
        public <T> T accept(DirectionVisitor<T> visitor) {
            return visitor.visitBackward();
        }
    },
    BACKWARD_LEFT {
        @Override
        public <T> T accept(DirectionVisitor<T> visitor) {
            return visitor.visitBackwardLeft();
        }
    },
    BACKWARD_RIGHT {
        @Override
        public <T> T accept(DirectionVisitor<T> visitor) {
            return visitor.visitBackwardRight();
        }
    };

    public abstract <T> T accept(DirectionVisitor<T> visitor);

}
