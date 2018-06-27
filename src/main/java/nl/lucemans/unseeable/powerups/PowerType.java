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

/// eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDYwM2Q1Mzk4YzI1NzdjYzJiNzQ5MzM2YzgzNTVmNWJlNTJiNTQxNDNkZmUwOWRjYzk2YTRlNTgwM2U1OTIyNCJ9fX0=
/// eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTQ5ZDIxMmJmYzBhMzRhNzA3NjNlMmE2OGRlNGZhOTI3MGNjZjJkODA3MWIxY2M4MzgxM2U0MTA2YjlkMWRmZSJ9fX0=
/// eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWIxNWNlODIzNzcwZDlhMjY5YzFlYmY1ODNkM2U0OTMyNzQ3YTEzZWY0MzYxM2NkNGY3NWY4MDRjYTQifX19
/// eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGVhM2RkYzkxN2Q2ZWJkOGFlZWM3ZmRmZGFmMzI2ZDhjNmZlYWUzZGY1NjRjZDI3MzdkZGNhNzQ1ZjVlN2IxIn19fQ==
/// eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTIzMjI5ZjZlNjA2ZDkxYjdlNjdhMmJjZjNlMmEzMzMzYmE2MTNiNmQ2NDA5MTk5YjcxNjljMDYzODliMCJ9fX0=
/// eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWIzYWMzZmZiOTVlY2IwYWM1N2NhYjQ5MzBhM2FiNzQ2MjExYjU5ODg2MWEzNTJkZjNlM2M2ZTljNjk5OGUifX19
/// eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNlZjk1OTVjZDhjMTZkOTMwNjBmMmI4NTQyYTNjMzFmNGRhYTg5ZGEwYTQ2MjUzMjRjZDUyZWI3MDgzMmFlIn19fQ==
/// eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWI3YzczYzk5YmY4NmYyYjFhMjkzMTVhYzU5YTJkYjhkYWUxNjMxMWMxNTEwNGExMWNkZTRkZGYzZjA3ZjllIn19fQ==
/// eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzY1MmExZTgyMmQyNDdkZTliYzMyMmFiN2MxYjE0MmM4N2MxN2NjN2Y3MWM3YWU3YWIzZDk2YjkxMWU1NzIifX19
/// eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliYzBkMjYwNTc1MmExMWRiMTRmYjZlNTY2M2IzOGJkNTU1YmMxZmUxNDEwZDJiMzY2YWM1ODcyYjg3OSJ9fX0=
/// eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliYzBkMjYwNTc1MmExMWRiMTRmYjZlNTY2M2IzOGJkNTU1YmMxZmUxNDEwZDJiMzY2YWM1ODcyYjg3OSJ9fX0=
/// eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDk5MmU0YThmNDNjNzdhYzJlMmQ3OTJkOTYwNGY2MWYzNDM4N2M4NzdhYmEzNzEyZTQwNDdiNzQwYzU3OCJ9fX0=
/// eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTcxMTQwNjVhM2M5NWM1ZDIyNTE4OGFkN2JmZGFhOWI4YjA4NDVkZjRlMzZjMjRiNDUzNDdmZDc0NzBhNyJ9fX0=
/// eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2I0ZTQ3OGMxMWNjZGZhNDUzMjk4NzQ3MjQ1ZTA0MjVlYzQyNTFlMTdmYjNlNjdkYmVjMTQxNzZjNjQ3MTcifX19
/// eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmNmNWIxY2ZlZDFjMjdkZDRjM2JlZjZiOTg0NDk5NDczOTg1MWU0NmIzZmM3ZmRhMWNiYzI1YjgwYWIzYiJ9fX0=
/// eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZiZDc1ZDNhZWYzOTk4NmJhOGU4ZDNmMzlhYmIxMDhlM2E0OGRlNTZlNTViNjQxYWNiZjNjYzRkNmQ3OTRiIn19fQ==
/// eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjJlODdmODgzMzg2NmExNjcyZTFmMmFhODk1NjYxODBmYzkxZmZiZTRiYzg2Mzc5M2ViNWU2MTYyNjQ4NSJ9fX0=
/// eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmFlZWNhMzYxNTczNGYyZGRlNjUyYzc1YzhmYjUyMGVmZjNiYTQwNWM2NTVhNWZmM2E4N2FiYzY4Yzk4MWIyIn19fQ==
/// eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWE1ZWFiYjA5NTc3NTljZjYxODRhMjk0MWNmZGEwMWI5MWZkMjFmMGQxNTIxMzM1YmYxNDMyZDczYWViZWFiIn19fQ==
/// eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDA5NTU4N2FhNTY1ODM3N2VmYmU1ZmY3ZGFmNTRmZWZmZTZkNWY2YmRhYmEzZGMxOWVlN2QyZjE4NjI2MjQ3ZCJ9fX0=
/// eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmI4ODRkNjFmMjM1MjM1MDQ3NDgzYWM0YmE0Y2U1Mjg2OTFlNjQyNGJhYzEzODE0MTU5MjcyZDk2NzNhYyJ9fX0=
/// eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjEzYTI3OGFlOTkwMmY3YzE1YmYzOTY5OWM2YTE0MjYxNDI1Y2NhZmVkYWIyNGZhNmE4NTljNDE1YTMwNWQ0YSJ9fX0=
/// eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjVlZDNhZmJkNjk2NDk5ZGVkY2NmMmRmZDY2NWZkY2VmMDQyOWE4OTk0MjhiNmZkODczNWNiZGNiMjViYjgifX19
/// eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTc4ZTZhODNmZmEzYTE2OWZjN2ExMmM2ZjZhYzkzMWZmMWIyMTJjMzYwYTU2Nzc0ZTRjOGQ0ZTI2OWI5NGU1YiJ9fX0=