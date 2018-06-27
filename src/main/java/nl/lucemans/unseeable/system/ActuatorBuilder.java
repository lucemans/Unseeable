package nl.lucemans.unseeable.system;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ActuatorBuilder {

    public String target = "";
    public String mode = "";
    public String effect = "";
    public Integer duration = 1;
    public Integer strength = 1;

    public String produce() {
        String res = target;
        res += ":" + mode;
        if (mode.equalsIgnoreCase("effect")) {
            res += ":" + effect;
            res += "=" + strength + "=" + duration;
            return res;
        }
        return res;
    }

    public boolean isSetup() {
        return !(target.equalsIgnoreCase("target") || mode.equalsIgnoreCase("") || effect.equalsIgnoreCase(""));
    }

    public static ActuatorBuilder analyze(String str) {
        String[] r = str.split(":");

        ActuatorBuilder ab = new ActuatorBuilder();

        ab.target = r[0];
        ab.mode = r[1];
        if (ab.mode.equalsIgnoreCase("effect")) {
            String[] eff = r[2].split("=");
            ab.effect = eff[0];
            ab.strength = Integer.parseInt(eff[1]);
            ab.duration = Integer.parseInt(eff[2]);
        }
        return ab;
    }

    public void trigger(Player p) {
        if (target.equalsIgnoreCase("self")) {
            if (mode.equalsIgnoreCase("effect")) {
                PotionEffectType type = PotionEffectType.getByName(effect.toUpperCase());
                PotionEffect eff = new PotionEffect(type, duration*20, strength-1, false, false);
                //TODO: MAYBE POTION PRIORITY
                p.removePotionEffect(type);
                p.addPotionEffect(eff);
            }
        }
    }
}
