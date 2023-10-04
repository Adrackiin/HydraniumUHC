package fr.adrackiin.hydranium.uhc.guis.host.settings.config;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.exceptions.APGuiNotFoundException;
import fr.adrackiin.hydranium.api.gui.*;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.border.UHCBorder;
import fr.adrackiin.hydranium.uhc.game.settings.Settings;
import fr.adrackiin.hydranium.uhc.utils.MathUtils;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class APGBorder extends APGuiListener implements APGCloseCustom {

    private APGui gui;
    private final Settings settings = Game.getGame().getSettings();
    private final HashMap<Byte, Object> back = new HashMap<>();

    public APGBorder() {
        back.put((byte)1, settings.getBorderType());
        back.put((byte)2, settings.getSize());
        back.put((byte)3, settings.getPvp());
        APGui parent;
        try {
            parent = APICore.getPlugin().getAPGuiManager().get("§cConfiguration").getAPGui();
        } catch(APGuiNotFoundException e) {
            e.printStackTrace();
            return;
        }
        this.gui = new APGui("§cBordure", (byte)4, new APGuiItem[]{
                new APGuiItem(Material.ENDER_PEARL, (short)0, (byte)1, (byte)2, "§eTéléportation", null),
                new APGuiItem(Material.EYE_OF_ENDER, (short)0, (byte)1, (byte)6, "§eMouvement", null),
                highLess.get(-100, (byte)2), less.get(-50, (byte)2), fewLess.get(-10, (byte)2), fewMore.get(10, (byte)2), more.get(50, (byte)2), highMore.get(100, (byte)2),
                highLess.get(-50, (byte)3), less.get(-10, (byte)3), fewLess.get(-5, (byte)3), fewMore.get(5, (byte)3), more.get(10, (byte)3), highMore.get(50, (byte)3),
                cancel.get((byte)30),
                validate.get((byte)32),
                new APGuiItem(Material.BARRIER, (short)0, (byte)1, (byte)4, get((byte)1, back.get((byte)1)), null),
                new APGuiItem(Material.BARRIER, (short)0, (byte)1, (byte)13, get((byte)2, back.get((byte)2)), null),
                new APGuiItem(Material.BARRIER, (short)0, (byte)1, (byte)22, get((byte)3, back.get((byte)3)), null),
        }, parent, false, false);
        parent.addChildren(this.gui, (byte)13);
    }

    @Override
    public void onClick(APGuiClickEvent e) {
        APGuiItem item = gui.getItem(e.getSlot());
        if(item.getType() == Material.STAINED_CLAY){
            byte b = (byte)(1 + e.getSlot() / 9);
            switch(b){
                case 2: back.replace(b, (short)MathUtils.surround((short)(back.get(b)) + MathUtils.getValue(item.getName().substring(2)), 100, 2000));break;
                case 3: back.replace(b, (short)MathUtils.surround((short)(back.get(b)) + MathUtils.getValue(item.getName().substring(2)), 10, (short)(back.get((byte)2)) - 1));break;
            }
            gui.getItem((byte)(4 + (b - 1) * 9)).setName(get(b, back.get(b)));
            gui.update((byte)(4 + (b - 1) * 9));
            return;
        }
        switch(e.getSlot()){
            case 2:
                back.replace((byte)1, UHCBorder.Type.BORDER_TP);
                gui.getItem((byte)4).setName(get((byte)1, back.get((byte)1)));
                gui.update((byte)4);
                break;
            case 6:
                back.replace((byte)1, UHCBorder.Type.BORDERMOVE);
                gui.getItem((byte)4).setName(get((byte)1, back.get((byte)1)));
                gui.update((byte)4);
                break;
            case 30:
                cancel();
                gui.getParent().open(e.getPlayer());
                break;
            case 32:
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
                get((byte) 1, back.get((byte) 1)),
                get((byte) 2, back.get((byte) 2)),
                get((byte) 3, back.get((byte) 3))};
    }

    private void cancel(){
        back.replace((byte)1, settings.getBorderType());
        back.replace((byte)2, settings.getSize());
        back.replace((byte)3, settings.getFinalSize());
        for(Byte b : back.keySet()){
            gui.getItem((byte)(4 + (b - 1) * 9)).setName(get(b, back.get(b)));
            gui.update((byte)(4 + (b - 1) * 9));
        }
    }

    private void validate(){
        if(back.get((byte)1) != settings.getBorderType())settings.setBorderType((UHCBorder.Type)back.get((byte)1));
        if((short)back.get((byte)2) != settings.getSize())settings.setSize((short)back.get((byte)2));
        if((short)back.get((byte)3) != settings.getFinalSize())settings.setFinalSize((short)back.get((byte)3));
        gui.getParent().getItem((byte)13).setDescription(Collections.singletonList(getDescParent()));
        gui.getParent().update((byte)13);
    }

    private String get(byte id, Object value){
        switch(id){
            case 1: return "§6Bordure: §e" + (value == UHCBorder.Type.BORDERMOVE ? "Mouvement" : "Téléportation");
            case 2: return "§6Taille: §e" + value + "§7 blocs";
            case 3: return "§6Taille finale: §e" + value + "§7 blocs";
        }
        return "";
    }
}
