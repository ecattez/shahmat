# Shahmat

A Chess implementation with the Domain Driven Design

## The Game

The ultimate aim in the chess game is delivering a checkmate – trapping your opponent´s king.
The term checkmate is an alteration of the Persian phrase "Shah Mat", meaning literally, "the King is ambushed".

Each side starts out with 16 pieces, consisting of 8 pawns, 2 rooks, 2 knights, 2 bishops, 1 queen and 1 king, all in the same color.

**White always goes first.**

## Pawn moves

Pawns are both simple and complex in their movements.

The pawn piece has the fewest options of any chess piece on the board in where it can move and it can only move forward until it reaches the other side of the board.
Here are a few things to know about how a pawn chess piece moves:

- It can only directly forward one square;
- It can move directly forward two squares on their first move only;
- It can move diagonally forward when capturing an opponent's chess piece;
- If there is another piece in front of it, the pawn is stuck, unless there is a piece ahead on the capturing diagonals;
- Once it reaches the other side of the chess board, the player may *promote* the pawn in for any other chess piece if they choose, except another king.

![Pawn moves](https://cdn11.bigcommerce.com/s-dlmdd/content/pawn-moves.jpg)

### En passant

*En passant* (French: [ɑ̃ paˈsɑ̃], lit. in passing) is a move in chess. It is a special pawn capture that can only occur immediately after a pawn makes a move of two squares from its starting square, and it could have been captured by an enemy pawn had it advanced only one square. The opponent captures the just-moved pawn "as it passes" through the first square. The result is the same as if the pawn had advanced only one square and the enemy pawn had captured it normally.

*En passant* is a unique privilege of pawns, other pieces cannot capture en passant. It is the only capture in chess in which the capturing piece does not replace the captured piece on its square.

![En passant](https://upload.wikimedia.org/wikipedia/commons/0/09/Ajedrez_animaci%C3%B3n_en_passant.gif)

### Promotion

Promotion in chess is a rule that requires a pawn that reaches its eighth rank to be replaced by the player's choice of a queen, knight, rook, or bishop of the same color.

The new piece replaces the pawn within the same move. The choice of new piece is not limited to pieces previously captured, thus promotion can result in a player owning, for example, two or more queens despite starting the game with one.

Pawn promotion, or the threat of it, often decides the result in an endgame. Since the queen is the most powerful piece, the vast majority of promotions are to a queen. Promotion to a queen is also called queening; promotion to any other piece is referred to as underpromotion.

## Rook

The rooks are the most simple-moving chess pieces on the board. Their movements are only straight, moving forward, backward or side to side. At any point in the game, the piece can move in any direction that is straight ahead, behind or to the side. Here are a few things to know about how the Rook chess piece moves:

- It can move forward, backward, left or right at any time;
- It can move anywhere from 1 to 7 squares in any direction, so long as it is not obstructed by any other piece;
- It can capture an opponent piece that obstruct its way.

![Rook moves](https://cdn11.bigcommerce.com/s-dlmdd/content/rook-moves2.jpg)

## Bishop

The bishop chess piece is stuck moving in diagonals. Each player starts out with two bishop pieces, each one residing on its own color of square. Between both pieces, you can cover the entire board, but one piece can only cover one half of the board, only the colors of squares it started the game on.

- It can move in any direction diagonally, so long as it is not obstructed by another piece.
- It cannot move past any piece that is obstructing its path.
- It can take any other piece on the board that is within its bounds of movement.

![Bishop moves](https://cdn11.bigcommerce.com/s-dlmdd/content/bishop-moves.jpg)

## References

[http://www.chesscoachonline.com/chess-articles/chess-rules](http://www.chesscoachonline.com/chess-articles/chess-rules)
[https://www.wholesalechess.com/pages/new-to-chess/pieces.html](https://www.wholesalechess.com/pages/new-to-chess/pieces.html)
