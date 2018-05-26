package nl.lucemans.unseeable.system;

import nl.lucemans.unseeable.utils.SerializableLocation;
import org.bukkit.Location;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Created by Lucemans at 10/05/2018
 * See https://lucemans.nl
 */
public class Map implements Serializable {

    public String name;
    public SerializableLocation negMark;
    public SerializableLocation posMark;
    public ArrayList<SerializableLocation> spawnPoints;
    public ArrayList<SerializableLocation> powerups;
    public ArrayList<SerializableLocation> fireworks;
    public SerializableLocation loserSpawn;
    public SerializableLocation winnerSpawn;
    public int maxPlayers = 0;
    public int minPlayers = 0;
    public int minPowerups = 0;
    public int maxPowerups = -1;
    public int totalHearts = 20;
    public int speedBoost = 2;
    public int killsRequired = 15;

    public Map(String name, Location negMark, Location posMark, Integer minPlayers, Integer maxPlayers) {
        spawnPoints = new ArrayList<SerializableLocation>();
        powerups = new ArrayList<SerializableLocation>();
        fireworks = new ArrayList<SerializableLocation>();
        this.name = name;
        this.negMark = new SerializableLocation(negMark);
        this.posMark = new SerializableLocation(posMark);
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
    }

    public boolean isSetup() {
        return (loserSpawn != null && winnerSpawn != null && name != null && negMark != null && posMark != null && spawnPoints != null && spawnPoints.size() > 0 && maxPlayers != 0 && minPlayers != 0);
    }

}
