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

public class APGOther extends APGuiListener {

    private APGui gui;
    private final Settings settings = Game.getGame().getSettings();
    private final HashMap<Byte, Boolean> back = new HashMap<>();

    public APGOther() {
        back.put((byte)11, settings.isShears());
        back.put((byte)12, settings.isEnderPearlDamage());
        back.put((byte)14, settings.isNotchApple());
        back.put((byte)15, settings.isAbsorption());
        back.put((byte)20, settings.isFriendlyFire());
        back.put((byte)21, settings.isDayNightCycle());
        back.put((byte)23, settings.isCraftGoldenHead());
        back.put((byte)24, settings.isNerfStrength());
        APGui parent;
        try {
            parent = APICore.getPlugin().getAPGuiManager().get("§cOptions").getAPGui();
        } catch(APGuiNotFoundException e) {
            e.printStackTrace();
            return;
        }
        this.gui = new APGui("§cAutres", (byte)4, new APGuiItem[]{
                new APGuiItem(Material.SHEARS, (short)0, (byte)1, (byte)11, get((byte)11, back.get((byte)11) ? "§a" : "§c"), getDesc((byte)11)),
                new APGuiItem(Material.ENDER_PEARL, (short)0, (byte)1, (byte)12, get((byte)12, back.get((byte)12) ? "§a" : "§c"), getDesc((byte)12)),
                new APGuiItem(Material.GOLDEN_APPLE, (short)1, (byte)1, (byte)14, get((byte)14, back.get((byte)14) ? "§a" : "§c"), getDesc((byte)14)),
                new APGuiItem(Material.GOLD_INGOT, (short)0, (byte)1, (byte)15, get((byte)15, back.get((byte)15) ? "§a" : "§c"), getDesc((byte)15)),
                new APGuiItem(Material.FISHING_ROD, (short)0, (byte)1, (byte)20, get((byte)20, back.get((byte)20) ? "§a" : "§c"), getDesc((byte)20)),
                new APGuiItem(Material.WATCH, (short)0, (byte)1, (byte)21, get((byte)21, back.get((byte)21) ? "§a" : "§c"), getDesc((byte)21)),
                new APGuiItem(Material.WORKBENCH, (short)0, (byte)1, (byte)23, get((byte)23, back.get((byte)23) ? "§a" : "§c"), getDesc((byte)23)),
                new APGuiItem(Material.BLAZE_POWDER, (short)0, (byte)1, (byte)24, get((byte)24, back.get((byte)24) ? "§a" : "§c"), getDesc((byte)24)),
        }, parent, true, true);
        parent.addChildren(this.gui, (byte)23);
        for(byte b : back.keySet()){
            this.gui.getItem(b).setGlow(back.get(b));
            this.gui.update(b);
        }
    }

    @Override
    public void onClick(APGuiClickEvent e) {
        switch(e.getSlot()){
            case 11:
                settings.setShears(!settings.isShears());
                break;
            case 12:
                settings.setEnderPearlDamage(!settings.isEnderPearlDamage());
                break;
            case 14:
                settings.setNotchApple(!settings.isNotchApple());
                break;
            case 15:
                settings.setAbsorption(!settings.isAbsorption());
                break;
            case 20:
                settings.setFriendlyFire(!settings.isFriendlyFire());
                break;
            case 21:
                settings.setDayNightCycle(!settings.isDayNightCycle());
                break;
            case 23:
                settings.setCraftGoldenHead(!settings.isCraftGoldenHead());
                break;
            case 24:
                if(e.isClickConfigure()){
                    try {
                        gui.getChildren(e.getSlot()).open(e.getPlayer());
                        if(back.get(e.getSlot()))return;
                    } catch(APGuiChildrenNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
                settings.setNerfStrength(!settings.isNerfStrength());
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
            case 11: return color + "Cisailles";
            case 12: return color + "Dégâts Enderperle";
            case 14: return color + "Pomme de Notch";
            case 15: return color + "Absorption";
            case 20: return color + "Friendly Fire";
            case 21: return color + "Cycle Jour/Nuit";
            case 23: return color + "Craft Tête d'or";
            case 24: return color + "Nerf de la Force";
        }
        return "";
    }

    private String[] getDesc(byte slot){
        if(back.get(slot)) {
            if(slot == 24){
                return new String[]{"", "§6État: §aActivé", "", "§6Force: §e+" + settings.getStrength() + "% de dégâts"};
            }
        }
        return new String[]{"", "§6État: " + getState(slot)};
    }

    private String getState(byte slot){
        return (back.get(slot) ? "§aActivé" : "§cDésactivé");
    }

}
