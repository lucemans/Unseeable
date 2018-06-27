package nl.lucemans.unseeable.gui;

import nl.lucemans.NovaItems.NItem;
import nl.lucemans.ninventory.NInventory;
import nl.lucemans.unseeable.ConfigSettings;
import nl.lucemans.unseeable.Unseeable;
import nl.lucemans.unseeable.powerups.PowerupTemplate;
import nl.lucemans.unseeable.system.Map;
import nl.lucemans.unseeable.system.Map2;
import nl.lucemans.unseeable.utils.SerializableLocation;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class MapEditor {

    /*  City    |   Town    |   Castle  */
    public static void openMaps(final Player p) {
        NInventory ninv = new NInventory("Maps", (int) Math.ceil((float) (Unseeable.maps.size() + 2) / 9.0) * 9, Unseeable.instance);
        Integer i = 0;
        for (final Map m : Unseeable.maps) {
            ninv.setItem(NItem.create(Material.WOOL).setName("&r" + m.name).setDescription("&r", "&r" + m.maxPlayers + " players",  "&rMinimal of " + m.minPlayers, "").make(), i);
            ninv.setLClick(i, new Runnable() {
                @Override
                public void run() {
                    MapEditor.openMap(p, m);
                }
            });
            i++;
        }
        ninv.setItem(NItem.create(Material.INK_SACK).setName("&a&lCreate &rnew").setDescription("&r", "&7Click to create a new", "&7map, this requires a", "&7worledit selection to be made of.", "&7the entire map.").make(), i);

        ninv.setItem(NItem.create(Material.BARRIER).setName("&c&l<- Return").setDescription("&r", "&7Click to return to the previous menu.").make(), ninv.getInv().getSize() - 1);
        ninv.setLClick(ninv.getInv().getSize(), new Runnable() {
            @Override
            public void run() {
                EditorGUI.openEditor(p);
            }
        });

        p.openInventory(ninv.getInv());
    }

    public static void openMap(final Player p, final Map m) {
        NInventory ninv = new NInventory(ChatColor.translateAlternateColorCodes('&', m.name + " > Edit"), 18, Unseeable.instance);
        ArrayList<String> desc = new ArrayList<>();

        Integer i = 0;
        desc.add("");
        desc.add("&7This is the speed");
        desc.add("&7modifier players get.");
        desc.add("&71 = no modifier.");
        desc.add("");
        desc.add("&rModifier: " + m.speedBoost);
        desc.add("");
        desc.add("&rClick to edit");
        ninv.setItem(NItem.create(Material.FEATHER).setName("&bSpeed").setDescription(desc).make(), i);
        ninv.setLClick(0, new Runnable() {
            @Override
            public void run() {
                editSpeed(m, p);
            }
        });
        i++;
        desc.clear();
        desc.add("");
        desc.add("&7This is where fireworks");
        desc.add("&7will spawn when game finishes.");
        desc.add("");
        desc.add("&rAmount: " + (m.fireworks.size() > 0 ? m.fireworks.size() : "None"));
        desc.add("");
        desc.add("&rClick to edit");
        ninv.setItem(NItem.create(Material.FIREWORK).setName("&cFireworks").setAmount((m.fireworks.size() == 0 ? 1 : m.fireworks.size())).setDescription(desc).make(), i);
        final ArrayList<SerializableLocation> fireworkList = m.fireworks;
        ninv.setLClick(i, new Runnable() {
            @Override
            public void run() {
                openListEditor(p, m, fireworkList, Material.FIREWORK, "Fireworks", "firework");
            }
        });
        i++;
        desc.clear();
        desc.add("");
        desc.add("&7This is where players");
        desc.add("&7will be spawned.");
        desc.add("");
        desc.add("&rAmount: " + (m.spawnPoints.size() > 0 ? m.spawnPoints.size() : "None"));
        desc.add("");
        desc.add("&rClick to edit");
        ninv.setItem(NItem.create(Material.BED).setName("&ePlayer Spawns").setAmount((m.spawnPoints.size() == 0 ? 1 : m.spawnPoints.size())).setDescription(desc).make(), i);
        final ArrayList<SerializableLocation> spawnList = m.spawnPoints;
        ninv.setLClick(i, new Runnable() {
            @Override
            public void run() {
                openListEditor(p, m, spawnList, Material.BED, "Spawnpoint", "spawn");
            }
        });
        i++;
        desc.clear();
        desc.add("");
        desc.add("&7This is where powerups");
        desc.add("&7can spawn.");
        desc.add("");
        desc.add("&rAmount: " + (m.powerups.size() > 0 ? m.powerups.size() : "None"));
        desc.add("");
        desc.add("&rClick to edit");
        ninv.setItem(NItem.create(Material.BEACON).setName("&ePowerup Spawns").setAmount((m.powerups.size() == 0 ? 1 : m.powerups.size())).setDescription(desc).make(), i);
        final ArrayList<SerializableLocation> powerupList = m.powerups;
        ninv.setLClick(i, new Runnable() {
            @Override
            public void run() {
                openListEditor(p, m, powerupList, Material.BEACON, "Powerups", "powerup");
            }
        });
        i++;
        desc.clear();
        desc.add("");
        desc.add("&7This is where winner");
        desc.add("&7will be teleported.");
        desc.add("");
        desc.add("&rLocation: " + (m.winnerSpawn == null ? "Not Set" : Math.round(m.winnerSpawn.x*10.0)/10.0 + " " + Math.round(m.winnerSpawn.y*10.0)/10.0 + " " + Math.round(m.winnerSpawn.z*10.0)/10.0 + " " + m.winnerSpawn.world));
        desc.add("");
        desc.add("&rLeft Click to teleport");
        desc.add("&rRight click to set");
        ninv.setItem(NItem.create(Material.GOLD_BLOCK).setName("&6Win Teleport").setDescription(desc).make(), i);
        ninv.setLClick(i, new Runnable() {
            @Override
            public void run() {
                p.teleport(m.winnerSpawn.getLocation());
            }
        });
        ninv.setRClick(i, new Runnable() {
            @Override
            public void run() {
                p.performCommand("usa spawn " + m.name + " set win");
            }
        });
        i++;
        desc.clear();
        desc.add("");
        desc.add("&7This is where the losers");
        desc.add("&7will be teleported.");
        desc.add("");
        desc.add("&rLocation: " + (m.loserSpawn == null ? "Not Set" : Math.round(m.loserSpawn.x*10.0)/10.0 + " " + Math.round(m.loserSpawn.y*10.0)/10.0 + " " + Math.round(m.loserSpawn.z*10.0)/10.0 + " " + m.loserSpawn.world));
        desc.add("");
        desc.add("&rLeft Click to teleport");
        desc.add("&rRight click to set");
        ninv.setItem(NItem.create(Material.REDSTONE_BLOCK).setName("&cLose Teleport").setDescription(desc).make(), i);
        ninv.setLClick(i, new Runnable() {
            @Override
            public void run() {
                p.teleport(m.loserSpawn.getLocation());
            }
        });
        ninv.setRClick(i, new Runnable() {
            @Override
            public void run() {
                p.performCommand("usa spawn " + m.name + " set lose");
            }
        });
        i++;
        desc.clear();
        desc.add("");
        desc.add("&7This is where specators");
        desc.add("&7will be teleported.");
        desc.add("");
        desc.add("&rLocation: " + (m.spectatorSpawn == null ? "Not Set" : Math.round(m.spectatorSpawn.x*10.0)/10.0 + " " + Math.round(m.spectatorSpawn.y*10.0)/10.0 + " " + Math.round(m.spectatorSpawn.z*10.0)/10.0 + " " + m.spectatorSpawn.world));
        desc.add("");
        desc.add("&rLeft Click to teleport");
        desc.add("&rRight click to set");
        ninv.setItem(NItem.create(Material.LAPIS_BLOCK).setName("&9Spectator Teleport").setDescription(desc).make(), i);
        ninv.setLClick(i, new Runnable() {
            @Override
            public void run() {
                p.teleport(m.spectatorSpawn.getLocation());
            }
        });
        ninv.setRClick(i, new Runnable() {
            @Override
            public void run() {
                p.performCommand("usa spawn " + m.name + " set spectate");
            }
        });
        i++;
        desc.clear();
        desc.add("&r");
        desc.add("&7Manage what powerups can");
        desc.add("&7spawn in the map.");
        desc.add("");
        desc.add("&rLeft Click to edit");
        ninv.setItem(NItem.create(Material.STAINED_GLASS).setName("&rToggle Powerups").setDescription(desc).make(), i);
        ninv.setLClick(i, new Runnable() {
            @Override
            public void run() {
                openPowerupToggle(p, m);
            }
        });

        ninv.setItem(NItem.create(Material.BARRIER).setName("&c&l<- Return").setDescription("&r", "&7Click to return to the previous menu.").make(), ninv.getInv().getSize() - 1);
        ninv.setLClick(ninv.getInv().getSize(), new Runnable() {
            @Override
            public void run() {
                openMaps(p);
            }
        });

        p.openInventory(ninv.getInv());
    }

    public static void editSpeed(final Map m, final Player p) {
        NInventory ninv = new NInventory(m.name + " > Speed", 9, Unseeable.instance);

        ninv.setItem(NItem.create(Material.FEATHER).setName("&b&lSpeedBoost: &r" + m.speedBoost).setAmount(m.speedBoost == 0 ? 1 : Math.abs(m.speedBoost)).setDescription("&r", "&7This is the speedboost players", "&7are given ingame.", "&r", "&7Click to &a&lSAVE&7.").make(), 4);

        ninv.setItem(NItem.create(Material.STAINED_GLASS_PANE).setName("&a+1").setDurability((short) 5).setDescription("&r", "&7Click to &a&lINCREMENT &7players", "&7speedboost ingame.").make(), 8);
        ninv.setItem(NItem.create(Material.STAINED_GLASS_PANE).setName("&c-1").setDurability((short) 14).setDescription("&r", "&7Click to &c&lDECREMENT &7players", "&7speedboost ingame.").make(), 0);

        ninv.setLClick(8, new Runnable() {
            @Override
            public void run() {
                m.speedBoost += 1;
                editSpeed(m, p);
            }
        });
        ninv.setLClick(0, new Runnable() {
            @Override
            public void run() {
                m.speedBoost -= 1;
                editSpeed(m, p);
            }
        });

        ninv.setLClick(4, new Runnable() {
            @Override
            public void run() {
                openMap(p, m);
            }
        });

        p.openInventory(ninv.getInv());
    }

    public static void openListEditor(final Player p, final Map m, final ArrayList<SerializableLocation> list, final Material material, final String name, final String type) {
        NInventory ninv = new NInventory(name, (int) Math.ceil((list.size() + 2.0) / 9.0)*9, Unseeable.instance);

        Integer i = 0;
        for (final SerializableLocation loc : list) {
            ninv.setItem(NItem.create(material).setName("Location").setDescription("", "&7Location: " + Math.round(loc.x*10.0)/10.0 + " " + Math.round(loc.y*10.0)/10.0 + " " + Math.round(loc.z*10.0)/10.0 + " " + loc.world, "", "&7Left Click to &3&lTELEPORT&7.", "&7Shift Right Click to &c&lDELETE&7.").make(), i);

            ninv.setLClick(i, new Runnable() {
                @Override
                public void run() {
                    p.teleport(loc.getLocation());
                }
            });
            ninv.setShiftRClick(i, new Runnable() {
                @Override
                public void run() {
                    list.remove(loc);
                    openListEditor(p, m, list,  material, name, type);
                }
            });

            i++;
        }

        ninv.setItem(NItem.create(Material.INK_SACK).setName("Create new").setDescription("&r", "&7Click to plot new &6&lLocation&7.").make(), i);
        ninv.setLClick(i, new Runnable() {
            @Override
            public void run() {
                p.performCommand("usa spawn " + m.name + " add " + type);
                openListEditor(p, m, list, material, name, type);
            }
        });

        ninv.setItem(NItem.create(Material.BARRIER).setName("&c&l<- Return").setDescription("&r", "&7Click to return to the previous menu.").make(), ninv.getInv().getSize() - 1);
        ninv.setLClick(ninv.getInv().getSize(), new Runnable() {
            @Override
            public void run() {
                openMap(p, m);
            }
        });

        p.openInventory(ninv.getInv());
    }

    public static void openPowerupToggle(final Player p, final Map m) {
        NInventory ninv = new NInventory(m.name + " > Powerups", (int) Math.ceil((ConfigSettings.powerupTemplates.size() + 1) / 9.0)*9, Unseeable.instance);

        if (!(m instanceof Map2))
            return;

        Integer i = 0;
        for (final PowerupTemplate temp : ConfigSettings.powerupTemplates) {
            ninv.setItem(NItem.create(Material.STAINED_GLASS).make(), i);

            NItem item = NItem.create(Material.STAINED_GLASS).setName(temp.name);
            ItemStack te = item.make();
            if (((Map2) m).unlockedPowerups.contains(temp.name))
                te.setDurability((short) 5);
            else
                te.setDurability((short) 14);

            ninv.setItem(te, i);
            ninv.setLClick(i, new Runnable() {
                @Override
                public void run() {
                    if (((Map2) m).unlockedPowerups.contains(temp.name))
                        ((Map2) m).unlockedPowerups.remove(temp.name);
                    else
                        ((Map2) m).unlockedPowerups.add(temp.name);
                    openPowerupToggle(p, m);
                }
            });
            i++;
        }

        ninv.setItem(NItem.create(Material.BARRIER).setName("&c&l<- Return").setDescription("&r", "&7Click to return to the previous menu.").make(), ninv.getInv().getSize() - 1);
        ninv.setLClick(ninv.getInv().getSize()-1, new Runnable() {
            @Override
            public void run() {
                openMap(p, m);
            }
        });

        p.openInventory(ninv.getInv());
    }
}