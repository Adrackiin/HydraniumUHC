package fr.adrackiin.hydranium.uhc.guis.host.settings.death;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.exceptions.APGuiNotFoundException;
import fr.adrackiin.hydranium.api.gui.*;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.settings.Settings;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class APGHead extends APGuiListener implements APGCloseCustom {

    private APGui gui;
    private final Settings settings = Game.getGame().getSettings();
    private final HashMap<Byte, Object> back = new HashMap<>();

    public APGHead() {
        back.put((byte)1, settings.howDropHead());
        APGui parent;
        try {
            parent = APICore.getPlugin().getAPGuiManager().get("§cMort").getAPGui();
        } catch(APGuiNotFoundException e) {
            e.printStackTrace();
            return;
        }
        this.gui = new APGui("§cDrop de la tête", (byte)2, new APGuiItem[]{
                new APGuiItem(Material.GRASS, (short)0, (byte)1, (byte)1, "§eSur le sol", null),
                new APGuiItem(Material.FENCE, (short)0, (byte)1, (byte)7, "§eSur poteau", null),
                cancel.get((byte)12),
                validate.get((byte)14),
                new APGuiItem(Material.SKULL_ITEM, (short)3, (byte)1, (byte)4, get((byte)1, back.get((byte)1)), null),
        }, parent, false, false);
        parent.addChildren(this.gui, (byte)15);
    }

    @Override
    public void onClick(APGuiClickEvent e) {
        switch(e.getSlot()){
            case 2:
                back.replace((byte)1, Settings.DropHead.GROUND);
                gui.getItem((byte)4).setName(get((byte)1, back.get((byte)1)));
                gui.update((byte)4);
                break;
            case 6:
                back.replace((byte)1, Settings.DropHead.FENCE);
                gui.getItem((byte)4).setName(get((byte)1, back.get((byte)1)));
                gui.update((byte)4);
                break;
            case 12:
                cancel();
                gui.getParent().open(e.getPlayer());
                break;
            case 14:
                validate();
                gui.getParent().open(e.getPlayer());
                break;
        }
    }

    @Override
    public void customClosing(APPlayer player) {
        cancel();
    }

    @Override
    public APGui getAPGui() {
        return gui;
    }

    private String[] getDescParent(){
        return new String[]{"",
                "§7Drop une tête", "§7à la mort d'un joueur", "", "§6État: §aActivé", "", "§6Drop: §e" + (settings.howDropHead() == Settings.DropHead.GROUND ? "Sur le sol" : "Sur poteau")};

    }

    private void cancel(){
        back.replace((byte)1, settings.howDropHead());
        for(Byte b : back.keySet()){
            gui.getItem((byte)(4 + (b - 1) * 9)).setName(get(b, back.get(b)));
            gui.update((byte)(4 + (b - 1) * 9));
        }
    }

    private void validate(){
        if(back.get((byte)1) != settings.howDropHead())settings.setHowDropHead((Settings.DropHead) back.get((byte)1));
        gui.getParent().getItem((byte)15).setDescription(Collections.singletonList(getDescParent()));
        gui.getParent().update((byte)15);
    }

    private String get(byte id, Object value){
        if(id == 1){
            return "§6Drop de la tête: §e" + (value == Settings.DropHead.GROUND ? "Sur le sol" : "Sur poteau");
        }
        return "";
    }
}
