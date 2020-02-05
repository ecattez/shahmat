package dev.ecattez.shahmat.infra.projection;

import dev.ecattez.shahmat.infra.controller.HalBoardResource;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Objects;

@Relation(collectionRelation = "boards", itemRelation = "board")
public class BoardInfo extends RepresentationModel<BoardInfo> {

    private String id;
    private String white;
    private String black;
    private String turnOf;
    private boolean over;

    public BoardInfo(String id, String white, String black, String turnOf, boolean over) {
        this.id = id;
        this.white = white;
        this.black = black;
        this.turnOf = turnOf;
        this.over = over;
        this.add(HalBoardResource.getBoardLink(IanaLinkRelations.SELF, id));
    }

    public String getId() {
        return id;
    }

    public String getWhite() {
        return white;
    }

    public String getBlack() {
        return black;
    }

    public String getTurnOf() {
        return turnOf;
    }

    public boolean isOver() {
        return over;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BoardInfo boardInfo = (BoardInfo) o;
        return over == boardInfo.over &&
            Objects.equals(id, boardInfo.id) &&
            Objects.equals(white, boardInfo.white) &&
            Objects.equals(black, boardInfo.black) &&
            Objects.equals(turnOf, boardInfo.turnOf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, white, black, turnOf, over);
    }
}
