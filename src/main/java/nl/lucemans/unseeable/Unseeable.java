package nl.lucemans.unseeable;

import nl.lucemans.unseeable.commands.AdminCommand;
import nl.lucemans.unseeable.commands.UnseeableCommand;
import nl.lucemans.unseeable.system.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;

/*
 * Created by Lucemans at 10/05/2018
 * See https://lucemans.nl
 */
public class Unseeable extends JavaPlugin implements Listener {

    public static Unseeable instance;
    public ArrayList<Map> maps = new ArrayList<Map>();
    public GameInstance currentGame = null;

    public static String NAME = "&b&lUn&6&lseeable";

    private File mapFile = new File(getDataFolder(), "Maps.data");

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;

        // Load maps
        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        if (mapFile.exists())
        {
            try {
                FileInputStream fis = new FileInputStream(mapFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                maps = (ArrayList<Map>)ois.readObject();
                ois.close();
                fis.close();
            } catch (Exception e) {
                getLogger().severe(parse("The Following error in " + NAME + " occurred whilst loading the maps."));
                e.printStackTrace();
            }
        }

        getCommand("unseeable").setExecutor(new UnseeableCommand());
        getCommand("unseeableadmin").setExecutor(new AdminCommand());

        Bukkit.getPluginManager().registerEvents(this, this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                if (currentGame != null)
                    currentGame.tick();
            }
        }, 1L, 1L);
    }

    @Override
    public void onDisable() {
        try{
            if(mapFile.exists())
                mapFile.delete();
            FileOutputStream fos = new FileOutputStream(mapFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(maps);
            oos.flush();
            fos.flush();
            oos.close();
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Map findMap(String name) {
        for (Map m : maps) {
            if (m.name.equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }

    public boolean hasWorkingMap() {
        for (Map m : maps)
            if (m.isSetup())
                return true;
        return false;
    }

    public static String parse(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        // check if user is ingame if so push it through.

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.WALL_SIGN || event.getClickedBlock().getType() == Material.SIGN_POST) {
                Sign s = (Sign) event.getClickedBlock().getState();
                if (s.getLine(0).equalsIgnoreCase(parse("&c["+NAME+"&c]"))) {
                    // it is one of our signs
                    event.getPlayer().sendMessage(s.getLine(1).replace(parse("&a&l"), ""));
                    Map m = findMap (s.getLine(1).replace(parse("&a&l"), ""));
                    if (m == null) {
                        event.getPlayer().sendMessage(parse("Could not find map."));
                        return;
                    }
                    else
                    {
                        if (m.isSetup()) {
                            if (Unseeable.instance.currentGame == null || Unseeable.instance.currentGame.state == GameInstance.GameState.STOPPED || Unseeable.instance.currentGame.m == m) {
                                // can start new game
                                event.getPlayer().performCommand("us join " + m.name);
                                return;
                            }
                            event.getPlayer().sendMessage(parse("A Game is already running."));
                        }
                        else {
                            event.getPlayer().sendMessage(parse("Map is not setup properly."));
                        }
                        return;
                    }
                    //event.getPlayer().sendMessage(parse("&cThis sign is setup incorrectly."));
                }
            }
        }
    }

    @EventHandler
    public void onSign(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[usa]")) {
            event.setLine(0, parse("&c["+NAME+"&c]"));

            if (event.getLine(1).equalsIgnoreCase("join")) {
                Map m = findMap(event.getLine(2));
                if (m != null) {
                    if (m.isSetup()) {
                        event.setLine(1, parse("&a&l" + m.name));
                        event.setLine(2, parse("("+m.minPlayers+"/"+m.maxPlayers+")"));
                        event.getPlayer().sendMessage(parse("&aSuccessfully created a join sign!"));
                        return;
                    }
                    else
                    {
                        event.getPlayer().sendMessage(parse("&cThis map is not setup properly."));
                    }
                }
                else
                {
                    event.getPlayer().sendMessage(parse("&cPlease specify a map on the third line."));
                }
            }
            else
            {
                event.getPlayer().sendMessage(parse("&cPlease specify an action on the second line (JOIN)"));
            }
            event.setLine(1, parse("&c&lERROR!"));
        }
    }
}
