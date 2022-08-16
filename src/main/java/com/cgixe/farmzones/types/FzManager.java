package com.cgixe.farmzones.types;

import java.util.ArrayList;
import java.util.List;

public class FzManager {
    private final List<FzPlayer> players;

    public FzManager() {
        // initialize player list using db // for now, just init empty list
        players = new ArrayList<>(0); // TODO: read db with player list
    }

    public FzPlayer addPlayer(FzPlayer player) {
        players.add(player);
        return player;
    }

    public List<FzPlayer> getPlayers() {
        return players;
    }

    public FzPlayer getPlayer(String name) {
        for (FzPlayer p : players) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        // automatically add player to db if doesn't exist
        FzPlayer newPlayer = new FzPlayer(name);
        addPlayer(newPlayer);
        return newPlayer;
    }
}
