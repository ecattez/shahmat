package dev.ecattez.shahmat.infra.controller;

import dev.ecattez.shahmat.domain.board.Board;
import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.piece.PieceType;
import dev.ecattez.shahmat.domain.board.piece.move.Movement;
import dev.ecattez.shahmat.domain.board.piece.move.MovingRules;
import dev.ecattez.shahmat.domain.board.square.Square;
import dev.ecattez.shahmat.domain.board.violation.RuleNotImplemented;
import dev.ecattez.shahmat.domain.board.violation.RulesViolation;
import dev.ecattez.shahmat.domain.command.InitBoard;
import dev.ecattez.shahmat.domain.command.Move;
import dev.ecattez.shahmat.domain.command.Promote;
import dev.ecattez.shahmat.domain.event.ChessEvent;
import dev.ecattez.shahmat.domain.game.BoardDecision;
import dev.ecattez.shahmat.domain.game.ChessGame;
import dev.ecattez.shahmat.domain.game.GameType;
import dev.ecattez.shahmat.infra.aggregate.ChessGameId;
import dev.ecattez.shahmat.infra.projection.HalBoard;
import dev.ecattez.shahmat.infra.projection.HalPiece;
import dev.ecattez.shahmat.infra.projection.HalSquare;
import dev.ecattez.shahmat.infra.publisher.ChessGamePubSub;
import dev.ecattez.shahmat.infra.store.EventStore;
import dev.ecattez.shahmat.infra.store.SequenceAlreadyExists;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("boards")
public class HalBoardResource {

    private final ChessGamePubSub pubSub;
    private final EventStore eventStore;

    public HalBoardResource(ChessGamePubSub pubSub, EventStore eventStore) {
        this.pubSub = pubSub;
        this.eventStore = eventStore;
    }

    private void dispatch(ChessGameId chessGameId, Long sequence, Supplier<List<ChessEvent>> supplier) {
        try {
            pubSub.dispatch(
                chessGameId,
                sequence,
                supplier.get()
            );
        } catch (RulesViolation e) {
            throw new RulesNotSatisfied(e);
        } catch (SequenceAlreadyExists e) {
            throw new BoardStateConflict();
        }
    }

    @PostMapping
    public ResponseEntity<Void> createBoard(@RequestParam(defaultValue = "CLASSICAL") GameType gameType) {
        ChessGameId chessGameId = ChessGameId.newInstance();

        dispatch(
            chessGameId,
            0L,
            () -> ChessGame.initalizeBoard(
                Collections.emptyList(),
                new InitBoard(
                    gameType
                )
            )
        );

        return ResponseEntity
            .created(
                getBoardLink(
                    IanaLinkRelations.SELF,
                    chessGameId.value
                ).toUri()
            )
            .build();
    }

    @GetMapping(path = "{boardId}", produces = "application/prs.hal-forms+json")
    public HalBoard getBoard(@PathVariable String boardId) {
        ChessGameId chessGameId = ChessGameId.fromString(boardId);
        List<ChessEvent> history = eventStore.history(chessGameId);

        if (history.isEmpty()) {
            throw new BoardNotFound(chessGameId);
        }

        Board board = BoardDecision.replay(history);
        return toHalBoard(chessGameId, board);
    }

    @GetMapping(path = "{boardId}/squares/{where}", produces = "application/prs.hal-forms+json")
    public HalSquare getSquare(@PathVariable String boardId, @PathVariable String where) {
        ChessGameId chessGameId = ChessGameId.fromString(boardId);
        List<ChessEvent> history = eventStore.history(chessGameId);

        if (history.isEmpty()) {
            throw new BoardNotFound(chessGameId);
        }

        Board board = BoardDecision.replay(history);
        Square location = new Square(where);

        HalSquare halSquare;
        if (board.isVacant(location)) {
            halSquare = toHalSquare(location);
        } else {
            halSquare = getOccupiedSquares(board)
                .stream()
                .filter(occupied -> occupied.equals(location))
                .map(occupied -> toHalSquare(board, occupied, board.getPiece(occupied)))
                .findFirst()
                .get();
        }

        return halSquare;
    }

    @PutMapping(path = "{boardId}/squares/{where}", produces = "application/prs.hal-forms+json")
    public HalBoard promote(@PathVariable String boardId, @PathVariable String where, @Valid  @RequestBody PromotionPayload payload) {
        ChessGameId chessGameId = ChessGameId.fromString(boardId);
        List<ChessEvent> history = eventStore.history(chessGameId);

        if (history.isEmpty()) {
            throw new BoardNotFound(chessGameId);
        }

        PieceType promotedTo = PieceType.valueOf(payload.getPromotedTo());
        Square location = new Square(where);

        dispatch(
            chessGameId,
            (long) history.size(),
            () -> ChessGame.promote(
                history,
                new Promote(
                    location,
                    promotedTo
                )
            )
        );

        return toHalBoard(
            chessGameId,
            BoardDecision.replay(eventStore.history(chessGameId))
        );
    }

    @PostMapping(path = "{boardId}/squares/{where}", produces = "application/prs.hal-forms+json")
    public HalBoard move(@PathVariable String boardId, @PathVariable String where, @Valid  @RequestBody MovePayload payload) {
        ChessGameId chessGameId = ChessGameId.fromString(boardId);
        List<ChessEvent> history = eventStore.history(chessGameId);

        if (history.isEmpty()) {
            throw new BoardNotFound(chessGameId);
        }

        PieceType pieceType = PieceType.valueOf(payload.getType());
        Square fromSquare = new Square(where);
        Square toSquare = new Square(payload.getTo());

        dispatch(
            chessGameId,
            (long) history.size(),
            () -> ChessGame.move(
                history,
                new Move(
                    pieceType,
                    fromSquare,
                    toSquare
                )
            )
        );

        return toHalBoard(
            chessGameId,
            BoardDecision.replay(eventStore.history(chessGameId))
        );
    }

    private HalBoard toHalBoard(ChessGameId chessGameId, Board board) {
        return HalBoard
            .builder()
            .id(
                chessGameId.value
            )
            .squares(
                Stream.of(
                    getOccupiedSquares(board)
                        .stream()
                        .map(this::toHalSquare)
                        .collect(Collectors.toList()),
                    getVacantSquares(board)
                        .stream()
                        .map(this::toHalSquare)
                        .collect(Collectors.toList())
                )
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList())
            )
            .build();
    }

    public List<Square> getOccupiedSquares(Board board) {
        List<Square> occupiedSquares = new LinkedList<>();
        Square.SquareIterator iterator = new Square.SquareIterator();
        iterator.forEachRemaining(occupiedSquares::add);
        occupiedSquares.removeIf(board::isVacant);
        return occupiedSquares;
    }

    private List<Square> getVacantSquares(Board board) {
        List<Square> vacantSquares = new LinkedList<>();
        Square.SquareIterator iterator = new Square.SquareIterator();
        iterator.forEachRemaining(vacantSquares::add);
        vacantSquares.removeIf(square -> !board.isVacant(square));
        return vacantSquares;
    }

    private HalSquare toHalSquare(Square vacant) {
        return new HalSquare(
            vacant.toString(),
            null
        );
    }

    private HalSquare toHalSquare(Board board, Square occupied, Piece piece) {
        List<String> availableMoves;
        try {
            availableMoves = piece.type().accept(MovingRules.getInstance())
                .getAvailableMovements(board, piece, occupied)
                .stream()
                .map(Movement::to)
                .map(Square::toString)
                .collect(Collectors.toList());
        } catch (RuleNotImplemented e) {
            availableMoves = new LinkedList<>();
        }

        return new HalSquare(
            occupied.toString(),
            new HalPiece(
                piece.type().toString(),
                piece.color().toString(),
                piece.toString(),
                availableMoves,
                board.isPromotingIn(occupied)
            )
        );
    }

    public static Link getBoardLink(LinkRelation rel, String boardId) {
        return linkTo(
            methodOn(HalBoardResource.class).getBoard(boardId)
        ).withRel(rel);
    }

    public static Link getSquareLink(LinkRelation rel, String boardId, String location) {
        return linkTo(
            methodOn(HalBoardResource.class).getSquare(boardId, location)
        ).withRel(rel);
    }

}
