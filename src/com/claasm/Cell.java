package com.claasm;

/**
 * Created by claasmeiners on 17/07/17.
 * This Class is the superclass of the two types of cells that are in a Minesweeper game: mine cells and empty cells
 */
public abstract class Cell {
    private boolean flagged;
    public static final String CHARACTER_COVERED = "_";
    public static final String CHARACTER_FLAGGED = "X";

    /**
     * @return Whether the cell is flagged by the user
     */
    public boolean isFlagged() {
        return flagged;
    }

    /**
     * @param flagged Whether the cell is to be flagged or "un-flagged"
     */
    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }
}
