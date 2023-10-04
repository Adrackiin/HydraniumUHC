package fr.adrackiin.hydranium.uhc.guis;

import fr.adrackiin.hydranium.api.gui.APGui;
import fr.adrackiin.hydranium.api.gui.APGuiClickEvent;
import fr.adrackiin.hydranium.api.gui.APGuiItem;
import fr.adrackiin.hydranium.api.gui.APGuiListener;

public class APGBuffer extends APGuiListener {

    private final APGui gui;

    public APGBuffer() {
        this.gui = new APGui(" ", (byte)0, new APGuiItem[]{}, null, false, false);
    }

    @Override
    public void onClick(APGuiClickEvent e) {

    }

    @Override
    public APGui getAPGui() {
        return gui;
    }
}
