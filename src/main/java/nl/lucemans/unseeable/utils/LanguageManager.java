package nl.lucemans.unseeable.utils;

import nl.lucemans.unseeable.Unseeable;

/*
 * Created by Lucemans at 21/05/2018
 * See https://lucemans.nl
 */
public class LanguageManager {

    public static String getLang(String path) {
        return (String) Unseeable.instance.getConfig().get(path);
    }

    public static String get(String path, String[] args) {
        String res = getLang(path);
        Integer i = 1;
        for (String str : args) {
            res = res.replace("$" + i, str);
            i++;
        }
        if (!path.equalsIgnoreCase("lang.prefix"))
            res = res.replace("%prefix", getLang("lang.prefix"));
        return Unseeable.parse(res);
    }
}
