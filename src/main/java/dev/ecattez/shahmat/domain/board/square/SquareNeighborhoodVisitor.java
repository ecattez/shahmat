package dev.ecattez.shahmat.domain.board.square;

import dev.ecattez.shahmat.domain.board.Direction;
import dev.ecattez.shahmat.domain.board.DirectionVisitor;
import dev.ecattez.shahmat.domain.board.OrientationVisitor;

import java.util.Optional;
import java.util.function.Function;

public class SquareNeighborhoodVisitor implements OrientationVisitor<Function<Direction, Function<Square, Optional<Square>>>> {

    private static final WhiteDirectionVisitor WHITE_DIRECTION_VISITOR = new WhiteDirectionVisitor();
    private static final BlackDirectionVisitor BLACK_DIRECTION_VISITOR = new BlackDirectionVisitor();
    private static SquareNeighborhoodVisitor instance;

    public static SquareNeighborhoodVisitor getInstance() {
        if (instance == null) {
            instance = new SquareNeighborhoodVisitor();
        }
        return instance;
    }

    private SquareNeighborhoodVisitor() {}

    @Override
    public Function<Direction, Function<Square, Optional<Square>>> visitUpward() {
        return (direction) -> direction.accept(WHITE_DIRECTION_VISITOR);
    }

    @Override
    public Function<Direction, Function<Square, Optional<Square>>> visitDownward() {
        return (direction) -> direction.accept(BLACK_DIRECTION_VISITOR);
    }

    private static final class BlackDirectionVisitor implements DirectionVisitor<Function<Square, Optional<Square>>> {

        @Override
        public Function<Square, Optional<Square>> visitForward() {
            return (from) -> Optional.of(from)
                .filter(current -> !current.rank.isFirst())
                .map(current -> new Square(current.file, current.rank.previous()));
        }

        @Override
        public Function<Square, Optional<Square>> visitForwardLeft() {
            return (from) -> Optional.of(from)
                .filter(current -> !current.rank.isFirst() && !current.file.isLast())
                .map(current -> new Square(current.file.next(), current.rank.previous()));
        }

        @Override
        public Function<Square, Optional<Square>> visitForwardRight() {
            return (from) -> Optional.of(from)
                .filter(current -> !current.rank.isFirst() && !current.file.isFirst())
                .map(current -> new Square(current.file.previous(), current.rank.previous()));
        }

        @Override
        public Function<Square, Optional<Square>> visitShiftLeft() {
            return (from) -> Optional.of(from)
                .filter(current -> !current.file.isLast())
                .map(current -> new Square(current.file.next(), current.rank));
        }

        @Override
        public Function<Square, Optional<Square>> visitShiftRight() {
            return (from) -> Optional.of(from)
                .filter(current -> !current.file.isFirst())
                .map(current -> new Square(current.file.previous(), current.rank));
        }

        @Override
        public Function<Square, Optional<Square>> visitBackward() {
            return (from) -> Optional.of(from)
                .filter(current -> !current.rank.isLast())
                .map(current -> new Square(current.file, current.rank.next()));
        }

        @Override
        public Function<Square, Optional<Square>> visitBackwardLeft() {
            return (from) -> Optional.of(from)
                .filter(current -> !current.rank.isLast() && !current.file.isLast())
                .map(current -> new Square(current.file.next(), current.rank.next()));
        }

        @Override
        public Function<Square, Optional<Square>> visitBackwardRight() {
            return (from) -> Optional.of(from)
                .filter(current -> !current.rank.isLast() && !current.file.isFirst())
                .map(current -> new Square(current.file.previous(), current.rank.next()));
        }

    }

    private static final class WhiteDirectionVisitor implements DirectionVisitor<Function<Square, Optional<Square>>> {

        @Override
        public Function<Square, Optional<Square>> visitForward() {
            return (from) -> Optional.of(from)
              .filter(current -> !current.rank.isLast())
              .map(current -> new Square(current.file, current.rank.next()));
        }

        @Override
        public Function<Square, Optional<Square>> visitForwardLeft() {
            return (from) -> Optional.of(from)
                .filter(current -> !current.rank.isLast() && !current.file.isFirst())
                .map(current -> new Square(current.file.previous(), current.rank.next()));
        }

        @Override
        public Function<Square, Optional<Square>> visitForwardRight() {
            return (from) -> Optional.of(from)
                .filter(current -> !current.rank.isLast() && !current.file.isLast())
                .map(current -> new Square(current.file.next(), current.rank.next()));
        }

        @Override
        public Function<Square, Optional<Square>> visitShiftLeft() {
            return (from) -> Optional.of(from)
                .filter(current -> !current.file.isFirst())
                .map(current -> new Square(current.file.previous(), current.rank));
        }

        @Override
        public Function<Square, Optional<Square>> visitShiftRight() {
            return (from) -> Optional.of(from)
                .filter(current -> !current.file.isLast())
                .map(current -> new Square(current.file.next(), current.rank));
        }

        @Override
        public Function<Square, Optional<Square>> visitBackward() {
            return (from) -> Optional.of(from)
                .filter(current -> !current.rank.isFirst())
                .map(current -> new Square(current.file, current.rank.previous()));
        }

        @Override
        public Function<Square, Optional<Square>> visitBackwardLeft() {
            return (from) -> Optional.of(from)
                .filter(current -> !current.rank.isFirst() && !current.file.isFirst())
                .map(current -> new Square(current.file.previous(), current.rank.previous()));
        }

        @Override
        public Function<Square, Optional<Square>> visitBackwardRight() {
            return (from) -> Optional.of(from)
                .filter(current -> !current.rank.isFirst() && !current.file.isLast())
                .map(current -> new Square(current.file.next(), current.rank.previous()));
        }

    }
}
