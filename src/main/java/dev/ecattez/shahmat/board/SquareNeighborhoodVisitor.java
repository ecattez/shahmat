package dev.ecattez.shahmat.board;

import java.util.Optional;

public class SquareNeighborhoodVisitor implements OrientationVisitor<Optional<Square>> {

    // todo: maybe try to extract direction ?
    private Direction direction;
    private Square.File file;
    private Square.Rank rank;

    public SquareNeighborhoodVisitor(
        Direction direction,
        Square location
    ) {
        this.direction = direction;
        this.file = location.file;
        this.rank = location.rank;
    }

    @Override
    public Optional<Square> visitBlackView() {
        return direction.accept(new BlackDirectionVisitor());
    }

    @Override
    public Optional<Square> visitWhiteView() {
        return direction.accept(new WhiteDirectionVisitor());
    }

    private final class BlackDirectionVisitor implements DirectionVisitor<Optional<Square>> {

        @Override
        public Optional<Square> visitForward() {
            if (rank.isFirst()) {
                return Optional.empty();
            }
            return Optional.of(new Square(file, rank.previous()));
        }

        @Override
        public Optional<Square> visitForwardLeft() {
            if (rank.isFirst() || file.isLast()) {
                return Optional.empty();
            }
            return Optional.of(new Square(file.next(), rank.previous()));
        }

        @Override
        public Optional<Square> visitForwardRight() {
            if (rank.isFirst() || file.isFirst()) {
                return Optional.empty();
            }
            return Optional.of(new Square(file.previous(), rank.previous()));
        }

        @Override
        public Optional<Square> visitShiftLeft() {
            if (file.isLast()) {
                return Optional.empty();
            }
            return Optional.of(new Square(file.next(), rank));
        }

        @Override
        public Optional<Square> visitShiftRight() {
            if (file.isFirst()) {
                return Optional.empty();
            }
            return Optional.of(new Square(file.previous(), rank));
        }

        @Override
        public Optional<Square> visitBackward() {
            if (rank.isLast()) {
                return Optional.empty();
            }
            return Optional.of(new Square(file, rank.next()));
        }

        @Override
        public Optional<Square> visitBackwardLeft() {
            if (rank.isLast() || file.isLast()) {
                return Optional.empty();
            }
            return Optional.of(new Square(file.next(), rank.next()));
        }

        @Override
        public Optional<Square> visitBackwardRight() {
            if (rank.isLast() || file.isFirst()) {
                return Optional.empty();
            }
            return Optional.of(new Square(file.previous(), rank.next()));
        }

    }

    private final class WhiteDirectionVisitor implements DirectionVisitor<Optional<Square>> {

        @Override
        public Optional<Square> visitForward() {
            if (rank.isLast()) {
                return Optional.empty();
            }
            return Optional.of(new Square(file, rank.next()));
        }

        @Override
        public Optional<Square> visitForwardLeft() {
            if (rank.isLast() || file.isFirst()) {
                return Optional.empty();
            }
            return Optional.of(new Square(file.previous(), rank.next()));
        }

        @Override
        public Optional<Square> visitForwardRight() {
            if (rank.isLast() || file.isLast()) {
                return Optional.empty();
            }
            return Optional.of(new Square(file.next(), rank.next()));
        }

        @Override
        public Optional<Square> visitShiftLeft() {
            if (file.isFirst()) {
                return Optional.empty();
            }
            return Optional.of(new Square(file.previous(), rank));
        }

        @Override
        public Optional<Square> visitShiftRight() {
            if (file.isLast()) {
                return Optional.empty();
            }
            return Optional.of(new Square(file.next(), rank));
        }

        @Override
        public Optional<Square> visitBackward() {
            if (rank.isFirst()) {
                return Optional.empty();
            }
            return Optional.of(new Square(file, rank.previous()));
        }

        @Override
        public Optional<Square> visitBackwardLeft() {
            if (rank.isFirst() || file.isFirst()) {
                return Optional.empty();
            }
            return Optional.of(new Square(file.previous(), rank.previous()));
        }

        @Override
        public Optional<Square> visitBackwardRight() {
            if (rank.isFirst() || file.isLast()) {
                return Optional.empty();
            }
            return Optional.of(new Square(file.next(), rank.previous()));
        }

    }
}
