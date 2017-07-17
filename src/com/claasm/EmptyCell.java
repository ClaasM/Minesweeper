package com.claasm;

/**
 * Created by claasmeiners on 17/07/17.
 */
public class EmptyCell extends Cell {
    private boolean uncovered;

    public boolean isUncovered() {
        return uncovered;
    }

    public void setUncovered(boolean uncovered) {
        this.uncovered = uncovered;
    }
}
