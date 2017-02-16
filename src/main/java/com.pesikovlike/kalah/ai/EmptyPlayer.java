package com.pesikovlike.kalah.ai;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EmptyPlayer extends Player {
    private static final Logger LOGGER = Logger.getLogger("EmptyPlayer");
    public EmptyPlayer () {}
    public int getSowIndex(Board board) {
        LOGGER.log(Level.SEVERE, "sow index of empty player: ");
        return 0;
    }

}
