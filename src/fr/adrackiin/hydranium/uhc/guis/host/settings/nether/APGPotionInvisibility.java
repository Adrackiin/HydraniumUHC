package fr.adrackiin.hydranium.uhc.guis.host.settings.nether;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.exceptions.APGuiNotFoundException;
import fr.adrackiin.hydranium.api.gui.*;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.settings.Settings;
import org.bukkit.Material;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class APGPotionInvisibility extends APGuiListener implements APGCloseCustom {

    private APGui gui;
    private final Settings settings = Game.getGame().getSettings();
    private final HashMap<Byte, Boolean> back = new HashMap<>();
    private final PotionType type = PotionType.INVISIBILITY;

    public APGPotionInvisibility() {
        back.put((byte)1, settings.isPotionDuration(type));
        back.put((byte)2, settings.isPotionSplash(type));
        APGui parent;
        try {
            parent = APICore.getPlugin().getAPGuiManager().get("§cNether").getAPGui();
        } catch(APGuiNotFoundException e) {
            e.printStackTrace();
            return;
        }
        this.gui = new APGui("§cPotion d'Invisibilité", (byte)3, new APGuiItem[]{
                deactivate.get((byte)1), activate.get((byte)7),
                deactivate.get((byte)10), activate.get((byte)16),
                cancel.get((byte)21),
                validate.get((byte)23),
                new APGuiItem(Material.REDSTONE, (short)0, (byte)1, (byte)4, get((byte)2), null),
                new APGuiItem(Material.SULPHUR, (short)0, (byte)1, (byte)13, get((byte)3), null),
        }, parent, false, false);
        parent.addChildren(this.gui, (byte)30);
    }

    @Override
    public void onClick(APGuiClickEvent e) {
        APGuiItem item = gui.getItem(e.getSlot());
        if(item.getType() == Material.STAINED_CLAY){
            switch(e.getSlot() % 9){
                case 1:
                    back.replace((byte) (1 + e.getSlot() / 9), false);
                    break;
                case 7:
                    back.replace((byte) (1 + e.getSlot() / 9), true);
                    break;
            }
            gui.getItem((byte)(4 + (e.getSlot() / 9) * 9)).setName(get((byte) (1 + e.getSlot() / 9)));
            gui.update((byte)(4 + (e.getSlot() / 9) * 9));
            return;
        }
        switch(e.getSlot()){
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
    public void customClosing(APPlayer player) {
        cancel();
    }

    @Override
    public APGui getAPGui() {
        return gui;
    }

    private String[] getDescParent(){
        return new String[]{"",
                "§6État: §aActivé", "", get((byte) 1), get((byte) 2)};
    }

    private void cancel(){
        back.replace((byte)1, settings.isPotionDuration(type));
        back.replace((byte)2, settings.isPotionSplash(type));
        for(Byte b : back.keySet()){
            gui.getItem((byte)(4 + (b - 1) * 9)).setName(get(b));
            gui.update((byte)(4 + (b - 1) * 9));
        }
    }

    private void validate(){
        if(back.get((byte)1) != settings.isPotionDuration(type))settings.setPotionDuration(type, back.get((byte)1));
        if(back.get((byte)2) != settings.isPotionSplash(type))settings.setPotionSplash(type, back.get((byte)2));
        gui.getParent().getItem((byte)30).setDescription(Collections.singletonList(getDescParent()));
        gui.getParent().update((byte)30);
    }

    private String get(byte id){
        switch(id){
            case 1: return "§6Allongée: §e" + (back.get(id) ? "§aActivé" : "§cDésactivé");
            case 2: return "§6Jetable: §e" + (back.get(id) ? "§aActivé" : "§cDésactivé");
        }
        return "";
    }

}
