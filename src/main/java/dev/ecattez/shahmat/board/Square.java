package dev.ecattez.shahmat.board;

import dev.ecattez.shahmat.board.violation.FileOutsideOfBoard;
import dev.ecattez.shahmat.board.violation.InvalidPosition;
import dev.ecattez.shahmat.board.violation.RankOutsideOfBoard;
import dev.ecattez.shahmat.board.violation.SquareOutsideOfBoard;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class Square {

    private static final String FILE_PATTERN = "[aAbBcCdDeEfFgGhH]";
    private static final String RANK_PATTERN = "[1-8]";
    private static final String SQUARE_PATTERN = FILE_PATTERN + RANK_PATTERN;

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

    public boolean hasNeighbour(Direction direction, Orientation orientation) {
        return findNeighbour(direction, orientation)
            .isPresent();
    }

    public Optional<Square> findNeighbour(Direction direction, Orientation orientation, int times) {
        Square current = this;
        for (int i = 1; i <= times; i++) {
            Optional<Square> found = orientation.accept(
                new SquareNeighborhoodVisitor(direction, current)
            );

            if (found.isEmpty()) {
                return Optional.empty();
            }

            if (i == times) {
                return found;
            }

            current = found.get();
        }
        return Optional.empty();
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
            return name();
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
            return String.valueOf(value);
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

}
