package com.claasm;

/**
 * Created by claasmeiners on 17/07/17.
 * This is the class for the empty cell, all of which have to be exposed for the player to win.
 */
public class EmptyCell extends Cell {
    public static final String CHARACTER_EXPOSED = "0";
    private boolean exposed;

    /**
     * @return whether the cell has already been exposed
     */
    public boolean isExposed() {
        return exposed;
    }

    /**
     * @param exposed whether the cell is to be exposed or "un-exposed" (covered)
     */
    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }
}
