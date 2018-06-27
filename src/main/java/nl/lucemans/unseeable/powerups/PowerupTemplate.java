package nl.lucemans.unseeable.powerups;

import nl.lucemans.unseeable.system.ActuatorBuilder;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class PowerupTemplate implements Serializable {

    public String name = "";
    public String description = "";
    public String base = "";

    public ArrayList<String> actuators = new ArrayList<>();

    public PowerupTemplate(String name, String desc) {
        this.name = name;
        this.description = desc;
        this.base = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTBkZmM4YTM1NjNiZjk5NmY1YzFiNzRiMGIwMTViMmNjZWIyZDA0Zjk0YmJjZGFmYjIyOTlkOGE1OTc5ZmFjMSJ9fX0=";
    }

    public void trigger(Player p) {
        for (String act : actuators) {
            ActuatorBuilder actbl = ActuatorBuilder.analyze(act);
            actbl.trigger(p);
        }
    }
}
