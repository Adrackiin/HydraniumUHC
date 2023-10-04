package fr.adrackiin.hydranium.uhc.guis.host.settings;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.exceptions.APGuiChildrenNotFoundException;
import fr.adrackiin.hydranium.api.exceptions.APGuiNotFoundException;
import fr.adrackiin.hydranium.api.gui.APGui;
import fr.adrackiin.hydranium.api.gui.APGuiClickEvent;
import fr.adrackiin.hydranium.api.gui.APGuiItem;
import fr.adrackiin.hydranium.api.gui.APGuiListener;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.settings.Settings;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class APGDeath extends APGuiListener {

    private APGui gui;
    private final Settings settings = Game.getGame().getSettings();
    private final HashMap<Byte, Boolean> back = new HashMap<>();

    public APGDeath() {
        back.put((byte)11, settings.isGapple());
        back.put((byte)13, settings.isGhead());
        back.put((byte)15, settings.isDropHead());
        APGui parent;
        try {
            parent = APICore.getPlugin().getAPGuiManager().get("§cOptions").getAPGui();
        } catch(APGuiNotFoundException e) {
            e.printStackTrace();
            return;
        }
        this.gui = new APGui("§cMort", (byte)3, new APGuiItem[]{
                new APGuiItem(Material.GOLDEN_APPLE, (short)0, (byte)1, (byte)11, get((byte)11, back.get((byte)11) ? "§a" : "§c"), getDesc((byte)11)),
                new APGuiItem(Material.GOLD_BLOCK, (short)0, (byte)1, (byte)13, get((byte)13, back.get((byte)13) ? "§a" : "§c"), getDesc((byte)13)),
                new APGuiItem(Material.SKULL_ITEM, (short)3, (byte)1, (byte)15, get((byte)15, back.get((byte)15) ? "§a" : "§c"), getDesc((byte)15)),
        }, parent, true, true);
        parent.addChildren(this.gui, (byte)13);
        for(byte i = 11; i <= 15; i += 2){
            this.gui.getItem(i).setGlow(back.get(i));
            this.gui.update(i);
        }
    }

    @Override
    public void onClick(APGuiClickEvent e) {
        switch(e.getSlot()){
            case 11:
                settings.setGapple(!settings.isGapple());
                break;
            case 13:
                settings.setGhead(!settings.isGhead());
                break;
            case 15:
                if(e.isClickConfigure()){
                    try {
                        gui.getChildren(e.getSlot()).open(e.getPlayer());
                        if(back.get(e.getSlot()))return;
                    } catch(APGuiChildrenNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
                settings.setDropHead(!settings.isDropHead());
                break;
            default:
                return;
        }
        back.replace(e.getSlot(), !back.get(e.getSlot()));
        gui.getItem(e.getSlot()).setName(get(e.getSlot(), (back.get(e.getSlot()) ? "§a" : "§c")));
        gui.getItem(e.getSlot()).setDescription(Collections.singletonList(getDesc(e.getSlot())));
        gui.getItem(e.getSlot()).setGlow(back.get(e.getSlot()));
        gui.update(e.getSlot());
    }

    @Override
    public APGui getAPGui() {
        return gui;
    }

    private String get(byte slot, String color){
        switch(slot){
            case 11: return color + "Pomme d'or";
            case 13: return color + "Tête d'or";
            case 15: return color + "Tête";
        }
        return "";
    }

    private String[] getDesc(byte slot){
        switch(slot){
            case 11: return new String[]{"", "§7Drop une pomme d'or", "§7à la mort d'un joueur", "", "§6État: " + getState(slot)};
            case 13: return new String[]{"", "§7Drop une tête d'or", "§7à la mort d'un joueur", "", "§6État: " + getState(slot)};
            case 15:
                if(back.get(slot))return new String[]{"", "§7Drop une tête", "§7à la mort d'un joueur", "", "§6État: " + getState(slot), "", "§6Drop: §e" + (settings.howDropHead() == Settings.DropHead.GROUND ? "Sur le sol" : "Sur poteau")};
                return new String[]{"", "§7Drop une tête", "§7à la mort d'un joueur", "", "§6État: " + getState(slot)};
        }
        return new String[]{};
    }

    private String getState(byte slot){
        return (back.get(slot) ? "§aActivé" : "§cDésactivé");
    }

}
