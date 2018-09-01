# Puzzle15. Console Mode

To run game us *org.puzzle.app.GameApp* main class or *sbt run* command.
You can move "empty place" using commands:
- *up*
- *down*
- *left*
- *right*

To start a game you have to generate new Game Field using command *new <random_moves_count>*. 
New field is generated from the "ordered" field moving the "empty place" in random order. 

You can exit the game using command *quit*.
## Commands
- *new <random_moves_count>* - generate new field using random moves
- *up* - move "empty space" up
- *down* - move "empty space" down
- *left* - move "empty space" left
- *right* - "empty space" right 
- *quit* - quit the game

You can use short forms of command: *n, u, d, l, r, q*.

## Game Field
You can play using not inly 4X4 game field. You can use any square field (2x2, 3X3, 4x4).
To set dimension of a game field you can use *application.conf* file and parameter *game.dimension = 4*.
