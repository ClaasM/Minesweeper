package com.claasm;

import java.util.Arrays;

/**
 * Created by claasmeiners on 17/07/17.
 */
public class Board {
    Cell[][] cells;

    public Board(int width, int height) {
        this.cells = new Cell[width][height];
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
        //TODO
        return false;
    }

    /**
     * Simulates the right-click of the user on a cell, toggling the flag on the cell.
     *
     * @param x the x-location of the cell
     * @param y the y-location of the cell
     * @return false if it was a bomb (the game needs to stopped)
     */
    public void toggleFlag(int x, int y) {
        //TODO
    }

    /**
     * Used to check whether the game is won
     * @return true if there are no more empty cells uncovered
     */
    public boolean isCleared() {
        //TODO
        return false;
    }

    /**
     * Used to print the board to the console so the user can see the state of the game
     * @return the board, represented as a human-readable character-grid
     */
    @Override
    public String toString() {
        //TODO
        return null;
    }
}
