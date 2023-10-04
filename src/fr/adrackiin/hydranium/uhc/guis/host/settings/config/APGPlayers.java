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

public class APGPlayers extends APGuiListener implements APGCloseCustom {

    private APGui gui;
    private final Settings settings = Game.getGame().getSettings();
    private byte backTeam = settings.getTeam();
    private short backSlot = settings.getMaxPlayers();

    public APGPlayers() {
        APGui parent;
        try {
            parent = APICore.getPlugin().getAPGuiManager().get("§cConfiguration").getAPGui();
        } catch(APGuiNotFoundException e) {
            e.printStackTrace();
            return;
        }
        this.gui = new APGui("§cJoueurs", (byte)3, new APGuiItem[]{
                highLess.get(-5, (byte)1), less.get(-2, (byte)1), fewLess.get(-1, (byte)1), fewMore.get(1, (byte)1), more.get(2, (byte)1), highMore.get(5, (byte)1),
                highLess.get(-10, (byte)2), less.get(-5, (byte)2), fewLess.get(-1, (byte)2), fewMore.get(1, (byte)2), more.get(5, (byte)2), highMore.get(10, (byte)2),
                cancel.get((byte)21),
                validate.get((byte)23),
                new APGuiItem(Material.SKULL_ITEM, (short)3, (byte)1, (byte)4, getTeam(settings.getTeam()), null),
                new APGuiItem(Material.SKULL_ITEM, (short)3, (byte)1, (byte)13, getSlot(settings.getMaxPlayers()), null)
        }, parent, false, false);
        parent.addChildren(this.gui, (byte)10);
    }

    @Override
    public void onClick(APGuiClickEvent e) {
        APGuiItem item = gui.getItem(e.getSlot());
        if(item.getType() == Material.STAINED_CLAY){
            switch(1 + e.getSlot() / 9){
                case 1:
                    backTeam = (byte)MathUtils.surround(backTeam += MathUtils.getValue(item.getName().substring(2)), 1, 50);
                    gui.getItem((byte)4).setName(getTeam(backTeam));
                    gui.update((byte)4);
                    break;
                case 2:
                    backSlot = (short)MathUtils.surround(backSlot += MathUtils.getValue(item.getName().substring(2)), 10, 150);
                    gui.getItem((byte)13).setName(getSlot(backSlot));
                    gui.update((byte)13);
                    break;
            }
            return;
        }
        switch(e.getSlot()){
            case 21:
                cancel();
                gui.getParent().open(e.getPlayer());
                break;
            case 23:
                settings.setTeam(backTeam);
                settings.setMaxPlayers(backSlot);
                gui.getParent().getItem((byte)10).setDescription(Collections.singletonList(getDescParent(backTeam, backSlot)));
                gui.getParent().update((byte)10);
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

    private void cancel(){
        backTeam = settings.getTeam();
        backSlot = settings.getMaxPlayers();
        gui.getItem((byte)4).setName(getTeam(backTeam));
        gui.update((byte)4);
        gui.getItem((byte)13).setName(getSlot(backSlot));
        gui.update((byte)13);
    }

    private String getTeam(byte team){
        return "§6Team: §e" + (team == 1 ? "FFA" : "To" + team);
    }
    
    private String getSlot(short slot){
        return "§6Slot: §e" + slot;
    }

    private String[] getDescParent(byte team, short slot){
        return new String[]{"§6Team: §e" + (team == 1 ? "FFA" : "To" + team),
                "§6Slot: §e" + slot};
    }
}
