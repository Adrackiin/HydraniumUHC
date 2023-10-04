package fr.adrackiin.hydranium.uhc.guis;

import fr.adrackiin.hydranium.api.exceptions.APGuiChildrenNotFoundException;
import fr.adrackiin.hydranium.api.gui.APGui;
import fr.adrackiin.hydranium.api.gui.APGuiClickEvent;
import fr.adrackiin.hydranium.api.gui.APGuiItem;
import fr.adrackiin.hydranium.api.gui.APGuiListener;
import org.bukkit.Material;

public class APGHost extends APGuiListener {

    private final APGui gui;
//host-menu
    public APGHost() {
        gui = new APGui("§cHost", (byte)4, new APGuiItem[]{
                new APGuiItem(Material.BOOKSHELF, (short)0, (byte)1, (byte)11, "§cScénarios", null),
                new APGuiItem(Material.BOOK_AND_QUILL, (short)0, (byte)1, (byte)13, "§cOptions", null),
                new APGuiItem(Material.ENCHANTED_BOOK, (short)0, (byte)1, (byte)15, "§cRègles", null),
                new APGuiItem(Material.MAP, (short)0, (byte)1, (byte)21, "§cGénération", null),
                redBorder.get((byte)0),
                redBorder.get((byte)8),
                redBorder.get((byte)9),
                redBorder.get((byte)17),
                redBorder.get((byte)18),
                redBorder.get((byte)26),
                redBorder.get((byte)27),
                redBorder.get((byte)35)
        }, null, true, false);
    }

    @Override
    public void onClick(APGuiClickEvent e) {
        try {
            this.gui.getChildren(e.getSlot()).open(e.getPlayer());
        } catch(APGuiChildrenNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public APGui getAPGui() {
        return gui;
    }
}
