package nl.lucemans.unseeable.gui;

import nl.lucemans.NovaItems.NItem;
import nl.lucemans.ninventory.NInventory;
import nl.lucemans.unseeable.ConfigSettings;
import nl.lucemans.unseeable.Unseeable;
import nl.lucemans.unseeable.powerups.Actuator;
import nl.lucemans.unseeable.powerups.PowerupTemplate;
import nl.lucemans.unseeable.system.ChatCallback;
import nl.lucemans.unseeable.system.ChatResponse;
import nl.lucemans.unseeable.utils.PlayerHead;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class PowerupEditor {

    public static void editPowerups(final Player p) {
        NInventory ninv = new NInventory("Global Powerups", (int) Math.ceil((float) (ConfigSettings.powerupTemplates.size() + 2) / 9.0) * 9, Unseeable.instance);
        Integer i = 0;
        for (final PowerupTemplate template : ConfigSettings.powerupTemplates) {
            ninv.setItem(NItem.create(PlayerHead.getItemStack(template.base, 1)).setDurability((short) 3).setName("&r" + template.name).make(), i);
            ninv.setLClick(i, new Runnable() {
                @Override
                public void run() {
                    //open ze powerup Editor
                    editPowerup(p, template);
                }
            });
            i++;
        }
        ninv.setItem(NItem.create(Material.INK_SACK).setName("&a&lCreate &rnew").make(), i);
        ninv.setLClick(i, new Runnable() {
            @Override
            public void run() {
                // openn new powerup editor
                String name = "Name";
                String description = "Description";
                editPowerup(p, new PowerupTemplate(name, description));
            }
        });

        ninv.setItem(NItem.create(Material.BARRIER).setName("&c&l<- Return").setDescription("&r", "&7Click to return to the previous menu.").make(), ninv.getInv().getSize() - 1);
        ninv.setLClick(ninv.getInv().getSize() - 1, new Runnable() {
            @Override
            public void run() {
                EditorGUI.openEditor(p);
            }
        });

        p.openInventory(ninv.getInv());
    }

    /*  NAME    |   DESCRIPTION |   ICON    |   Actuators   |   Save    |   Abort       */
    public static void editPowerup(final Player p, final PowerupTemplate template) {
        NInventory ninv = new NInventory("Powerup Creator: " + template.name, 9, Unseeable.instance);

        ninv.setItem(NItem.create(Material.ANVIL).setName("&6&lName").setDescription("", "&7This is the name the user sees.", "", "&rCurrent: " + template.name, "", "&7Click to &3&lRENAME&7.").make(), 0);
        ninv.setItem(NItem.create(Material.ANVIL).setName("&3&lLore").setDescription("", "&7This is the description the user sees.", "", "&rCurrent: " + template.description, "", "&7Click to &3&lRELORE&7.").make(), 1);
        ninv.setItem(NItem.create(PlayerHead.getItemStack(template.base, 1)).setName("&e&lIcon").setDescription("", "&7Set the icon of the Powerup", "&7will show.", "", "&7Click to &3&lEDIT&7.").setDurability((short) 3).make(), 2);
        ninv.setItem(NItem.create(Material.REDSTONE).setName("&c&lActuators").setDescription("", "&7This is what happens when one pickups this powerup.", "", "&7Click to &3&lEDIT&7.").make(), 3);
        ninv.setItem(NItem.create(Material.EMERALD_BLOCK).setName("&a&lSave").make(), 7);
        ninv.setItem(NItem.create(Material.REDSTONE_BLOCK).setName("&c&l" + (!ConfigSettings.powerupTemplates.contains(template) ? "Abort" : "Delete")).make(), 8);

        ninv.setLClick(0, new Runnable() {
            @Override
            public void run() {
                new ChatResponse(p, (template.name.equalsIgnoreCase("name") ? "Please enter a name for your powerup." : "Please change the name of your powerup. Last name: \"" + template.name + "\""), new ChatCallback() {
                    @Override
                    public boolean callback(String str) {

                        for (PowerupTemplate temp : ConfigSettings.powerupTemplates)
                            if (temp.name.equalsIgnoreCase(str))
                                return false;
                        template.name = str;
                        editPowerup(p, template);
                        return true;
                    }
                });
            }
        });
        ninv.setLClick(1, new Runnable() {
            @Override
            public void run() {
                new ChatResponse(p, (template.description.equalsIgnoreCase("description") ? "Please enter a description for your powerup." : "Please change the description of your powerup. Last description: \"" + template.description + "\""), new ChatCallback() {
                    @Override
                    public boolean callback(String str) {

                        for (PowerupTemplate temp : ConfigSettings.powerupTemplates)
                            if (temp.description.equalsIgnoreCase(str))
                                return false;
                        template.description = str;
                        editPowerup(p, template);
                        return true;
                    }
                });
            }
        });
        ninv.setLClick(2, new Runnable() {
            @Override
            public void run() {
                editIcon(p, template);
            }
        });
        ninv.setLClick(3, new Runnable() {
            @Override
            public void run() {
                ActuatorGui.editActuators(p, template);
            }
        });
        ninv.setLClick(8, new Runnable() {
            @Override
            public void run() {
                ConfigSettings.powerupTemplates.remove(template);
                p.closeInventory();
                editPowerups(p);
            }
        });
        ninv.setLClick(7, new Runnable() {
            @Override
            public void run() {
                ConfigSettings.powerupTemplates.remove(template);
                ConfigSettings.powerupTemplates.add(template);
                p.closeInventory();
                editPowerups(p);
            }
        });
        p.openInventory(ninv.getInv());
    }

    /* Actuators + new */
    public static void editActuators(final Player p, final PowerupTemplate template) {
        ActuatorGui.editActuators(p, template);
    }

    public static void editIcon(final Player p, final PowerupTemplate template) {
        NInventory ninv = new NInventory("Choose an Icon", 9, Unseeable.instance);

        ninv.setItem(NItem.create(
                (PlayerHead.getSkull(p.getInventory().getItemInMainHand()).equalsIgnoreCase("") ? new ItemStack(Material.STONE) : PlayerHead.getItemStack(PlayerHead.getSkull(p.getInventory().getItemInMainHand()), 1))
        ).setDurability((short) (PlayerHead.getSkull(p.getInventory().getItemInMainHand()).equalsIgnoreCase("") ? 0 : 3)).setName("&rCustom").setDescription("&r", "&7Use the head that you are holding.", "", (PlayerHead.getSkull(p.getInventory().getItemInMainHand()).equals("") ? "&rPlease hold a skull." : "&rClick to &a&lSET&7.")).make(), 2);
        ninv.setItem(NItem.create(Material.BEDROCK).setName("Premade").setDescription("&r", "&7Choose from a premade list of", "&7icons. Might be good for", "&7some inspiration.", "", "&rClick to &e&lVIEW&7.").make(), 6);

        ninv.setLClick(2, new Runnable() {
            @Override
            public void run() {
                if (p.getInventory().getItemInMainHand() == null || p.getInventory().getItemInMainHand().getType() == Material.AIR || p.getInventory().getItemInMainHand().getType() != Material.SKULL_ITEM)
                {
                    p.sendMessage("Please hold a skull in your hand.");
                    return;
                }
                String res = PlayerHead.getSkull(p.getInventory().getItemInMainHand());
                if (res.equalsIgnoreCase("")) {
                    p.sendMessage("Sorry that did not seem to work.");
                    return;
                }
                template.base = res;
                p.sendMessage("Succesfully updated.");
                editPowerup(p, template);
            }
        });

        ninv.setLClick(6, new Runnable() {
            @Override
            public void run() {
                p.closeInventory();
                openSuggestor(p, template);
            }
        });

        ninv.setItem(NItem.create(Material.BARRIER).setName("&c&l<- Return").setDescription("&r", "&7Click to return to the previous menu.").make(), ninv.getInv().getSize() - 1);
        ninv.setLClick(ninv.getInv().getSize(), new Runnable() {
            @Override
            public void run() {
                editPowerup(p, template);
            }
        });

        p.openInventory(ninv.getInv());
    }

    public static void openSuggestor(final Player p, final PowerupTemplate template) {
        ArrayList<String> skins = new ArrayList<>(Arrays.asList(
"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDYwM2Q1Mzk4YzI1NzdjYzJiNzQ5MzM2YzgzNTVmNWJlNTJiNTQxNDNkZmUwOWRjYzk2YTRlNTgwM2U1OTIyNCJ9fX0=",
"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTQ5ZDIxMmJmYzBhMzRhNzA3NjNlMmE2OGRlNGZhOTI3MGNjZjJkODA3MWIxY2M4MzgxM2U0MTA2YjlkMWRmZSJ9fX0=",
"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWIxNWNlODIzNzcwZDlhMjY5YzFlYmY1ODNkM2U0OTMyNzQ3YTEzZWY0MzYxM2NkNGY3NWY4MDRjYTQifX19",
"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGVhM2RkYzkxN2Q2ZWJkOGFlZWM3ZmRmZGFmMzI2ZDhjNmZlYWUzZGY1NjRjZDI3MzdkZGNhNzQ1ZjVlN2IxIn19fQ==",
"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTIzMjI5ZjZlNjA2ZDkxYjdlNjdhMmJjZjNlMmEzMzMzYmE2MTNiNmQ2NDA5MTk5YjcxNjljMDYzODliMCJ9fX0=",
"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWIzYWMzZmZiOTVlY2IwYWM1N2NhYjQ5MzBhM2FiNzQ2MjExYjU5ODg2MWEzNTJkZjNlM2M2ZTljNjk5OGUifX19",
"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNlZjk1OTVjZDhjMTZkOTMwNjBmMmI4NTQyYTNjMzFmNGRhYTg5ZGEwYTQ2MjUzMjRjZDUyZWI3MDgzMmFlIn19fQ==",
"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWI3YzczYzk5YmY4NmYyYjFhMjkzMTVhYzU5YTJkYjhkYWUxNjMxMWMxNTEwNGExMWNkZTRkZGYzZjA3ZjllIn19fQ==",
"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzY1MmExZTgyMmQyNDdkZTliYzMyMmFiN2MxYjE0MmM4N2MxN2NjN2Y3MWM3YWU3YWIzZDk2YjkxMWU1NzIifX19",
"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliYzBkMjYwNTc1MmExMWRiMTRmYjZlNTY2M2IzOGJkNTU1YmMxZmUxNDEwZDJiMzY2YWM1ODcyYjg3OSJ9fX0=",
"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliYzBkMjYwNTc1MmExMWRiMTRmYjZlNTY2M2IzOGJkNTU1YmMxZmUxNDEwZDJiMzY2YWM1ODcyYjg3OSJ9fX0=",
"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDk5MmU0YThmNDNjNzdhYzJlMmQ3OTJkOTYwNGY2MWYzNDM4N2M4NzdhYmEzNzEyZTQwNDdiNzQwYzU3OCJ9fX0=",
"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTcxMTQwNjVhM2M5NWM1ZDIyNTE4OGFkN2JmZGFhOWI4YjA4NDVkZjRlMzZjMjRiNDUzNDdmZDc0NzBhNyJ9fX0=",
"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2I0ZTQ3OGMxMWNjZGZhNDUzMjk4NzQ3MjQ1ZTA0MjVlYzQyNTFlMTdmYjNlNjdkYmVjMTQxNzZjNjQ3MTcifX19",
"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmNmNWIxY2ZlZDFjMjdkZDRjM2JlZjZiOTg0NDk5NDczOTg1MWU0NmIzZmM3ZmRhMWNiYzI1YjgwYWIzYiJ9fX0=",
"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZiZDc1ZDNhZWYzOTk4NmJhOGU4ZDNmMzlhYmIxMDhlM2E0OGRlNTZlNTViNjQxYWNiZjNjYzRkNmQ3OTRiIn19fQ==",
"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjJlODdmODgzMzg2NmExNjcyZTFmMmFhODk1NjYxODBmYzkxZmZiZTRiYzg2Mzc5M2ViNWU2MTYyNjQ4NSJ9fX0=",
"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmFlZWNhMzYxNTczNGYyZGRlNjUyYzc1YzhmYjUyMGVmZjNiYTQwNWM2NTVhNWZmM2E4N2FiYzY4Yzk4MWIyIn19fQ==",
"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWE1ZWFiYjA5NTc3NTljZjYxODRhMjk0MWNmZGEwMWI5MWZkMjFmMGQxNTIxMzM1YmYxNDMyZDczYWViZWFiIn19fQ==",
"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDA5NTU4N2FhNTY1ODM3N2VmYmU1ZmY3ZGFmNTRmZWZmZTZkNWY2YmRhYmEzZGMxOWVlN2QyZjE4NjI2MjQ3ZCJ9fX0=",
"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmI4ODRkNjFmMjM1MjM1MDQ3NDgzYWM0YmE0Y2U1Mjg2OTFlNjQyNGJhYzEzODE0MTU5MjcyZDk2NzNhYyJ9fX0=",
"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjEzYTI3OGFlOTkwMmY3YzE1YmYzOTY5OWM2YTE0MjYxNDI1Y2NhZmVkYWIyNGZhNmE4NTljNDE1YTMwNWQ0YSJ9fX0=",
"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjVlZDNhZmJkNjk2NDk5ZGVkY2NmMmRmZDY2NWZkY2VmMDQyOWE4OTk0MjhiNmZkODczNWNiZGNiMjViYjgifX19",
"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTc4ZTZhODNmZmEzYTE2OWZjN2ExMmM2ZjZhYzkzMWZmMWIyMTJjMzYwYTU2Nzc0ZTRjOGQ0ZTI2OWI5NGU1YiJ9fX0="
        ));

        NInventory ninv = new NInventory("Pick an icon", (int) Math.ceil(skins.size() / 9.0) * 9, Unseeable.instance);

        Integer i = 0;
        for (final String skin : skins)
        {
            ninv.setItem(NItem.create(PlayerHead.getItemStack(skin, 1)).setName("&r#" + i).setDurability((short) 3).make(), i);
            ninv.setLClick(i, new Runnable() {
                @Override
                public void run() {
                    template.base = skin;
                    p.closeInventory();
                    editPowerup(p, template);
                }
            });
            i++;
        }

        p.openInventory(ninv.getInv());
    }
}
