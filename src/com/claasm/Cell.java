package com.claasm;

/**
 * Created by claasmeiners on 17/07/17.
 */
public abstract class Cell {
    private boolean flagged;

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }
}
