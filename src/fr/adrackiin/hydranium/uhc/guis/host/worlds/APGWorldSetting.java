package fr.adrackiin.hydranium.uhc.guis.host.worlds;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.exceptions.APGuiNotFoundException;
import fr.adrackiin.hydranium.api.gui.APGui;
import fr.adrackiin.hydranium.api.gui.APGuiClickEvent;
import fr.adrackiin.hydranium.api.gui.APGuiItem;
import fr.adrackiin.hydranium.api.gui.APGuiListener;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.gameworld.GameWorld;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class APGWorldSetting extends APGuiListener {

    private APGui gui;
    private final HashMap<Byte, Boolean> back = new HashMap<>();

    public APGWorldSetting() {
        back.put((byte)10, Game.getGame().getCaveOre().isActive());
        back.put((byte)16, Game.getGame().getLapis().isActive());
        APGui parent;
        try {
            parent = APICore.getPlugin().getAPGuiManager().get("§cGénération").getAPGui();
        } catch(APGuiNotFoundException e) {
            e.printStackTrace();
            return;
        }
        this.gui = new APGui("§cOptions de Génération", (byte)3, new APGuiItem[]{
                new APGuiItem(Material.DIAMOND_ORE, (short)0, (byte)1, (byte)10, get((byte)10, back.get((byte)10) ? "§a" : "§c"), getDesc((byte)10)),
                new APGuiItem(Material.LAPIS_BLOCK, (short)0, (byte)1, (byte)16, get((byte)16, back.get((byte)16) ? "§a" : "§c"), getDesc((byte)16))
        }, parent, true, true);
        parent.addChildren(this.gui, (byte)31);
    }

    @Override
    public void onClick(APGuiClickEvent e) {
        switch(e.getSlot()){
            case 10:
                if(Game.getGame().getCaveOre().isActive()) Game.getGame().getCaveOre().stop();
                else {
                    if(Game.getGame().getGameWorldType() != GameWorld.Type.NORMAL){
                        e.getPlayer().sendMessage("§cMonde inadapté");
                        return;
                    }
                    Game.getGame().getCaveOre().activate();
                }
                break;
            case 16:
                if(Game.getGame().getLapis().isActive()) Game.getGame().getLapis().stop();
                else {
                    if(Game.getGame().getGameWorldType() != GameWorld.Type.FLAT_VILLAGE){
                        e.getPlayer().sendMessage("§cMonde inadapté");
                        return;
                    }
                    Game.getGame().getLapis().activate();
                }
                break;
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

    private String get(byte slot, String color) {
        switch(slot){
            case 10: return color + "Minerais";
            case 16: return color + "Lapis";
        }
        return "";
    }

    private String[] getDesc(byte slot) {
        switch(slot){
            case 10: return new String[]{"", "§7Générer les minerais seulement en grottes", "", "§6État: " + (back.get(slot) ? "§aActivé" : "§cDésactivé"), "", "§cUniquement en Overworld - Normal"};
            case 16: return new String[]{"", "§7Générer des blocks de lapis", "§7dans les bibliothèques des villages", "", "§6État: " + (back.get(slot) ? "§aActivé" : "§cDésactivé"), "", "§cUniquement en Overworld - FlatLand Villages"};
        }
        return new String[]{};
    }


}
