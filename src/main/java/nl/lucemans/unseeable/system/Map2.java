package nl.lucemans.unseeable.system;

import nl.lucemans.unseeable.utils.SerializableLocation;
import org.bukkit.Location;

import java.io.Serializable;
import java.util.ArrayList;

public class Map2 extends Map implements Serializable {

    public ArrayList<String> unlockedPowerups = new ArrayList<>();

    public Map2(String name, Location negMark, Location posMark, Integer minPlayers, Integer maxPlayers) {
        super(name, negMark, posMark, minPlayers, maxPlayers);
    }
}