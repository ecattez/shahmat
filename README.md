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

## References

[http://www.chesscoachonline.com/chess-articles/chess-rules](http://www.chesscoachonline.com/chess-articles/chess-rules)
[https://www.wholesalechess.com/pages/new-to-chess/pieces.html](https://www.wholesalechess.com/pages/new-to-chess/pieces.html)
