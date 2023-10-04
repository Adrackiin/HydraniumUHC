package fr.adrackiin.hydranium.uhc.guis.host;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.exceptions.APGuiChildrenNotFoundException;
import fr.adrackiin.hydranium.api.exceptions.APGuiNotFoundException;
import fr.adrackiin.hydranium.api.gui.APGui;
import fr.adrackiin.hydranium.api.gui.APGuiClickEvent;
import fr.adrackiin.hydranium.api.gui.APGuiItem;
import fr.adrackiin.hydranium.api.gui.APGuiListener;
import org.bukkit.Material;

public class APGSettings extends APGuiListener {

    private APGui gui;

    public APGSettings() {
        APGui parent;
        try {
            parent = APICore.getPlugin().getAPGuiManager().get("§cHost").getAPGui();
        } catch(APGuiNotFoundException e) {
            e.printStackTrace();
            return;
        }
        this.gui = new APGui("§cOptions", (byte)4, new APGuiItem[]{
                new APGuiItem(Material.BOOK, (short)0, (byte)1, (byte)11, "§cConfiguration", null),
                new APGuiItem(Material.GOLDEN_APPLE, (short)0, (byte)1, (byte)13, "§cMort", null),
                new APGuiItem(Material.MONSTER_EGG, (short)50, (byte)1, (byte)15, "§cMobs", null),
                new APGuiItem(Material.NETHERRACK, (short)0, (byte)1, (byte)21, "§cNether", null),
                new APGuiItem(Material.PAPER, (short)0, (byte)1, (byte)23, "§cParamètres", null),
        }, parent, true, true);
        parent.addChildren(this.gui, (byte)13);
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
