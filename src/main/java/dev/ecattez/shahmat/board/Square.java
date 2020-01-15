package dev.ecattez.shahmat.board;

import dev.ecattez.shahmat.board.violation.InvalidFile;
import dev.ecattez.shahmat.board.violation.InvalidPosition;
import dev.ecattez.shahmat.board.violation.InvalidRank;
import dev.ecattez.shahmat.board.violation.InvalidSquare;
import dev.ecattez.shahmat.board.violation.OutsideFile;
import dev.ecattez.shahmat.board.violation.OutsideRank;

import java.util.Objects;
import java.util.Optional;

public class Square {

    private static final String FILE_PATTERN = "[aAbBcCdDeEfFgGhH]";
    private static final String RANK_PATTERN = "[1-8]";
    private static final String SQUARE_PATTERN = FILE_PATTERN + RANK_PATTERN;

    public final File file;
    public final Rank rank;

    public Square(String position) throws InvalidSquare {
        if (position == null || !position.matches(SQUARE_PATTERN)) {
            throw new InvalidSquare(position);
        }
        this.file = new File(position.substring(0, 1).toUpperCase());
        this.rank = new Rank(Integer.parseInt(position.substring(1, 2)));
    }

    public Square(File file, Rank rank) throws InvalidSquare {
        if (file.value == null || !file.value.matches(FILE_PATTERN)) {
            throw new InvalidFile(file.value);
        }
        if (!String.valueOf(rank.value).matches(RANK_PATTERN)) {
            throw new InvalidRank(rank.value);
        }
        this.file = file;
        this.rank = rank;
    }

    public Optional<Square> findNeighbour(Direction direction, Orientation orientation) {
        return orientation.accept(
            new SquareNeighborhoodVisitor(direction, this)
        );
    }

    public Square getNeighbour(Direction direction, Orientation orientation) throws InvalidPosition {
        return findNeighbour(direction, orientation)
            .orElseThrow(() -> new InvalidPosition("Neighbour does not exists"));
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
        return file.value + rank.value;
    }

    // Todo: enum ?
    public static final class File {

        public static final File FIRST = new File("A");
        public static final File LAST = new File("H");

        public final String value;

        public File(String value) throws InvalidFile {
            if (value == null || !value.matches(FILE_PATTERN)) {
                throw new InvalidFile(value);
            }
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            File file = (File) o;
            return Objects.equals(value, file.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return value;
        }

        public boolean isFirst() {
            return FIRST.equals(this);
        }

        public boolean isLast() {
            return LAST.equals(this);
        }

        public boolean hasPrevious() {
            return exists(String.valueOf((char) (value.charAt(0) - 1)));
        }

        public boolean hasNext() {
            return exists(String.valueOf((char) (value.charAt(0) + 1)));
        }

        public File previous() throws OutsideFile {
            return other(String.valueOf((char) (value.charAt(0) - 1)));
        }

        public File next() throws OutsideFile {
            return other(String.valueOf((char) (value.charAt(0) + 1)));
        }

        private static boolean exists(String file) throws OutsideFile {
            return Optional.of(file)
                .filter(value -> value.matches(FILE_PATTERN))
                .isPresent();
        }

        private static File other(String file) throws OutsideFile {
            return Optional.of(file)
                .filter(value -> value.matches(FILE_PATTERN))
                .map(File::new)
                .orElseThrow(() -> new OutsideFile(file));
        }
    }

    public static final class Rank {

        public static final Rank FIRST = new Rank(1);
        public static final Rank LAST = new Rank(8);

        public final int value;

        public Rank(int value) throws InvalidRank {
            if (!String.valueOf(value).matches(RANK_PATTERN)) {
                throw new InvalidRank(value);
            }
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Rank rank = (Rank) o;
            return value == rank.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public boolean isFirst() {
            return FIRST.equals(this);
        }

        public boolean isLast() {
            return LAST.equals(this);
        }

        public boolean hasPrevious() {
            return exists(value - 1);
        }

        public boolean hasNext() {
            return exists(value + 1);
        }

        public Rank previous() throws OutsideRank {
            return other(value - 1);
        }

        public Rank next() throws OutsideRank {
            return other(value + 1);
        }

        private static boolean exists(int rank) {
            return Optional.of(rank)
                .filter(value -> String.valueOf(value).matches(RANK_PATTERN))
                .isPresent();
        }

        private static Rank other(int rank) throws OutsideRank {
            return Optional.of(rank)
                .filter(value -> String.valueOf(value).matches(RANK_PATTERN))
                .map(Rank::new)
                .orElseThrow(() -> new OutsideRank(rank));
        }
    }

}
