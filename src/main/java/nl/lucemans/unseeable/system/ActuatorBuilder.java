package nl.lucemans.unseeable.system;

import nl.lucemans.unseeable.Unseeable;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class ActuatorBuilder {

    public String target = "";
    public String mode = "";
    public String effect = "";
    public Integer duration = 1;
    public Integer strength = 1;
    public Integer priority = 2;

    public String produce() {
        String res = target;
        res += ":" + mode;
        if (mode.equalsIgnoreCase("effect")) {
            res += ":" + effect;
            res += "=" + strength + "=" + duration;
            return res;
        }
        if (mode.equalsIgnoreCase("visible")) {
            res += ":" + priority + "=" + duration;
        }
        if (mode.equalsIgnoreCase("invisible")) {
            res += ":" + priority + "=" + duration;
        }
        return res;
    }

    public boolean isSetup() {
        return !(target.equalsIgnoreCase("") || mode.equalsIgnoreCase("") || effect.equalsIgnoreCase(""));
    }

    public static ActuatorBuilder analyze(String str) {
        // all:visible=2=10
        // all, visible=2=10
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
        if (ab.mode.equalsIgnoreCase("visible")) {
            String[] eff = r[2].split("=");
            ab.priority = Integer.parseInt(eff[0]);
            ab.duration = Integer.parseInt(eff[1]);
        }
        if (ab.mode.equalsIgnoreCase("invisible")) {
            String[] eff = r[2].split("=");
            ab.priority = Integer.parseInt(eff[0]);
            ab.duration = Integer.parseInt(eff[1]);
        }
        return ab;
    }

    public void trigger(Player p) {
        ArrayList<Player> _targets = new ArrayList<Player>();

        // Targets
        if (target.equalsIgnoreCase("self")) {
            _targets.add(p);
        }
        if (target.equalsIgnoreCase("other")) {
            for (Player _p : Unseeable.instance.currentGame.players)
                if (!_p.getUniqueId().toString().equalsIgnoreCase(p.getUniqueId().toString()))
                    _targets.add(_p);
        }
        if (target.equalsIgnoreCase("all")) {
            for (Player _p : Unseeable.instance.currentGame.players)
                _targets.add(p);
        }

        // Effects
        if (mode.equalsIgnoreCase("effect")) {
            PotionEffectType type = PotionEffectType.getByName(effect.toUpperCase());
            PotionEffect eff = new PotionEffect(type, duration*20, strength-1, false, false);
            //TODO: MAYBE POTION PRIORITY
            for (Player _p : _targets) {
                _p.removePotionEffect(type);
                _p.addPotionEffect(eff);
            }
        }
        if (mode.equalsIgnoreCase("visible")) {
            for (Player _p : _targets) {
                Unseeable.instance.currentGame.removePriorityBuffer(_p, priority);
                Unseeable.instance.currentGame.buffs.add(new EffectBuffer(_p.getUniqueId(), 1, priority, (int) Math.ceil(duration*10)));
            }
        }
        if (mode.equalsIgnoreCase("invisible")) {
            for (Player _p : _targets) {
                Unseeable.instance.currentGame.removePriorityBuffer(_p, priority);
                Unseeable.instance.currentGame.buffs.add(new EffectBuffer(_p.getUniqueId(), 0, priority, (int) Math.ceil(duration*10)));
            }
        }
    }
}
