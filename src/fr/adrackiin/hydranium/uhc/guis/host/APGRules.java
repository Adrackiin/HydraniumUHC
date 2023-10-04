package fr.adrackiin.hydranium.uhc.guis.host;

import fr.adrackiin.hydranium.api.APICore;
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

public class APGRules extends APGuiListener {

    private APGui gui;
    private final Settings settings = Game.getGame().getSettings();
    private final HashMap<Byte, Boolean> back = new HashMap<>();

    public APGRules() {
        back.put((byte)10, settings.isStalk());
        back.put((byte)19, settings.isSteal());
        back.put((byte)28, settings.isIpvp());
        back.put((byte)37, settings.isCrossteam());
        back.put((byte)13, settings.isTrap());
        back.put((byte)22, settings.isBuild());
        back.put((byte)31, settings.isTower());
        back.put((byte)40, settings.isDigdown());
        back.put((byte)16, settings.isStripMining());
        back.put((byte)25, settings.isPokeHoling());
        back.put((byte)34, settings.isRollerCoaster());
        back.put((byte)43, settings.isSoundMining());
        APGui parent;
        try {
            parent = APICore.getPlugin().getAPGuiManager().get("§cHost").getAPGui();
        } catch(APGuiNotFoundException e) {
            e.printStackTrace();
            return;
        }
        this.gui = new APGui("§cRègles", (byte)6, new APGuiItem[]{
                new APGuiItem(Material.IRON_SWORD, (short)0, (byte)1, (byte)10, get((byte)10, back.get((byte)10) ? "§a" : "§c"), getDesc((byte)10)),
                new APGuiItem(Material.APPLE, (short)0, (byte)1, (byte)19, get((byte)19, back.get((byte)19) ? "§a" : "§c"), getDesc((byte)19)),
                new APGuiItem(Material.LAVA_BUCKET, (short)0, (byte)1, (byte)28, get((byte)28, back.get((byte)28) ? "§a" : "§c"), getDesc((byte)28)),
                new APGuiItem(Material.LEASH, (short)0, (byte)1, (byte)37, get((byte)37, back.get((byte)37) ? "§a" : "§c"), getDesc((byte)37)),
                new APGuiItem(Material.PISTON_STICKY_BASE, (short)0, (byte)1, (byte)13, get((byte)13, back.get((byte)13) ? "§a" : "§c"), getDesc((byte)13)),
                new APGuiItem(Material.COBBLESTONE, (short)0, (byte)1, (byte)22, get((byte)22, back.get((byte)22) ? "§a" : "§c"), getDesc((byte)22)),
                new APGuiItem(Material.LADDER, (short)0, (byte)1, (byte)31, get((byte)31, back.get((byte)31) ? "§a" : "§c"), getDesc((byte)31)),
                new APGuiItem(Material.DIAMOND_SPADE, (short)0, (byte)1, (byte)40, get((byte)40, back.get((byte)40) ? "§a" : "§c"), getDesc((byte)40)),
                new APGuiItem(Material.DIAMOND_PICKAXE, (short)0, (byte)1, (byte)16, get((byte)16, back.get((byte)16) ? "§a" : "§c"), getDesc((byte)16)),
                new APGuiItem(Material.GLASS, (short)0, (byte)1, (byte)25, get((byte)25, back.get((byte)25) ? "§a" : "§c"), getDesc((byte)25)),
                new APGuiItem(Material.COBBLESTONE_STAIRS, (short)0, (byte)1, (byte)34, get((byte)34, back.get((byte)34) ? "§a" : "§c"), getDesc((byte)34)),
                new APGuiItem(Material.SKULL_ITEM, (short)2, (byte)1, (byte)43, get((byte)43, back.get((byte)43) ? "§a" : "§c"), getDesc((byte)43)),
        }, parent, true, true);
        parent.addChildren(this.gui, (byte)15);
        for(byte b : back.keySet()){
            this.gui.getItem(b).setGlow(back.get(b));
            this.gui.update(b);
        }
    }

    @Override
    public void onClick(APGuiClickEvent e) {
        switch(e.getSlot()){
            case 10:
                settings.setStalk(!settings.isStalk());
                break;
            case 19:
                settings.setStalk(!settings.isStalk());
                break;
            case 28:
                settings.setIpvp(!settings.isIpvp());
                break;
            case 37:
                settings.setCrossteam(!settings.isCrossteam());
                break;
            case 13:
                settings.setTrap(!settings.isTrap());
                break;
            case 22:
                settings.setBuild(!settings.isBuild());
                break;
            case 31:
                settings.setTower(!settings.isTower());
                break;
            case 40:
                settings.setDigdown(!settings.isDigdown());
                break;
            case 16:
                settings.setStripMining(!settings.isStripMining());
                break;
            case 25:
                settings.setPokeHoling(!settings.isPokeHoling());
                break;
            case 34:
                settings.setRollerCoaster(!settings.isRollerCoaster());
                break;
            case 43:
                settings.setSoundMining(!settings.isSoundMining());
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
            case 10: return color + "Stalk";
            case 19: return color + "Vol";
            case 28: return color + "IPvP";
            case 37: return color + "CrossTeam";
            case 13: return color + "Pièges";
            case 22: return color + "Construction";
            case 31: return color + "Tower";
            case 40: return color + "DigDown";
            case 16: return color + "Minage tout droit";
            case 25: return color + "Minage optimisé";
            case 34: return color + "Minage en escalier (5 à 32)";
            case 43: return color + "Minage aux sons";
        }
        return "";
    }

    private String[] getDesc(byte slot){
        return new String[]{"", "§6État: " + getState(slot)};
    }

    private String getState(byte slot){
        return (back.get(slot) ? "§aActivé" : "§cDésactivé");
    }

}
