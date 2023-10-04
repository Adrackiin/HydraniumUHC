package fr.adrackiin.hydranium.uhc.guis.host.scenarios;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.exceptions.APGuiNotFoundException;
import fr.adrackiin.hydranium.api.gui.*;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.scenarios.SoftBow;
import fr.adrackiin.hydranium.uhc.utils.MathUtils;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collections;

public class APGSoftBow extends APGuiListener implements APGCloseCustom {

    private APGui gui;
    private final SoftBow softBow = (SoftBow)(Game.getGame().getScenariosManager().getScenario(SoftBow.class));
    private int backRecharging = softBow.getRechargingTime();

    public APGSoftBow() {
        APGui parent;
        try {
            parent = APICore.getPlugin().getAPGuiManager().get("§cScénarios").getAPGui();
        } catch(APGuiNotFoundException e) {
            e.printStackTrace();
            return;
        }
        this.gui = new APGui("§cSoftBow", (byte)2, new APGuiItem[]{
                highLess.get(-5, (byte)1), less.get(-2, (byte)1), fewLess.get(-1, (byte)1), fewMore.get(1, (byte)1), more.get(2, (byte)1), highMore.get(5, (byte)1),
                cancel.get((byte) 12),
                validate.get((byte) 14),
                new APGuiItem(Material.BOW, (short)270, (byte)1, (byte)4, "§6Temps de rechargement: §e" + softBow.getRechargingTime(), null)
        }, parent, false, false);
        parent.addChildren(this.gui, (byte)21);
    }

    @Override
    public void onClick(APGuiClickEvent e) {
        APGuiItem item = gui.getItem(e.getSlot());
        if(item.getType() == Material.STAINED_CLAY){
            switch(1 + e.getSlot() / 9){
                case 1:
                    backRecharging = (int) MathUtils.surround(backRecharging += MathUtils.getValue(item.getName().substring(2)), 5, 45);
                    gui.getItem((byte) 4).setName("§6Temps de rechargement: §e" + backRecharging);
                    gui.update((byte) 4);
                    break;
            }
            return;
        }
        switch(e.getSlot()){
            case 12:
                cancel();
                gui.getParent().open(e.getPlayer());
                break;
            case 14:
                softBow.setRechargingTime(backRecharging);
                gui.getParent().getItem((byte) 21).setDescription(Collections.singletonList(softBow.getDescriptionHost()));
                gui.getParent().update((byte) 21);
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
        backRecharging = softBow.getRechargingTime();
        gui.getItem((byte) 4).setName("§6Temps de rechargement: §e" + backRecharging);
        gui.update((byte) 4);
    }
}
