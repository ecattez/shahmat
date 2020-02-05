package dev.ecattez.shahmat.infra.controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import dev.ecattez.shahmat.domain.board.piece.PieceType;
import dev.ecattez.shahmat.domain.board.square.Square;
import dev.ecattez.shahmat.domain.board.piece.pawn.PawnPromotionAllowedPieces;
import dev.ecattez.shahmat.infra.projection.HalBoard;
import dev.ecattez.shahmat.infra.projection.HalPiece;
import dev.ecattez.shahmat.infra.projection.HalSquare;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;

import java.io.IOException;
import java.util.List;

// fixme: must be tested
public class HalBoardSerializer extends JsonSerializer<HalBoard> {

    @Override
    public void serialize(HalBoard board, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeStartObject();
        generator.writeStringField("turnOf", board.getTurnOf());
        generator.writeNumberField("number-of-living-black-pieces", board.getLivingBlackPieces());
        generator.writeNumberField("number-of-living-white-pieces", board.getLivingWhitePieces());

        generateEmbedded(generator, board);

        generateLinks(generator, List.of(
            HalBoardResource.getBoardLink(
                IanaLinkRelations.SELF,
                board.getId()
            )
        ));
        generator.writeEndObject();
    }

    private void generateEmbedded(JsonGenerator generator, HalBoard board) throws IOException {
        generator.writeObjectFieldStart("_embedded");

        generator.writeArrayFieldStart("squares");
        for (HalSquare square: board.getSquares()) {
            generateSquare(generator, board, square);
        }
        generator.writeEndArray();

        generator.writeEndObject();
    }

    private void generateSquare(JsonGenerator generator, HalBoard board, HalSquare square) throws IOException {
        generator.writeStartObject();
        generator.writeStringField("location", square.getLocation());

        if (!square.isVacant()) {
            generator.writeBooleanField("vacant", false);

            HalPiece piece = square.getPiece();
            generatePiece(generator, piece);
            generatePieceTemplates(generator, piece);
        } else {
            generator.writeBooleanField("vacant", true);
        }

        generateLinks(generator, List.of(
            HalBoardResource.getSquareLink(IanaLinkRelations.SELF, board.getId(), square.getLocation()),
            HalBoardResource.getBoardLink(IanaLinkRelations.RELATED, board.getId())
        ));

        generator.writeEndObject();
    }

    private void generatePiece(JsonGenerator generator, HalPiece piece) throws IOException {
        generator.writeObjectFieldStart("piece");
        generator.writeStringField("type", piece.getType());
        generator.writeStringField("color", piece.getColor());
        generator.writeStringField("unicode", piece.getUnicode());
        generator.writeEndObject();
    }

    private void generatePieceTemplates(JsonGenerator generator, HalPiece piece) throws IOException {
        generator.writeObjectFieldStart("_templates");

        if (piece.isPromoting()) {
            generator.writeObjectFieldStart("promote");
            generator.writeStringField("method", "PUT");
            generator.writeArrayFieldStart("properties");
            generator.writeStartObject();
            generator.writeStringField("name", "promotedTo");
            generator.writeArrayFieldStart("suggest");

            for (PieceType type: PieceType.values()) {
                if (type.accept(PawnPromotionAllowedPieces.getInstance())) {
                    generator.writeStartObject();
                    generator.writeStringField("value", type.toString());
                    generator.writeEndObject();
                }
            }

            generator.writeEndArray();
            generator.writeEndObject();
            generator.writeEndArray();
            generator.writeEndObject();
        } else if (piece.canMove()) {
            generator.writeObjectFieldStart("move");
            generator.writeStringField("method", "POST");

            generator.writeArrayFieldStart("properties");

            generator.writeStartObject();
            generator.writeStringField("name", "type");
            generator.writeStringField("value", piece.getType());
            generator.writeEndObject();

            generator.writeStartObject();
            generator.writeStringField("name", "to");
            generator.writeStringField("regex", Square.SQUARE_PATTERN);
            generator.writeArrayFieldStart("suggest");
            for (String move: piece.getAvailableMoves()) {
                generator.writeStartObject();
                generator.writeStringField("value", move);
                generator.writeEndObject();
            }
            generator.writeEndArray();
            generator.writeEndObject();
            generator.writeEndArray();
            generator.writeEndObject();
        }

        generator.writeEndObject();
    }

    private void generateLinks(JsonGenerator generator, List<Link> links) throws IOException {
        generator.writeObjectFieldStart("_links");
        for (Link link: links) {
            generator.writeObjectField(link.getRel().value(), new JsonHref(link));
        }
        generator.writeEndObject();
    }

    private static final class JsonHref {

        private String href;

        public JsonHref(Link link) {
            this.href = link.getHref();
        }

        public String getHref() {
            return href;
        }
    }

}
