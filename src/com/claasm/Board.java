package com.claasm;

/**
 * Created by claasmeiners on 17/07/17.
 */
public class Board {

    //The percentage of cells that will be populated with bombs
    private static final float BOMBS_PERCENTAGE = 25.0f;
    //The coordinate system starts on the bottom left
    Cell[][] cells;


    public Board(int width, int height) {
        this.cells = new Cell[width][height];
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < height; x++) {
                if (Math.random() < (BOMBS_PERCENTAGE / 100f)) {
                    cells[x][y] = new MineCell();
                } else {
                    cells[x][y] = new EmptyCell();
                }
            }
        }
        //TODO populate
    }

    public int getWidth() {
        return cells.length;
    }

    public int getHeight() {
        return cells.length > 0 ? cells[0].length : 0;
    }

    /**
     * Simulates the left-click of the user on a cell, opening the cell and all neighboring cells according to Minesweeper rules, or ending the game if there was a bomb below.
     *
     * @param x the x-location of the cell
     * @param y the y-location of the cell
     * @return true if it was a bomb (the game needs to stopped)
     */
    public boolean click(int x, int y) {
        Cell cell = cells[x][y];

        if (cell.isFlagged()) {
            //If the cell is flagged, it is not uncovered but the flag is removed.
            toggleFlag(x, y);
            return false;
        }

        if (cell.getClass() == EmptyCell.class) {
            //The cell is not a bomb
            EmptyCell emptyCell = (EmptyCell) cell;
            if (!emptyCell.isUncovered()) {
                //Clear the cell and all adjacent cells
                exposeFrom(x, y);
            }
            // else: the cell has already been uncovered, nothing is done
            return false;
        } else {
            //The cell is a bomb, the game is over
            return true;
        }
    }


    private void exposeFrom(int x, int y) {
        //Expose this cell and all adjacent cells that have no number
        EmptyCell emptyCell = (EmptyCell) cells[x][y]; //We just assume at this point that the cell is an Emptycell
        emptyCell.setUncovered(true);


        if (countAdjacentBombs(x, y) == 0) {

            //There are no adjacent bombs -> Recurse on all adjacent empty cells that are not yet uncovered
            forEachAdjacentCell(x, y, (adjacentX, adjacentY) -> {
                if (cells[adjacentX][adjacentY].getClass() == EmptyCell.class) {
                    EmptyCell adjacentEmptyCell = (EmptyCell) cells[adjacentX][adjacentY];
                    if (!adjacentEmptyCell.isUncovered()) {
                        exposeFrom(adjacentX, adjacentY);
                    }
                }
            });
        }
    }

    private int countAdjacentBombs(int x, int y) {
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
     * @return false if it was a bomb (the game needs to stopped)
     */
    public void toggleFlag(int x, int y) {
        Cell cell = cells[x][y];
        cell.setFlagged(!cell.isFlagged());
    }

    /**
     * Used to check whether the game is won
     *
     * @return true if there are no more empty cells covered
     */
    public boolean isCleared() {
        //For each row on the board..
        for (Cell[] row : cells) {
            //For each cell...
            for (Cell cell : row) {
                if (cell.getClass() == EmptyCell.class) {
                    EmptyCell emptyCell = (EmptyCell) cell;
                    if (!emptyCell.isUncovered()) {
                        //If it's an empty cell and not yet uncovered, the field is not cleared yet
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
        for (int y = getHeight(); y >= 0; y--) {
            //For each cell...
            for (int x = 0; x < getWidth(); x++) {
                Cell cell = cells[x][y];

                //Choose the appropriate character
                if (cell.isFlagged()) {
                    //It's a flagged cell
                    boardStringBuilder.append(Cell.CHARACTER_FLAGGED);
                } else {
                    if (cell.getClass() == EmptyCell.class && ((EmptyCell) cell).isUncovered()) {
                        //It is an exposed cell, potentially with adjacent bombs
                        int adjacentBombs = countAdjacentBombs(x, y);
                        boardStringBuilder.append(adjacentBombs == 0 ? EmptyCell.CHARACTER_UNCOVERED : adjacentBombs);
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
        //One last newline so the next instruction is never accidentally printed behind the field
        boardStringBuilder.append("\n");
        return boardStringBuilder.toString();
    }

    //This helper enum is used to store and iterate through all adjacency relations of a cell
    //It is used in the forEach of adjacent cells
    @SuppressWarnings("unused")
    enum adjacency {

        TOP(0, 1),
        RIGHT(1, 0),
        BOTTOM(-1, 0),
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
