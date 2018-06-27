package nl.lucemans.unseeable.commands;

import net.md_5.bungee.api.chat.BaseComponent;
import nl.lucemans.unseeable.Unseeable;
import nl.lucemans.unseeable.gui.EditorGUI;
import nl.lucemans.unseeable.gui.MapEditor;
import nl.lucemans.unseeable.system.Map;
import org.bukkit.entity.Player;

public class EditCommand implements BaseCommand {

    @Override
    public void execute(Player p, String[] args) {
        if (args.length == 1) {
            EditorGUI.openEditor(p);
            return;
        }

        String t = args[1];
        Map m = Unseeable.instance.findMap(t);
        if (m != null) {
            MapEditor.openMap(p, m);
            return;
        }

        EditorGUI.openEditor(p);
    }
}
