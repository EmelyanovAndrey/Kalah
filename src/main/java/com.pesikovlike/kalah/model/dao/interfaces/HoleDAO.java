package com.pesikovlike.kalah.model.dao.interfaces;

import com.pesikovlike.kalah.model.entity.GameState;
import com.pesikovlike.kalah.model.entity.Hole;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Igor on 31.10.2016.
 */
public interface HoleDAO {
    public void insertHole(Hole hole);

    public void updateHole(Hole hole);

    public void deleteHole(Hole hole);

    public Hole getHoleById(long id);

    public List<Hole> getHoleByGameState(GameState gameState);
}
