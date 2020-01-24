package dev.ecattez.shahmat.domain.board;

import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.square.Square;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class BoardStringFormatter {

    public String format(Board board) {
        Map<Square.Rank, List<String>> ranks = new LinkedHashMap<>();

        Iterator<Square> iterator = new Square.SquareIterator();
        Square current = null;

        List<String> line = new LinkedList<>();
        while (iterator.hasNext()) {
            Square next = iterator.next();
            if (current == null) {
                current = next;
            }
            if (!next.rank.equals(current.rank)) {
                ranks.put(current.rank, line);
                current = next;
                line = new LinkedList<>();
            }
            if (next.file.isLast() && next.rank.isLast()) {
                ranks.put(next.rank, line);
            }
            line.add(stringify(board, next));
        }

        StringBuilder sb = new StringBuilder();
        Square.Rank[] reverseOrderedRanks = Stream
            .of(Square.Rank.values())
            .sorted(Collections.reverseOrder())
            .toArray(Square.Rank[]::new);

        for (Square.Rank rank : reverseOrderedRanks) {
            sb.append(String.format("%d| %s\n", rank.value, String.join(" ", ranks.get(rank))));
        }
        sb.append(" | ");
        for (Square.File file : Square.File.values()) {
            sb.append(file.toString()).append(" ");
        }
        sb.append("\n");
        return sb.toString();
    }

    private String stringify(Board board, Square square) {
        return board.findPiece(square)
            .map(Piece::unicode)
            .orElse(".");
    }

}
