package com.pesikovlike.kalah.game;

import com.pesikovlike.kalah.user.UserService;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Igor on 07.11.2016.
 */

public class GameBidsService {

    private UserService userService;


    private Map<String, GameBid> gameBids = new HashMap<String, GameBid>();

    public int addGameBid(String login, GameBid gameBid) {
        if(!userService.userExist(gameBid.getFrendLogin())) {
            return 2; //друга не существует
        }
        gameBids.put(login, gameBid);

        return 0;
    }

    public GameBid getGameBid(String login) {
        return gameBids.get(login);
    }

    public Map<String, GameBid> getAllGameBids() {
        return gameBids;
    }

    public Map<String, GameBid> getAllGameBidsForUser(String login) {
        Map<String, GameBid> bids = new HashMap<String, GameBid>();
        for(Map.Entry<String, GameBid> bid : gameBids.entrySet()) {
            String key = bid.getKey();
            GameBid value = bid.getValue();
            if (value.getFrendLogin() == null || value.getFrendLogin().equals(login)) {
                bids.put(key, value);
            }
        }
        return bids;
    }

    public int deleteGameBid(String login) {
        gameBids.remove(login);

        return 0;
    }
}
