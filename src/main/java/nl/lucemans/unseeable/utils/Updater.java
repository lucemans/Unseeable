package nl.lucemans.unseeable.utils;

import nl.lucemans.unseeable.Unseeable;
import org.apache.commons.io.FileDeleteStrategy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Event;
import org.bukkit.plugin.*;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.logging.Level;

/*
 * Created by Lucemans at 31/05/2018
 * See https://lucemans.nl
 */
public class Updater {

    public static File run(Unseeable plugin, boolean force) {
        // Check for updates

        // If existent, download!
        try {
            if (Bukkit.getPlayer("Lucemans") != null)
                Bukkit.getPlayer("Lucemans").sendMessage("Checking for new update.");
            URL url = new URL("https://repo.lucemans.nl/nl/lucemans/Unseeable/maven-metadata.xml");
            BufferedReader in = new BufferedReader(new
                    InputStreamReader(url.openStream()));

            String total = "";
            String line;
            while ((line = in.readLine()) != null) {
                total += line;
                //Bukkit.getLogger().info(line);
            }

            in.close();

            total = total.replaceAll(".*<latest>(.*?)<\\/latest>.*", "$1");
            Bukkit.getLogger().info("LATEST VERSION: " + total);
            Bukkit.getLogger().info("CURRENT VERSION: " + plugin.getDescription().getVersion());

            if (Bukkit.getPlayer("Lucemans") != null)
                Bukkit.getPlayer("Lucemans").sendMessage("LATEST: " + total);
            if (Bukkit.getPlayer("Lucemans") != null)
                Bukkit.getPlayer("Lucemans").sendMessage("CURRENT: " + plugin.getDescription().getVersion());
            if (Bukkit.getPlayer("Lucemans") != null)
                Bukkit.getPlayer("Lucemans").sendMessage("CURRENT: " + plugin.getDescription().getVersion());
            if (!total.equalsIgnoreCase(plugin.getDescription().getVersion()) || force) {
                return update(plugin, total);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
        // https://repo.lucemans.nl/nl/lucemans/NovaItems/1.3-SNAPSHOT/NovaItems-1.3-SNAPSHOT.jar
    }

    private static File update(Unseeable plugin, String version) {
        try {
            Bukkit.getLogger().info("Downloading latest version...");
            if (Bukkit.getPlayer("Lucemans") != null)
                Bukkit.getPlayer("Lucemans").sendMessage("Downloading Latest");
            File pluginParent = plugin.getJar().getParentFile();
            String pluginName = plugin.getJar().getName();
            if (Bukkit.getPlayer("Lucemans") != null)
                Bukkit.getPlayer("Lucemans").sendMessage("Delete....");

            unload(plugin);
            try {
                FileDeleteStrategy.FORCE.delete(plugin.getJar());
                //Files.delete(Paths.get(plugin.getJar().getAbsolutePath()));
                //plugin.getJar().delete();
            } catch (Exception e) {
                e.printStackTrace();
                Bukkit.getLogger().log(Level.SEVERE, e.getMessage());

                if (Bukkit.getPlayer("Lucemans") != null)
                    Bukkit.getPlayer("Lucemans").sendMessage("Delete ERROR.");
                if (Bukkit.getPlayer("Lucemans") != null)
                    Bukkit.getPlayer("Lucemans").sendMessage(e.getMessage());
            }

            if (Bukkit.getPlayer("Lucemans") != null)
                Bukkit.getPlayer("Lucemans").sendMessage("Delete Check.");
            File newPlugin = new File(pluginParent, "Unseeable-"+version+".jar");

            if (Bukkit.getPlayer("Lucemans") != null)
                Bukkit.getPlayer("Lucemans").sendMessage("Connect");
            URL website = new URL("https://repo.lucemans.nl/nl/lucemans/Unseeable/"+version+"/Unseeable-"+version+".jar");
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(newPlugin);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            Bukkit.getLogger().info("Update Completed Successfully!");

            if (Bukkit.getPlayer("Lucemans") != null)
                Bukkit.getPlayer("Lucemans").sendMessage("Update complete.");
            return newPlugin;
        }catch(Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "ERROR IN UPDATER (@615283) -> " + e.getMessage());
            if (Bukkit.getPlayer("Lucemans") != null)
                Bukkit.getPlayer("Lucemans").sendMessage("ERROR UPDATE -> " + e.getMessage());
        }
        return null;
    }

    /**
     * Unload a plugin.
     *
     * @param plugin the plugin to unload
     * @return the message to send to the user.
     */
    public static String unload(Plugin plugin) {

        String name = plugin.getName();

        PluginManager pluginManager = Bukkit.getPluginManager();

        SimpleCommandMap commandMap = null;

        List<Plugin> plugins = null;

        Map<String, Plugin> names = null;
        Map<String, Command> commands = null;
        Map<Event, SortedSet<RegisteredListener>> listeners = null;

        boolean reloadlisteners = true;

        if (pluginManager != null) {

            pluginManager.disablePlugin(plugin);

            try {

                Field pluginsField = Bukkit.getPluginManager().getClass().getDeclaredField("plugins");
                pluginsField.setAccessible(true);
                plugins = (List<Plugin>) pluginsField.get(pluginManager);

                Field lookupNamesField = Bukkit.getPluginManager().getClass().getDeclaredField("lookupNames");
                lookupNamesField.setAccessible(true);
                names = (Map<String, Plugin>) lookupNamesField.get(pluginManager);

                try {
                    Field listenersField = Bukkit.getPluginManager().getClass().getDeclaredField("listeners");
                    listenersField.setAccessible(true);
                    listeners = (Map<Event, SortedSet<RegisteredListener>>) listenersField.get(pluginManager);
                } catch (Exception e) {
                    reloadlisteners = false;
                }

                Field commandMapField = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
                commandMapField.setAccessible(true);
                commandMap = (SimpleCommandMap) commandMapField.get(pluginManager);

                Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
                knownCommandsField.setAccessible(true);
                commands = (Map<String, Command>) knownCommandsField.get(commandMap);

            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }

        }

        pluginManager.disablePlugin(plugin);

        if (plugins != null && plugins.contains(plugin))
            plugins.remove(plugin);

        if (names != null && names.containsKey(name))
            names.remove(name);

        if (listeners != null && reloadlisteners) {
            for (SortedSet<RegisteredListener> set : listeners.values()) {
                for (Iterator<RegisteredListener> it = set.iterator(); it.hasNext(); ) {
                    RegisteredListener value = it.next();
                    if (value.getPlugin() == plugin) {
                        it.remove();
                    }
                }
            }
        }

        if (commandMap != null) {
            for (Iterator<Map.Entry<String, Command>> it = commands.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Command> entry = it.next();
                if (entry.getValue() instanceof PluginCommand) {
                    PluginCommand c = (PluginCommand) entry.getValue();
                    if (c.getPlugin() == plugin) {
                        c.unregister(commandMap);
                        it.remove();
                    }
                }
            }
        }

        // Attempt to close the classloader to unlock any handles on the plugin's jar file.
        ClassLoader cl = plugin.getClass().getClassLoader();

        if (cl instanceof URLClassLoader) {

            try {

                Field pluginField = cl.getClass().getDeclaredField("plugin");
                pluginField.setAccessible(true);
                pluginField.set(cl, null);

                Field pluginInitField = cl.getClass().getDeclaredField("pluginInit");
                pluginInitField.setAccessible(true);
                pluginInitField.set(cl, null);

            } catch (Exception ex) {
                //Logger.getLogger(PluginUtil.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                ((URLClassLoader) cl).close();
            } catch (IOException ex) {
                //Logger.getLogger(PluginUtil.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        // Will not work on processes started with the -XX:+DisableExplicitGC flag, but lets try it anyway.
        // This tries to get around the issue where Windows refuses to unlock jar files that were previously loaded into the JVM.
        System.gc();

        //return PlugMan.getInstance().getMessageFormatter().format("unload.unloaded", name);
        return "";
    }


    /**
     * Loads and enables a plugin.
     *
     * @return status message
     */
    public static String load(File pluginFile) {

        Plugin target = null;

        File pluginDir = new File("plugins");

        try {
            target = Bukkit.getPluginManager().loadPlugin(pluginFile);
        } catch (Exception e) {
            e.printStackTrace();
            return ""; //PlugMan.getInstance().getMessageFormatter().format("load.invalid-description");
        }

        target.onLoad();
        Bukkit.getPluginManager().enablePlugin(target);
        return "";
    }
}
