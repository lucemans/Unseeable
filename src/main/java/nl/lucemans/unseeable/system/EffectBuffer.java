package nl.lucemans.unseeable.system;

import java.util.UUID;

public class EffectBuffer {

    public UUID player;
    public Integer effect;
    public Integer priority;
    public Integer ticksLeft = 20; // 2 seconds timer

    public EffectBuffer(UUID player, Integer effect, Integer priority) {
        this.player = player;
        this.effect = effect;
        this.priority = priority;
    }
    public EffectBuffer(UUID player, Integer effect, Integer priority, Integer length) {
        this.player = player;
        this.effect = effect;
        this.priority = priority;
        this.ticksLeft = length;
    }

}
