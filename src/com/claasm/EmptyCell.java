package com.claasm;

/**
 * Created by claasmeiners on 17/07/17.
 */
public class EmptyCell extends Cell {
    public static final String CHARACTER_EXPOSED = "0";
    private boolean exposed;

    public boolean isExposed() {
        return exposed;
    }

    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }
}
