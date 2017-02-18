package com.pesikovlike.kalah.game.bid.implementation;

import com.pesikovlike.kalah.game.bid.GameBid;
import com.pesikovlike.kalah.game.bid.GameBidFactory;
import com.pesikovlike.kalah.game.bid.GameBidService;
import com.pesikovlike.kalah.model.dao.UserDAO;
import com.pesikovlike.kalah.user.UserService;


import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.inject.Named;
import javax.websocket.Session;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Igor on 29.11.2016.
 */
@Singleton
public class GameBidServiceImpl implements GameBidService {
    private static final Logger LOGGER = Logger.getLogger("GameBidService");
    @EJB
    private UserService userService;

    @Inject
    @Named("userDAO")
    private UserDAO userDAO;

    private final Map<String, GameBid> gameBids = new HashMap<String, GameBid>();

    public int addBid(GameBid newBid) {
        gameBids.put(newBid.getCreatorLogin(), newBid);
        return 0;
    }

    public int deleteBid(String creatorLogin) {
        gameBids.remove(creatorLogin);
        LOGGER.log(Level.SEVERE, getBids());
        return 0;
    }

    public String getBids() {
        String res = "bids: ";
        res += gameBids.values().size();
        return res;
    }

    public GameBid getBid(String creatorLogin) {

        return gameBids.get(creatorLogin);
    }
 
    public Set<GameBid> getAllGameBidsForUser(String friendLogin) {
        Set<GameBid> bids = new HashSet<GameBid>();
        for(Map.Entry<String, GameBid> bid : gameBids.entrySet()) {
            GameBid value = bid.getValue();
            if (value.getFriendLogin().equals("") || value.getFriendLogin().equals(friendLogin)) {
                bids.add(value);
            }
        }
        return bids;
    }
}
