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

public class APGTime extends APGuiListener implements APGCloseCustom {

    private APGui gui;
    private final Settings settings = Game.getGame().getSettings();
    private final HashMap<Byte, Object> back = new HashMap<>();

    public APGTime() {
        back.put((byte)1, settings.getInvulnerability());
        back.put((byte)2, settings.getFinalHeal());
        back.put((byte)3, settings.getPvp());
        back.put((byte)4, settings.getBorder());
        back.put((byte)5, settings.getMeetup());
        APGui parent;
        try {
            parent = APICore.getPlugin().getAPGuiManager().get("§cConfiguration").getAPGui();
        } catch(APGuiNotFoundException e) {
            e.printStackTrace();
            return;
        }
        this.gui = new APGui("§cTemps", (byte)6, new APGuiItem[]{
                highLess.get(-10, (byte)1), less.get(-5, (byte)1), fewLess.get(-1, (byte)1), fewMore.get(1, (byte)1), more.get(5, (byte)1), highMore.get(10, (byte)1),
                highLess.get(-5, (byte)2), less.get(-2, (byte)2), fewLess.get(-1, (byte)2), fewMore.get(1, (byte)2), more.get(2, (byte)2), highMore.get(5, (byte)2),
                highLess.get(-10, (byte)3), less.get(-5, (byte)3), fewLess.get(-1, (byte)3), fewMore.get(1, (byte)3), more.get(5, (byte)3), highMore.get(10, (byte)3),
                highLess.get(-10, (byte)4), less.get(-5, (byte)4), fewLess.get(-1, (byte)4), fewMore.get(1, (byte)4), more.get(5, (byte)4), highMore.get(10, (byte)4),
                highLess.get(-10, (byte)5), less.get(-5, (byte)5), fewLess.get(-1, (byte)5), fewMore.get(1, (byte)5), more.get(5, (byte)5), highMore.get(10, (byte)5),
                cancel.get((byte)48),
                validate.get((byte)50),
                new APGuiItem(Material.WATCH, (short)0, (byte)1, (byte)4, get((byte)1, back.get((byte)1)), null),
                new APGuiItem(Material.WATCH, (short)0, (byte)1, (byte)13, get((byte)2, back.get((byte)2)), null),
                new APGuiItem(Material.WATCH, (short)0, (byte)1, (byte)22, get((byte)3, back.get((byte)3)), null),
                new APGuiItem(Material.WATCH, (short)0, (byte)1, (byte)31, get((byte)4, back.get((byte)4)), null),
                new APGuiItem(Material.WATCH, (short)0, (byte)1, (byte)40, get((byte)5, back.get((byte)5)), null)
        }, parent, false, false);
        parent.addChildren(this.gui, (byte)11);
    }

    @Override
    public void onClick(APGuiClickEvent e) {
        APGuiItem item = gui.getItem(e.getSlot());
        if(item.getType() == Material.STAINED_CLAY){
            byte b = (byte)(1 + e.getSlot() / 9);
            switch(b){
                case 1: back.replace(b, (byte)MathUtils.surround((byte)(back.get(b)) + MathUtils.getValue(item.getName().substring(2)), 5, 60));break;
                case 2: back.replace(b, (byte)MathUtils.surround((byte)(back.get(b)) + MathUtils.getValue(item.getName().substring(2)), 2, (short)(back.get((byte)3)) - 1));break;
                case 3: back.replace(b, (short)MathUtils.surround((short)(back.get(b)) + MathUtils.getValue(item.getName().substring(2)), (byte)(back.get((byte)2)) + 1, (short)(back.get((byte)4)) - 1));break;
                case 4: back.replace(b, (short)MathUtils.surround((short)(back.get(b)) + MathUtils.getValue(item.getName().substring(2)), (short)(back.get((byte)3)) + 1, 120));break;
                case 5: back.replace(b, (short)MathUtils.surround((short)(back.get(b)) + MathUtils.getValue(item.getName().substring(2)), 5, 120));break;
            }
            gui.getItem((byte)(4 + (b - 1) * 9)).setName(get(b, back.get(b)));
            gui.update((byte)(4 + (b - 1) * 9));
            return;
        }
        switch(e.getSlot()){
            case 48:
                cancel();
                gui.getParent().open(e.getPlayer());
                break;
            case 50:
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
                get((byte) 3, back.get((byte) 3)),
                get((byte) 4, back.get((byte) 4)),
                get((byte) 5, back.get((byte) 5))};
    }

    private void cancel(){
        back.replace((byte)1, settings.getInvulnerability());
        back.replace((byte)2, settings.getFinalHeal());
        back.replace((byte)3, settings.getPvp());
        back.replace((byte)4, settings.getBorder());
        back.replace((byte)5, settings.getMeetup());
        for(Byte b : back.keySet()){
            gui.getItem((byte)(4 + (b - 1) * 9)).setName(get(b, back.get(b)));
            gui.update((byte)(4 + (b - 1) * 9));
        }
    }

    private void validate(){
        if((byte)back.get((byte)1) != settings.getInvulnerability())settings.setInvulnerability((byte)back.get((byte)1));
        if((byte)back.get((byte)2) != settings.getFinalHeal())settings.setFinalHeal((byte)back.get((byte)2));
        if((short)back.get((byte)3) != settings.getPvp())settings.setPvp((short)back.get((byte)3));
        if((short)back.get((byte)4) != settings.getBorder())settings.setBorder((short)back.get((byte)4));
        if((short)back.get((byte)5) != settings.getMeetup())settings.setMeetup((short)back.get((byte)5));
        gui.getParent().getItem((byte)11).setDescription(Collections.singletonList(getDescParent()));
        gui.getParent().update((byte)11);
    }

    private String get(byte id, Object value){
        switch(id){
            case 1: return "§6Invulnérabilité: §e" + value + "§7 secondes";
            case 2: return "§6Final Heal: §e" + value + "§7 minutes";
            case 3: return "§6PvP: §e" + value + "§7 minutes";
            case 4: return "§6Bordure: §e" + value + "§7 minutes";
            case 5: return "§6Temps de réduction: §e" + value + "§7 minutes";
        }
        return "";
    }

    private String[] getDescParent(byte invulnerability, byte finalheal, short pvp, short border, short meetup){
        return new String[]{"",
                get((byte)1, invulnerability),
                get((byte)2, finalheal),
                get((byte)3, pvp),
                get((byte)4, border),
                get((byte)5, meetup)};
    }
}
