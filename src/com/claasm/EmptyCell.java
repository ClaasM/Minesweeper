package com.claasm;

/**
 * Created by claasmeiners on 17/07/17.
 */
public class EmptyCell extends Cell {
    public static final String CHARACTER_UNCOVERED = "_";
    private boolean uncovered;

    public boolean isUncovered() {
        return uncovered;
    }

    public void setUncovered(boolean uncovered) {
        this.uncovered = uncovered;
    }
}
