package fr.adrackiin.hydranium.uhc.guis.host.settings.config;

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

public class APGFlint extends APGuiListener implements APGCloseCustom {

    private APGui gui;
    private final Settings settings = Game.getGame().getSettings();
    private final HashMap<Byte, Object> back = new HashMap<>();

    public APGFlint() {
        back.put((byte)1, settings.getFlintRate());
        APGui parent;
        try {
            parent = APICore.getPlugin().getAPGuiManager().get("§cConfiguration").getAPGui();
        } catch(APGuiNotFoundException e) {
            e.printStackTrace();
            return;
        }
        this.gui = new APGui("§cDrop: Silex", (byte)2, new APGuiItem[]{
                highLess.get(-10, (byte)1), less.get(-5, (byte)1), fewLess.get(-1, (byte)1), fewMore.get(1, (byte)1), more.get(5, (byte)1), highMore.get(10, (byte)1),
                cancel.get((byte)12),
                validate.get((byte)14),
                new APGuiItem(Material.FLINT, (short)0, (byte)1, (byte)4, get((byte)1, back.get((byte)1)), null),
        }, parent, false, false);
        parent.addChildren(this.gui, (byte)16);
    }

    @Override
    public void onClick(APGuiClickEvent e) {
        APGuiItem item = gui.getItem(e.getSlot());
        if(item.getType() == Material.STAINED_CLAY){
            byte b = (byte)(1 + e.getSlot() / 9);
            if(b == 1){
                back.replace(b, (byte)MathUtils.surround((byte)(back.get(b)) + MathUtils.getValue(item.getName().substring(2)), 0, 100));
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
    public void customClosing(APPlayer apPlayer) {
        cancel();
    }

    @Override
    public APGui getAPGui() {
        return gui;
    }

    private String[] getDescParent(){
        return new String[]{"",
                get((byte) 1, back.get((byte) 1))};
    }

    private void cancel(){
        back.replace((byte)1, settings.getFlintRate());
        for(Byte b : back.keySet()){
            gui.getItem((byte)(4 + (b - 1) * 9)).setName(get(b, back.get(b)));
            gui.update((byte)(4 + (b - 1) * 9));
        }
    }

    private void validate(){
        if((byte)back.get((byte)1) != settings.getFlintRate())settings.setFlintRate((byte)back.get((byte)1));
        gui.getParent().getItem((byte)16).setDescription(Collections.singletonList(getDescParent()));
        gui.getParent().update((byte)16);
    }

    private String get(byte id, Object value){
        if(id == 1){
            return "§6Taux: §e" + value + "%";
        }
        return "";
    }
}
