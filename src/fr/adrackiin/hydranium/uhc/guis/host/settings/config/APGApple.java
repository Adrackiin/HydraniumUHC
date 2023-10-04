package fr.adrackiin.hydranium.uhc.guis.host.settings.config;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.exceptions.APGuiNotFoundException;
import fr.adrackiin.hydranium.api.gui.*;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.settings.Settings;
import fr.adrackiin.hydranium.uhc.utils.MathUtils;
import fr.adrackiin.hydranium.uhc.utils.StringUtils;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class APGApple extends APGuiListener implements APGCloseCustom {

    private APGui gui;
    private final Settings settings = Game.getGame().getSettings();
    private final HashMap<Byte, Object> back = new HashMap<>();

    public APGApple() {
        back.put((byte)1, settings.isAllTrees());
        back.put((byte)2, settings.getAppleRate());
        APGui parent;
        try {
            parent = APICore.getPlugin().getAPGuiManager().get("§cConfiguration").getAPGui();
        } catch(APGuiNotFoundException e) {
            e.printStackTrace();
            return;
        }
        this.gui = new APGui("§cDrop: Pomme", (byte)3, new APGuiItem[]{
                new APGuiItem(Material.SAPLING, (short)1, (byte)1, (byte)2, "§eTous les arbres", null),
                new APGuiItem(Material.SAPLING, (short)0, (byte)1, (byte)6, "§eChênes seulement", null),
                highLess.get(-1, (byte)2), less.get(-0.5F, (byte)2), fewLess.get(-0.25F, (byte)2), fewMore.get(0.25F, (byte)2), more.get(0.5F, (byte)2), highMore.get(1, (byte)2),
                cancel.get((byte)21),
                validate.get((byte)23),
                new APGuiItem(Material.APPLE, (short)0, (byte)1, (byte)4, get((byte)1, back.get((byte)1)), null),
                new APGuiItem(Material.APPLE, (short)0, (byte)1, (byte)13, get((byte)2, back.get((byte)2)), null),
        }, parent, false, false);
        parent.addChildren(this.gui, (byte)15);
    }

    @Override
    public void onClick(APGuiClickEvent e) {
        APGuiItem item = gui.getItem(e.getSlot());
        if(item.getType() == Material.STAINED_CLAY){
            byte b = (byte)(1 + e.getSlot() / 9);
            if(b == 2){
                back.replace(b, MathUtils.surround((float)(back.get(b)) + MathUtils.getValue(item.getName().substring(2)), 0, 100));
            }
            gui.getItem((byte)(4 + (b - 1) * 9)).setName(get(b, back.get(b)));
            gui.update((byte)(4 + (b - 1) * 9));
            return;
        }
        switch(e.getSlot()){
            case 2:
                back.replace((byte)1, true);
                gui.getItem((byte)4).setName(get((byte)1, back.get((byte)1)));
                gui.update((byte)4);
                break;
            case 6:
                back.replace((byte)1, false);
                gui.getItem((byte)4).setName(get((byte)1, back.get((byte)1)));
                gui.update((byte)4);
                break;
            case 21:
                cancel();
                gui.getParent().open(e.getPlayer());
                break;
            case 23:
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
                get((byte) 2, back.get((byte) 2))};
    }

    private void cancel(){
        back.replace((byte)1, settings.isAllTrees());
        back.replace((byte)2, settings.getAppleRate());
        for(Byte b : back.keySet()){
            gui.getItem((byte)(4 + (b - 1) * 9)).setName(get(b, back.get(b)));
            gui.update((byte)(4 + (b - 1) * 9));
        }
    }

    private void validate(){
        if((boolean)back.get((byte)1) != settings.isAllTrees())settings.setAllTrees((boolean)back.get((byte)1));
        if((float)back.get((byte)2) != settings.getAppleRate())settings.setAppleRate((float)back.get((byte)2));
        gui.getParent().getItem((byte)15).setDescription(Collections.singletonList(getDescParent()));
        gui.getParent().update((byte)15);
    }

    private String get(byte id, Object value){
        switch(id){
            case 1: return "§6Arbres: §e" + ((boolean)value ? "Tous les arbres" : "Chênes seulement");
            case 2: return "§6Taux: §e" + StringUtils.toIntegerString((Float) value) + "%";
        }
        return "";
    }
}
