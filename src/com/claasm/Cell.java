package com.claasm;

/**
 * Created by claasmeiners on 17/07/17.
 */
public abstract class Cell {
    private boolean flagged;
    public static final String CHARACTER_COVERED = " ";
    public static final String CHARACTER_FLAGGED = "X";


    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }
}
