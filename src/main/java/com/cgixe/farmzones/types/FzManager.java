package com.cgixe.farmzones.types;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FzManager implements Serializable {
    private final List<FzPlayer> players;

    /***
     * Creates a new empty FzManager. This should never be the case unless the farms.ser file doesn't exist yet.
     */
    public FzManager() {
        players = new ArrayList<>(0);
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

    public String toJson() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(this);
    }
}
