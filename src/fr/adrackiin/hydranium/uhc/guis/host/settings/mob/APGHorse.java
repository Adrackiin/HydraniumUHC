package fr.adrackiin.hydranium.uhc.guis.host.settings.mob;

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

public class APGHorse extends APGuiListener implements APGCloseCustom {

    private APGui gui;
    private final Settings settings = Game.getGame().getSettings();
    private final HashMap<Byte, Boolean> back = new HashMap<>();

    public APGHorse() {
        back.put((byte)1, settings.isHorseSaddle());
        back.put((byte)2, settings.isHorseArmor());
        back.put((byte)3, settings.isHorseHeal());
        APGui parent;
        try {
            parent = APICore.getPlugin().getAPGuiManager().get("§cMobs").getAPGui();
        } catch(APGuiNotFoundException e) {
            e.printStackTrace();
            return;
        }
        this.gui = new APGui("§cCheval", (byte)4, new APGuiItem[]{
                deactivate.get((byte)1), activate.get((byte)7),
                deactivate.get((byte)10), activate.get((byte)16),
                deactivate.get((byte)19), activate.get((byte)25),
                cancel.get((byte)30),
                validate.get((byte)32),
                new APGuiItem(Material.SADDLE, (short)0, (byte)1, (byte)4, get((byte)1), null),
                new APGuiItem(Material.DIAMOND_BARDING, (short)0, (byte)1, (byte)13, get((byte)2), null),
                new APGuiItem(Material.WHEAT, (short)0, (byte)1, (byte)22, get((byte)3), null),
        }, parent, false, false);
        parent.addChildren(this.gui, (byte)43);
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
    public void customClosing(APPlayer player) {
        cancel();
    }

    @Override
    public APGui getAPGui() {
        return gui;
    }

    private String[] getDescParent(){
        return new String[]{"",
                "§6Spawn: §aActivé", "",
                get((byte) 1), get((byte) 2), get((byte) 3)};
    }

    private void cancel(){
        back.replace((byte)1, settings.isHorseSaddle());
        back.replace((byte)2, settings.isHorseArmor());
        back.replace((byte)3, settings.isHorseHeal());
        for(Byte b : back.keySet()){
            gui.getItem((byte)(4 + (b - 1) * 9)).setName(get(b));
            gui.update((byte)(4 + (b - 1) * 9));
        }
    }

    private void validate(){
        if(back.get((byte)1) != settings.isHorseSaddle())settings.setHorseSaddle(back.get((byte)1));
        if(back.get((byte)2) != settings.isHorseArmor())settings.setHorseArmor(back.get((byte)2));
        if(back.get((byte)3) != settings.isHorseHeal())settings.setHorseHeal(back.get((byte)3));
        gui.getParent().getItem((byte)43).setDescription(Collections.singletonList(getDescParent()));
        gui.getParent().update((byte)43);
    }

    private String get(byte id){
        switch(id){
            case 1: return "§6Selle: " + (back.get(id) ? "§aActivée" : "§cDésactivée");
            case 2: return "§6Armures: " + (back.get(id) ? "§aActivées" : "§cDésactivées");
            case 3: return "§6Heal: " + (back.get(id) ? "§aActivé" : "§cDésactivé");
        }
        return "";
    }

}
