package nl.lucemans.unseeable.utils;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;

/*
 * Created by Lucemans at 21/05/2018
 * See https://lucemans.nl
 */
public class LScoreboard {

    public Scoreboard scoreboard;
    public Objective obj;
    public String name;
    public int linesSkipped = 0;
    public ArrayList<String> content;
    private ArrayList<String> lastContent;

    public LScoreboard() {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        obj = scoreboard.registerNewObjective("lscore", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        name = "NAME";
        content = new ArrayList<String>();
        lastContent = new ArrayList<String>();
    }

    public void update() {
        obj.setDisplayName(name);
        if (changed()) {
            for (String str : lastContent) {
                scoreboard.resetScores(str);
            }
            int slot = 0;
            for (String str : content) {
                Score s = obj.getScore(str);
                s.setScore(content.size() - slot);
                slot++;
            }
            lastContent = (ArrayList<String>) content.clone();
        }
        //Integer slot = 0;
        /*for (String newStr : content) {
            if (lastContent.get(slot) == null) {
                Score score = obj.getScore(newStr);
                score.setScore(content.size() - slot);
            }
            else
            {
                if (!lastContent.get(slot).equalsIgnoreCase(newStr)) {
                    scoreboard.resetScores(lastContent.get(slot));

                    Score s1 = obj.getScore(newStr);
                    s1.setScore(content.size() - slot);
                }
            }
            slot++;
        }*/
    }

    public void spacer() {
        String str = "";
        for (int i = 0; i <= linesSkipped; i++) {
            str += " ";
        }
        content.add(str);
        linesSkipped += 1;
    }

    public boolean changed() {
        if (lastContent.size() != content.size())
            return true;
        int i = 0;
        for (String str : lastContent) {
            if (!content.get(i).equalsIgnoreCase(str))
                return true;
            i++;
        }
        return false;
    }
}
