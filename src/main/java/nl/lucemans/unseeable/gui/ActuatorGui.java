package nl.lucemans.unseeable.gui;

import nl.lucemans.NovaItems.NBlockColor;
import nl.lucemans.NovaItems.NItem;
import nl.lucemans.ninventory.NInventory;
import nl.lucemans.unseeable.Unseeable;
import nl.lucemans.unseeable.powerups.PowerupTemplate;
import nl.lucemans.unseeable.system.ActuatorBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.TreeMap;

public class ActuatorGui {

    public static void editActuators(final Player p, final PowerupTemplate template) {
        NInventory ninv = new NInventory(template.name + " > Actuators", (int) Math.ceil((template.actuators.size() + 2) / 9.0) * 9, Unseeable.instance);

        int i = 0;
        for (final String str : template.actuators) {
            ninv.setItem(NItem.create(Material.BEACON).make(), i);

            NItem item = NItem.create(Material.BEDROCK).setName("&c&lERROR.").setDescription("", "&7This actuator was not recognized", "&7" + str);
            ActuatorBuilder ab = ActuatorBuilder.analyze(str);
            if (ab.mode.equalsIgnoreCase("effect")) {
                item.type = Material.POTION;
                PotionMeta meta = (PotionMeta) item.make().getItemMeta();
                meta.setColor(PotionEffectType.getByName(ab.effect.toUpperCase()).getColor());
                item.setName("&7[" + ab.target.toUpperCase() + "] &b" + word(ab.effect.replaceAll("_", " ")) + " " + getNumeric(ab.strength)).setDescription("&eDuration: &7" + formatTime(ab.duration), "", "&rShift Right Click to &c&lDELETE&r.");
            }
            if (ab.mode.equalsIgnoreCase("invisible")) {
                item.type = Material.GLASS;
                item.setName("&7[" + ab.target.toUpperCase() + "] &bVisible " + getNumeric(ab.priority)).setDescription("&eDuration: &7" + formatTime(ab.duration), "", "&rShift Right Click to &c&lDELETE&r.");
            }
            if (ab.mode.equalsIgnoreCase("visible")) {
                item.type = Material.STAINED_GLASS;
                item.setName("&7[" + ab.target.toUpperCase() + "] &bInvisible " + getNumeric(ab.priority)).setDescription("&eDuration: &7" + formatTime(ab.duration), "", "&rShift Right Click to &c&lDELETE&r.");
            }

            ninv.setItem(item.make(), i);
            ninv.setShiftRClick(i, new Runnable() {
                @Override
                public void run() {
                    template.actuators.remove(str);
                    editActuators(p, template);
                }
            });

            i++;
        }
        ninv.setItem(NItem.create(Material.INK_SACK).make(), i);
        ninv.setLClick(i, new Runnable() {
            @Override
            public void run() {
                //TODO: ON CREATE
                editActuator(p, template, new ActuatorBuilder(), "target");
            }
        });

        ninv.setItem(NItem.create(Material.BARRIER).setName("&c&l<- Return").setDescription("&r", "&7Click to return to the previous menu.").make(), ninv.getInv().getSize() - 1);
        ninv.setLClick(ninv.getInv().getSize() - 1, new Runnable() {
            @Override
            public void run() {
                PowerupEditor.editPowerup(p, template);
            }
        });

        p.openInventory(ninv.getInv());
    }

    public static void editActuator(final Player p, final PowerupTemplate template, final ActuatorBuilder action, String mode) {
        if (mode.equalsIgnoreCase("target")) {
            NInventory ninv = new NInventory(template.name + " > Actuator", 9, Unseeable.instance);

            ninv.setItem(NItem.create(Material.SKULL_ITEM).setDurability((short) 1).setName("Self").make(), 2);
            ninv.setItem(NItem.create(Material.SKULL_ITEM).setDurability((short) 2).setName("Other").make(), 4);
            ninv.setItem(NItem.create(Material.SKULL_ITEM).setDurability((short) 3).setName("All").make(), 6);

            ninv.setLClick(2, new Runnable() {
                @Override
                public void run() {
                    action.target = "self";
                    editActuator(p, template, action, "effect");
                }
            });
            ninv.setLClick(4, new Runnable() {
                @Override
                public void run() {
                    action.target = "other";
                    editActuator(p, template, action, "effect");
                }
            });
            ninv.setLClick(6, new Runnable() {
                @Override
                public void run() {
                    action.target = "all";
                    editActuator(p, template, action, "effect");
                }
            });

            p.openInventory(ninv.getInv());
        }
        if (mode.equalsIgnoreCase("effect")) {
            Integer count = 0;
            count += PotionEffectType.values().length;
            count += 2; // Invis & Vis
            //count += 1; // custom enchants
            NInventory ninv = new NInventory(template.name + " > Actuator > Effect", (int) Math.ceil(count / 9.0) * 9, Unseeable.instance);
            Integer i = 0;
            for (final PotionEffectType pot : PotionEffectType.values()) {
                if (pot == null)
                    continue;
                if (pot.getColor() == null)
                    continue;
                if (pot.getName() == null)
                    continue;
                NItem item = NItem.create(Material.POTION);
                ItemStack item2 = item.make();
                PotionMeta meta = (PotionMeta) item2.getItemMeta();
                meta.setColor(pot.getColor());
                meta.addCustomEffect(new PotionEffect(pot, 1, 1, false, false), true);

                item.setMeta(meta);
                ninv.setItem(item.setName("&r[POTION] &b" + pot.getName().toLowerCase()).make(), i);
                ninv.setLClick(i, new Runnable() {
                    @Override
                    public void run() {
                        action.mode = "effect";
                        action.effect = pot.getName().toLowerCase();
                        editActuator(p, template, action, "strength");
                    }
                });
                i++;
            }
            // Invis & Vis
            ninv.setItem(NItem.create(Material.STAINED_GLASS).setColor(NBlockColor.CLEAR).setName("Visibility").make(), i);
            ninv.setLClick(i, new Runnable() {
                @Override
                public void run() {
                    action.mode = "visible";
                    editActuator(p, template, action, "priority");
                }
            });
            i++;
            ninv.setItem(NItem.create(Material.GLASS).setName("Invisibility").make(), i);
            ninv.setLClick(i, new Runnable() {
                @Override
                public void run() {
                    action.mode = "invisible";
                    editActuator(p, template, action, "priority");
                }
            });

            p.openInventory(ninv.getInv());
        }
        if (mode.equalsIgnoreCase("priority")) {
            NInventory ninv = new NInventory(template.name + " > Actuator > Priority: " + getNumeric(action.strength), 9, Unseeable.instance);

            if (action.priority > 1)
            {
                ninv.setItem(NItem.create(Material.STAINED_GLASS_PANE).setName("&c&lDecrease &r&bPriority &rby 1").setDescription("&r", "&7Decrease the priority of this actuator by 1.", "", "&rClick to &c&lDECREASE&r.").setDurability((short) 14).make(), 1);
                ninv.setLClick(1, new Runnable() {
                    @Override
                    public void run() {
                        action.priority -= 1;
                        editActuator(p, template, action, "priority");
                    }
                });
            }

            ninv.setItem(NItem.create(Material.STAINED_GLASS_PANE).setName("&a&lIncrease &r&bPriority &rby 1").setDescription("&r", "&7Increase the priority of this actuator by 1.", "", "&rClick to &a&lINCREASE&r.").setDurability((short) 5).make(), 7);
            ninv.setLClick(7, new Runnable() {
                @Override
                public void run() {
                    action.priority += 1;
                    editActuator(p, template, action, "priority");
                }
            });

            ninv.setItem(NItem.create(Material.YELLOW_FLOWER).setName("&b&lPriority: " + getNumeric(action.priority)).setDescription("&r","&7This is the priority of the effect specified.", "", "&rClick to &a&lCONFIRM&r.")
                    .setAmount(action.strength).make(), 4);
            ninv.setLClick(4, new Runnable() {
                @Override
                public void run() {
                    editActuator(p, template, action, "duration");
                }
            });

            p.openInventory(ninv.getInv());
        }
        if (mode.equalsIgnoreCase("strength")) {
            NInventory ninv = new NInventory(template.name + " > Actuator > Strength: " + getNumeric(action.strength), 9, Unseeable.instance);

            if (action.strength > 1)
            {
                ninv.setItem(NItem.create(Material.STAINED_GLASS_PANE).setName("&c&lDecrease &r&cStrength &rby 1").setDescription("&r", "&7Decrease the strength of this actuator by 1.", "", "&rClick to &c&lDECREASE&r.").setDurability((short) 14).make(), 1);
                ninv.setLClick(1, new Runnable() {
                    @Override
                    public void run() {
                        action.strength -= 1;
                        editActuator(p, template, action, "strength");
                    }
                });
            }

            ninv.setItem(NItem.create(Material.STAINED_GLASS_PANE).setName("&a&lIncrease &r&cStrength &rby 1").setDescription("&r", "&7Increase the strength of this actuator by 1.", "", "&rClick to &a&lINCREASE&r.").setDurability((short) 5).make(), 7);
            ninv.setLClick(7, new Runnable() {
                @Override
                public void run() {
                    action.strength += 1;
                    editActuator(p, template, action, "strength");
                }
            });

            ninv.setItem(NItem.create(Material.GLOWSTONE_DUST).setName("&e&lStrength &rModifier: " + getNumeric(action.strength)).setDescription("&r","&7This is the strength of the effect specified.", "", "&rClick to &a&lCONFIRM&r.")
                    .setAmount(action.strength).make(), 4);
            ninv.setLClick(4, new Runnable() {
                @Override
                public void run() {
                    editActuator(p, template, action, "duration");
                }
            });

            p.openInventory(ninv.getInv());
        }
        if (mode.equalsIgnoreCase("duration")) {

            NInventory ninv = new NInventory(template.name + " > Actuator > Duration: " + formatTime(action.duration), 9, Unseeable.instance);

            if (action.duration > 60) {
                ninv.setItem(NItem.create(Material.STAINED_GLASS_PANE).setName("&c&lDecrease &r&eDuration &rby 60").setDescription("&r", "&7Decrease the duration of this actuator by 60.", "", "&rClick to &c&lDECREASE&r.").setAmount(60).setDurability((short) 14).make(), 0);
                ninv.setLClick(0, new Runnable() {
                    @Override
                    public void run() {
                        action.duration -= 60;
                        editActuator(p, template, action, "duration");
                    }
                });
            }

            if (action.duration > 10) {
                ninv.setItem(NItem.create(Material.STAINED_GLASS_PANE).setName("&c&lDecrease &r&eDuration &rby 10").setDescription("&r", "&7Decrease the duration of this actuator by 10.", "", "&rClick to &c&lDECREASE&r.").setAmount(10).setDurability((short) 14).make(), 1);
                ninv.setLClick(1, new Runnable() {
                    @Override
                    public void run() {
                        action.duration -= 10;
                        editActuator(p, template, action, "duration");
                    }
                });
            }

            if (action.duration > 1)
            {
                ninv.setItem(NItem.create(Material.STAINED_GLASS_PANE).setName("&c&lDecrease &r&eDuration &rby 1").setDescription("&r", "&7Decrease the duration of this actuator by 1.", "", "&rClick to &c&lDECREASE&r.").setDurability((short) 14).make(), 2);
                ninv.setLClick(2, new Runnable() {
                    @Override
                    public void run() {
                        action.duration -= 1;
                        editActuator(p, template, action, "duration");
                    }
                });
            }

            ninv.setItem(NItem.create(Material.STAINED_GLASS_PANE).setName("&a&lIncrease &r&eDuration &rby 1").setDescription("&r", "&7Increase the duration of this actuator by 1.", "", "&rClick to &a&lINCREASE&r.").setDurability((short) 5).make(), 6);
            ninv.setLClick(6, new Runnable()    {
                @Override
                public void run() {
                    action.duration += 1;
                    editActuator(p, template, action, "duration");
                }
            });
            ninv.setItem(NItem.create(Material.STAINED_GLASS_PANE).setName("&a&lIncrease &r&eDuration &rby 10").setDescription("&r", "&7Increase the duration of this actuator by 10.", "", "&rClick to &a&lINCREASE&r.").setAmount(10).setDurability((short) 5).make(), 7);
            ninv.setLClick(7, new Runnable()    {
                @Override
                public void run() {
                    action.duration += 10;
                    editActuator(p, template, action, "duration");
                }
            });
            ninv.setItem(NItem.create(Material.STAINED_GLASS_PANE).setName("&a&lIncrease &r&eDuration &rby 60").setDescription("&r", "&7Increase the duration of this actuator by 60.", "", "&rClick to &a&lINCREASE&r.").setAmount(60).setDurability((short) 5).make(), 8);
            ninv.setLClick(8, new Runnable()    {
                @Override
                public void run() {
                    action.duration += 60;
                    editActuator(p, template, action, "duration");
                }
            });

            ninv.setItem(NItem.create(Material.REDSTONE).setName("&e&lDuration &rModifier").setDescription("&r","&7This is the duration of the effect specified.", "", "&rClick to &a&lCONFIRM&r.").setAmount(action.duration > 64 ? 64 : action.duration).make(), 4);
            ninv.setLClick(4, new Runnable() {
                @Override
                public void run() {
                    template.actuators.add(action.produce());
                    editActuators(p, template);
                }
            });

            p.openInventory(ninv.getInv());
        }
    }

    private static final TreeMap<Integer, String> map = new TreeMap<Integer, String>();

    static {
        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");
    }

    private static String getNumeric(Integer number) {
        int l = map.floorKey(number);
        if (number == l) {
            return map.get(number);
        }
        return map.get(l) + getNumeric(number-l);
    }

    private static String formatTime(Integer seconds) {
        Integer minutes = (int) Math.floor(seconds / 60.0);
        seconds = seconds % 60;
        String sec = "" + seconds;
        String min = "" + minutes;
        if (sec.length() == 1)
            sec = "0" + sec;
        return min + ":" + sec;
    }

    private static String word(String sentence) {
        String[] words = sentence.toLowerCase().split(" ");
        StringBuilder res = new StringBuilder();
        for (String str : words) {
            res.append(" " + ("" + str.charAt(0)).toUpperCase() + str.substring(1,str.length()));
        }
        return res.toString();
    }
}
