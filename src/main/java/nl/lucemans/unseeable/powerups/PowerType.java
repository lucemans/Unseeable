package nl.lucemans.unseeable.powerups;

/*
 * Created by Lucemans at 26/05/2018
 * See https://lucemans.nl
 */
public enum PowerType {
    VISIBILITY_I ("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTU4NjlmZDYzNTZkYjA3M2JhZGFlNzZkMTQzNTVkZjM1NGI5NzZjOWExMWIwNjMxZWY3NDc4ZTgyNmRhNTE5MCJ9fX0=")
    ,
    HEALTH_I ("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYThkYTdlMjU1ZTA5YzhiMzc4ZWM4NmMwYjkyMmZhODY0YzRiMTlkMGU1ZTVkYTRkOGM3M2MyYjU2OWMyMjUwMiJ9fX0=")
    ,
    DAMAGE_I ("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTBkZmM4YTM1NjNiZjk5NmY1YzFiNzRiMGIwMTViMmNjZWIyZDA0Zjk0YmJjZGFmYjIyOTlkOGE1OTc5ZmFjMSJ9fX0=")
    ,
    HEALTH_II ("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmIxOTQ1MTc4MzI2MzhjZTMzMzFhNzM0Yzc5Yjg5NmY5NGRkZTlhYTNjMDdhMzc5NmVhMjVkYjdjNGM3NmRmOCJ9fX0=")
    ,
    MYSTERY ("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmU3OGNhNDdjYTk2NjdjN2U1ZDc5NjUwZGQzY2MzYTdlMjVlMGQyZGE2ZjIxOThjMmQ4OWUyNGIzYjM4OWQ5In19fQ==")
    ;


    PowerType(String str) {
        this.str = str;
    }

    private final String str;

    public String getBase() {
        return this.str;
    }
}
