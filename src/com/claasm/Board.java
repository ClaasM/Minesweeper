package com.claasm;

/**
 * Created by claasmeiners on 17/07/17.
 * This contains the main logic and rules of the game.
 */
public class Board {

    //The percentage of cells that will be populated with mines
    private static final float MINES_PERCENTAGE = 10.0f;
    //The coordinate system starts on the bottom left
    private Cell[][] cells;

    /**
     * Creates a playing board and initializes all the cells either as empty cells or mine cells, with the percentage of mine cells given by MINES_PERCENTAGE
     *
     * @param width  the width of the board
     * @param height the height of the board
     */
    Board(int width, int height) {
        this.cells = new Cell[width][height];
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < height; x++) {
                if (Math.random() < (MINES_PERCENTAGE / 100f)) {
                    cells[x][y] = new MineCell();
                } else {
                    cells[x][y] = new EmptyCell();
                }
            }
        }
    }

    /**
     * @return the width of the board
     */
    int getWidth() {
        return cells.length;
    }

    /**
     * @return the height of the board
     */
    int getHeight() {
        return cells.length > 0 ? cells[0].length : 0;
    }

    /**
     * Simulates the left-click of the user on a cell, opening the cell and all neighboring cells according to Minesweeper rules, or ending the game if there was a mine below.
     *
     * @param x the x-location of the cell
     * @param y the y-location of the cell
     * @return true if it was a mine (the game needs to stopped)
     */
    boolean click(int x, int y) {
        Cell cell = cells[x][y];

        if (cell.isFlagged()) {
            //If the cell is flagged, it is not exposed but the flag is removed.
            toggleFlag(x, y);
            return false;
        }

        if (cell.getClass() == EmptyCell.class) {
            //The cell is not a mine
            EmptyCell emptyCell = (EmptyCell) cell;
            if (!emptyCell.isExposed()) {
                //Expose the cell and all adjacent cells
                exposeFrom(x, y);
            }
            // else: the cell has already been exposed, nothing is done
            return false;
        } else {
            //The cell is a mine, the game is over
            return true;
        }
    }

    /**
     * Starts with the given cell and, if the cell has no adjacent mines, calls itself recursively with all adjacent cells, according to Minesweeper rules.
     * @param x the x coordinate of the cell
     * @param y the y coordinate of the cell
     */
    private void exposeFrom(int x, int y) {
        //Expose this cell and all adjacent cells that have no number
        EmptyCell emptyCell = (EmptyCell) cells[x][y]; //We just assume at this point that the cell is an Emptycell
        emptyCell.expose();


        if (countAdjacentMines(x, y) == 0) {

            //There are no adjacent mines -> Recurse on all adjacent empty cells that are not yet exposed
            forEachAdjacentCell(x, y, (adjacentX, adjacentY) -> {
                if (cells[adjacentX][adjacentY].getClass() == EmptyCell.class) {
                    EmptyCell adjacentEmptyCell = (EmptyCell) cells[adjacentX][adjacentY];
                    if (!adjacentEmptyCell.isExposed()) {
                        exposeFrom(adjacentX, adjacentY);
                    }
                }
            });
        }
    }

    /**
     * Checks each adjacent cell, excluding the x,y-cell, for being a mine and keeps count
     * @param x the x coordinate of the cell
     * @param y the y coordinate of the cell
     * @return the number of mines in the 3 to 8 adjacent cells
     */
    private int countAdjacentMines(int x, int y) {
        //Sadly we have to do a bit of trickery here
        final int[] count = {0};
        forEachAdjacentCell(x, y, (adjacentX, adjacentY) -> {
            if (cells[adjacentX][adjacentY].getClass() == MineCell.class) {
                count[0]++;
            }
        });
        return count[0];
    }


    /**
     * Simulates the right-click of the user on a cell, toggling the flag on the cell.
     *
     * @param x the x-location of the cell
     * @param y the y-location of the cell
     */
    void toggleFlag(int x, int y) {
        Cell cell = cells[x][y];
        cell.setFlagged(!cell.isFlagged());
    }

    /**
     * Used to check whether the game is won
     *
     * @return true if there are no more empty cells which are not exposed
     */
    boolean isCleared() {
        //For each row on the board..
        for (Cell[] row : cells) {
            //For each cell...
            for (Cell cell : row) {
                if (cell.getClass() == EmptyCell.class) {
                    EmptyCell emptyCell = (EmptyCell) cell;
                    if (!emptyCell.isExposed()) {
                        //If it's an empty cell and not yet exposed, the board is not cleared yet
                        return false;
                    }
                }
            }
        }
        //No covered cells -> game is won
        return true;
    }

    /**
     * Used to print the board to the console so the user can see the state of the game
     *
     * @return the board, represented as a human-readable character-grid
     */
    @Override
    public String toString() {
        StringBuilder boardStringBuilder = new StringBuilder();
        //For each row on the board, starting with the upper one
        for (int y = getHeight() - 1; y >= 0; y--) {
            //For each cell...
            for (int x = 0; x < getWidth(); x++) {
                Cell cell = cells[x][y];

                //Choose the appropriate character
                if (cell.isFlagged()) {
                    //It's a flagged cell
                    boardStringBuilder.append(Cell.CHARACTER_FLAGGED);
                } else {
                    if (cell.getClass() == EmptyCell.class && ((EmptyCell) cell).isExposed()) {
                        //It is an exposed cell, potentially with adjacent mines
                        int adjacentMines = countAdjacentMines(x, y);
                        boardStringBuilder.append(adjacentMines == 0 ? EmptyCell.CHARACTER_EXPOSED : adjacentMines);
                    } else {
                        //It is just a regular old cell
                        boardStringBuilder.append(EmptyCell.CHARACTER_COVERED);
                    }
                }
                //To keep it a bit tidier in environments with potentially non-monospace console fonts
                boardStringBuilder.append("\t");
            }
            boardStringBuilder.append("\n");
        }
        //One last newline so the next instruction is never accidentally printed behind the board
        boardStringBuilder.append("\n");
        return boardStringBuilder.toString();
    }

    //This helper enum is used to store and iterate through all adjacency relations of a cell
    //It is used in the forEach of adjacent cells
    //It defines what we understand as being "adjacent"
    @SuppressWarnings("unused")
    enum adjacency {

        TOP(0, 1),
        RIGHT(1, 0),
        BOTTOM(0, -1),
        LEFT(-1, 0),

        TOP_RIGHT(1, 1),
        BOTTOM_RIGHT(1, -1),
        BOTTOM_LEFT(-1, -1),
        TOP_LEFT(-1, 1);

        int xDiff;
        int yDiff;

        adjacency(int xDiff, int yDiff) {
            this.xDiff = xDiff;
            this.yDiff = yDiff;
        }
    }

    //This interface is used to be able to perform forEach with lamba expressions with 2 parameters
    @FunctionalInterface
    interface Function<X, Y> {
        void apply(X x, Y y);
    }

    /**
     * Applies a function that takes two int's as an arguments to all adjacent cells of the cell at x,y that are within bounds
     *
     * @param x        the x-location of the cell
     * @param y        the y-location of the cell
     * @param function can assume the cell is within bounds
     */
    private void forEachAdjacentCell(int x, int y, Function<Integer, Integer> function) {
        //For convenience & performance:
        int width = getWidth();
        int height = getHeight();

        //For each adjacency relation
        for (adjacency a : adjacency.values()) {
            //Get the coordinates of the adjacent cell
            int adjacentX = x + a.xDiff;
            int adjacentY = y + a.yDiff;

            //Check if the cell is still within bounds
            if (adjacentX < height && adjacentX >= 0 && adjacentY < width && adjacentY >= 0) {
                //If so, apply the function
                function.apply(adjacentX, adjacentY);
            }
        }
    }
}
