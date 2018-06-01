package nl.lucemans.unseeable.commands;

import nl.lucemans.unseeable.Unseeable;
import nl.lucemans.unseeable.system.Map;
import nl.lucemans.unseeable.utils.LanguageManager;
import nl.lucemans.unseeable.utils.SerializableLocation;
import org.bukkit.entity.Player;
import org.inventivetalent.particle.ParticleEffect;

/*
 * Created by Lucemans at 20/05/2018
 * See https://lucemans.nl
 */
public class SpawnCommand implements BaseCommand {

    // /usa spawn <name> clear <spawn/powerup/firework>
    // /usa spawn <name> add <spawn/powerup/firework>
    // /usa spawn <name> set <lose/win>
    public void execute(Player p, String[] args) {
        if (args.length < 4) {
            p.sendMessage(LanguageManager.get("lang.suggest", new String[]{"/usa spawn <name> <add/set/clear> <spawn/powerup/lose/win>"}));
            return;
        }

        String mapName = args[1]; // City or Stone
        String mod = args[2]; // add set or clear
        String set = args[3]; // spawn powerup lose or win

        Map m = Unseeable.instance.findMap(mapName);
        if (m == null) {
            p.sendMessage(LanguageManager.get("lang.mapnotfound", new String[]{mapName}));
            return;
        }

        if (mod.equalsIgnoreCase("add")) {
            if (set.equalsIgnoreCase("spawn")) {
                m.spawnPoints.add(new SerializableLocation(p.getLocation()));
                String size = "" + m.spawnPoints.size();
                p.sendMessage(LanguageManager.get("lang.spawnadd", new String[]{m.name, size}));
            } else
            if (set.equalsIgnoreCase("powerup")) {
                m.powerups.add(new SerializableLocation(p.getLocation()));
                String size = "" + m.powerups.size();
                p.sendMessage(LanguageManager.get("lang.spawnpowerup", new String[]{m.name, size}));
            } else
            if (set.equalsIgnoreCase("firework")) {
                m.fireworks.add(new SerializableLocation(p.getLocation()));
                String size = "" + m.fireworks.size();
                p.sendMessage(LanguageManager.get("lang.spawnfirework", new String[]{m.name, size}));
            } else {
                p.sendMessage(LanguageManager.get("lang.suggest", new String[]{"/usa spawn " + m.name + " add <spawn/powerup/firework>"}));
            }
        } else
        if (mod.equalsIgnoreCase("set")) {
            if (set.equalsIgnoreCase("lose")) {
                m.loserSpawn = new SerializableLocation(p.getLocation());
                p.sendMessage(LanguageManager.get("lang.spawnlose", new String[]{m.name}));
            } else
            if (set.equalsIgnoreCase("win")) {
                m.winnerSpawn = new SerializableLocation(p.getLocation());
                p.sendMessage(LanguageManager.get("lang.spawnwin", new String[]{m.name}));
            } else
            if (set.equalsIgnoreCase("spectate")) {
                m.spectatorSpawn = new SerializableLocation(p.getLocation());
                p.sendMessage(LanguageManager.get("lang.spawnspectate", new String[]{m.name}));
            } else {
                p.sendMessage(LanguageManager.get("lang.suggest", new String[]{"/usa spawn " + m.name + " add <lose/win>"}));
            }
        } else
        if (mod.equalsIgnoreCase("clear")) {
            if (set.equalsIgnoreCase("spawn")) {
                m.spawnPoints.clear();
                String size = "" + m.spawnPoints.size();
                p.sendMessage(LanguageManager.get("lang.clearspawn", new String[]{m.name}));
            } else
            if (set.equalsIgnoreCase("powerup")) {
                m.powerups.clear();
                String size = "" + m.powerups.size();
                p.sendMessage(LanguageManager.get("lang.clearpowerup", new String[]{m.name}));
            } else
            if (set.equalsIgnoreCase("firework")) {
                m.fireworks.clear();
                String size = "" + m.fireworks.size();
                p.sendMessage(LanguageManager.get("lang.clearfirework", new String[]{m.name}));
            } else {
                p.sendMessage(LanguageManager.get("lang.suggest", new String[]{"/usa spawn " + m.name + " clear <spawn/powerup/firework>"}));
            }
        } else {
            p.sendMessage(LanguageManager.get("lang.suggest", new String[]{"/usa spawn "+m.name+" <add/set/clear> <spawn/powerup/lose/win>"}));
        }
    }
}
