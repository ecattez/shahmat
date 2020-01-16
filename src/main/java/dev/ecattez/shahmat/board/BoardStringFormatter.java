package dev.ecattez.shahmat.board;

public class BoardStringFormatter {

    public String format(Board board) {
        Square.File file = Square.File.first();
        Square.Rank rank = Square.Rank.last();

        Square current = new Square(file, rank);
        StringBuilder builder = new StringBuilder(rank.toString())
            .append("|")
            .append(toString(board, current));

        while (current.rank.hasPrevious() || current.rank.isFirst()) { // 8 to 1
            while (current.file.hasNext()) { // A to H
                file = current.file.next();
                current = new Square(file, rank);
                builder.append(toString(board, current));
            }
            file = Square.File.first();

            builder.append("\n");
            if (rank.hasPrevious()) {
                rank = current.rank.previous();
                builder.append(rank.toString()).append("|");
            } else {
                break;
            }


            current = new Square(file, rank);
            builder.append(toString(board, current));
        }
        builder.append(" |");
        file = Square.File.first();
        do {
            builder
                .append(" ")
                .append(file.toString())
                .append(" ");

            if (file.hasNext()) {
                file = file.next();
            } else {
                break;
            }
        } while (file.hasNext() || file.isLast());

        return builder.toString();
    }

    private String toString(Board board, Square square) {
        return " " + board.findPiece(square)
            .map(Object::toString)
            .orElse(".") + " ";
    }

}
