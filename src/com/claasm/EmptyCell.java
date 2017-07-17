package com.claasm;

/**
 * Created by claasmeiners on 17/07/17.
 * This is the class for the empty cell, all of which have to be exposed for the player to win.
 */
class EmptyCell extends Cell {
    static final String CHARACTER_EXPOSED = "0";
    private boolean exposed;

    /**
     * @return whether the cell has already been exposed
     */
    boolean isExposed() {
        return exposed;
    }

    /**
     * Exposes an empty cell, meaning it shows as empty and not a mine to a user and potentially contains the number of mines in adjacent cells
     */
    void expose() {
        this.exposed = true;
    }
}
