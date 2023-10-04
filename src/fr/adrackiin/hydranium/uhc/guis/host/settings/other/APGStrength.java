package fr.adrackiin.hydranium.uhc.guis.host.settings.other;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.exceptions.APGuiNotFoundException;
import fr.adrackiin.hydranium.api.gui.*;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.settings.Settings;
import fr.adrackiin.hydranium.uhc.utils.MathUtils;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class APGStrength extends APGuiListener implements APGCloseCustom {

    private APGui gui;
    private final Settings settings = Game.getGame().getSettings();
    private final HashMap<Byte, Short> back = new HashMap<>();

    public APGStrength() {
        back.put((byte)1, settings.getStrength());
        APGui parent;
        try {
            parent = APICore.getPlugin().getAPGuiManager().get("§cAutres").getAPGui();
        } catch(APGuiNotFoundException e) {
            e.printStackTrace();
            return;
        }
        this.gui = new APGui("§cNerf de la Force", (byte)2, new APGuiItem[]{
                highLess.get(-10, (byte)1), less.get(-5, (byte)1), fewLess.get(-1, (byte)1), fewMore.get(1, (byte)1), more.get(5, (byte)1), highMore.get(10, (byte)1),
                cancel.get((byte)12),
                validate.get((byte)14),
                new APGuiItem(Material.BLAZE_POWDER, (short)0, (byte)1, (byte)4, get((byte)1, back.get((byte)1)), null),
        }, parent, false, false);
        parent.addChildren(this.gui, (byte)24);
    }

    @Override
    public void onClick(APGuiClickEvent e) {
        APGuiItem item = gui.getItem(e.getSlot());
        if(item.getType() == Material.STAINED_CLAY){
            byte b = (byte)(1 + e.getSlot() / 9);
            if(b == 1){
                back.replace(b, (short)MathUtils.surround((back.get(b)) + MathUtils.getValue(item.getName().substring(2)), 0, 200));
            }
            gui.getItem((byte)(4 + (b - 1) * 9)).setName(get(b, back.get(b)));
            gui.update((byte)(4 + (b - 1) * 9));
            return;
        }
        switch(e.getSlot()){
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
        return new String[]{"", "§6État: §aActivé", "",
                get((byte) 1, back.get((byte) 1))};
    }

    private void cancel(){
        back.replace((byte)1, settings.getStrength());
        for(Byte b : back.keySet()){
            gui.getItem((byte)(4 + (b - 1) * 9)).setName(get(b, back.get(b)));
            gui.update((byte)(4 + (b - 1) * 9));
        }
    }

    private void validate(){
        if(back.get((byte)1) != settings.getStrength())settings.setStrength(back.get((byte)1));
        gui.getParent().getItem((byte)24).setDescription(Collections.singletonList(getDescParent()));
        gui.getParent().update((byte)24);
    }

    private String get(byte id, Object value){
        if(id == 1){
            return "§6Force: §e+" + value + "% de dégâts";
        }
        return "";
    }

}
