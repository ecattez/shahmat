package dev.ecattez.shahmat.domain.board.square;

import dev.ecattez.shahmat.domain.board.Direction;
import dev.ecattez.shahmat.domain.board.Orientation;
import dev.ecattez.shahmat.domain.board.violation.FileOutsideOfBoard;
import dev.ecattez.shahmat.domain.board.violation.InvalidPosition;
import dev.ecattez.shahmat.domain.board.violation.RankOutsideOfBoard;
import dev.ecattez.shahmat.domain.board.violation.SquareOutsideOfBoard;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class Square {

    public static final String FILE_PATTERN = "[aAbBcCdDeEfFgGhH]";
    public static final String RANK_PATTERN = "[1-8]";
    public static final String SQUARE_PATTERN = FILE_PATTERN + RANK_PATTERN;

    public final File file;
    public final Rank rank;

    public Square(String position) throws InvalidPosition {
        if (position == null || !position.matches(SQUARE_PATTERN)) {
            throw new InvalidPosition(position + " is not a valid square");
        }
        this.file = File.valueOf(position.substring(0, 1).toUpperCase());
        this.rank = Rank.valueOf(Integer.parseInt(position.substring(1, 2)));
    }

    public Square(File file, Rank rank) throws InvalidPosition {
        if (file == null) {
            throw new InvalidPosition("File can not be null");
        }
        if (rank == null) {
            throw new InvalidPosition("Rank can not be null");
        }
        this.file = file;
        this.rank = rank;
    }

    public Optional<Square> findNeighbour(Direction direction, Orientation orientation) {
        return orientation.accept(
            new SquareNeighborhoodVisitor(direction, this)
        );
    }

    public Square getNeighbour(Direction direction, Orientation orientation) throws SquareOutsideOfBoard {
        return findNeighbour(direction, orientation)
            .orElseThrow(SquareOutsideOfBoard::new);
    }

    public Optional<Square> findNeighbour(Direction direction, Orientation orientation, int times) {
        return findNeighbour(direction, orientation, this, times);
    }

    private Optional<Square> findNeighbour(Direction direction, Orientation orientation, Square current, int times) {
        if (times <= 0) {
            return Optional.of(current);
        }
        return orientation.accept(new SquareNeighborhoodVisitor(direction, current))
            .flatMap(found -> findNeighbour(direction, orientation, found, times - 1));
    }

    public Square getNeighbour(Direction direction, Orientation orientation, int times) throws SquareOutsideOfBoard {
        return findNeighbour(direction, orientation, times)
            .orElseThrow(SquareOutsideOfBoard::new);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Square square = (Square) o;
        return Objects.equals(file, square.file) &&
            Objects.equals(rank, square.rank);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, rank);
    }

    @Override
    public String toString() {
        return file.toString() + rank.toString();
    }

    public enum File {

        A,
        B,
        C,
        D,
        E,
        F,
        G,
        H;

        @Override
        public String toString() {
            return name().toLowerCase();
        }

        public boolean isFirst() {
            return A.equals(this);
        }

        public boolean isLast() {
            return H.equals(this);
        }

        public boolean hasPrevious() {
            return !isFirst();
        }

        public boolean hasNext() {
            return !isLast();
        }

        public File previous() throws FileOutsideOfBoard {
            if (hasPrevious()) {
                return File.values()[ordinal() - 1];
            }
            throw new FileOutsideOfBoard();
        }

        public File next() throws FileOutsideOfBoard {
            if (hasNext()) {
                return File.values()[ordinal() + 1];
            }
            throw new FileOutsideOfBoard();
        }

        public static File first() {
            return A;
        }

        public static File last() {
            return H;
        }

    }

    public enum Rank {

        ONE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8);

        public final int value;

        Rank(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value).toLowerCase();
        }

        public boolean isFirst() {
            return ONE.equals(this);
        }

        public boolean isLast() {
            return EIGHT.equals(this);
        }

        public boolean hasPrevious() {
            return !isFirst();
        }

        public boolean hasNext() {
            return !isLast();
        }

        public Rank previous() throws RankOutsideOfBoard {
            if (hasPrevious()) {
                return Rank.values()[ordinal() - 1];
            }
            throw new RankOutsideOfBoard();
        }

        public Rank next() throws RankOutsideOfBoard {
            if (hasNext()) {
                return Rank.values()[ordinal() + 1];
            }
            throw new RankOutsideOfBoard();
        }

        public static Rank first() {
            return ONE;
        }

        public static Rank last() {
            return EIGHT;
        }

        public static Rank valueOf(int value) {
            return Stream.of(Rank.values())
                .filter(rankValue -> value == rankValue.value)
                .findFirst()
                .orElseThrow(RankOutsideOfBoard::new);
        }
    }

    public final static class SquareIterator implements Iterator<Square> {

        private static final Square LAST = new Square(Square.File.last(), Square.Rank.last());
        private Square current;

        @Override
        public boolean hasNext() {
            return !LAST.equals(current);
        }

        @Override
        public Square next() {
            if (current == null) {
                current = new Square(Square.File.first(), Square.Rank.first());
                return current;
            }
            if (current.file.hasNext()) {
                current = new Square(current.file.next(), current.rank);
                return current;
            }
            if (current.rank.hasNext()) {
                current = new Square(Square.File.first(), current.rank.next());
                return current;
            }
            throw new SquareOutsideOfBoard();
        }

    }

}
