package fr.adrackiin.hydranium.uhc.guis.host.scenarios;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.exceptions.APGuiNotFoundException;
import fr.adrackiin.hydranium.api.gui.*;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.scenarios.TimeBomb;
import fr.adrackiin.hydranium.uhc.utils.MathUtils;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collections;

public class APGTimeBomb extends APGuiListener implements APGCloseCustom {

    private APGui gui;
    private final TimeBomb timeBomb = (TimeBomb)(Game.getGame().getScenariosManager().getScenario(TimeBomb.class));
    private int backSecond = timeBomb.getSecond();
    private int backRadius = timeBomb.getRadius();

    public APGTimeBomb() {
        APGui parent;
        try {
            parent = APICore.getPlugin().getAPGuiManager().get("§cScénarios").getAPGui();
        } catch(APGuiNotFoundException e) {
            e.printStackTrace();
            return;
        }
        this.gui = new APGui("§cTimeBomb", (byte)3, new APGuiItem[]{
                highLess.get(-5, (byte)1), less.get(-2, (byte)1), fewLess.get(-1, (byte)1), fewMore.get(1, (byte)1), more.get(2, (byte)1), highMore.get(5, (byte)1),
                highLess.get(-5, (byte)2), less.get(-2, (byte)2), fewLess.get(-1, (byte)2), fewMore.get(1, (byte)2), more.get(2, (byte)2), highMore.get(5, (byte)2),
                cancel.get((byte) 21),
                validate.get((byte) 23),
                new APGuiItem(Material.CHEST, (short)0, (byte)1, (byte)4, "§6Secondes: §e" + timeBomb.getSecond(), null),
                new APGuiItem(Material.CHEST, (short)0, (byte)1, (byte)13, "§6Rayon: §e" + timeBomb.getRadius(), null)
        }, parent, false, false);
        parent.addChildren(this.gui, (byte)11);
    }

    @Override
    public void onClick(APGuiClickEvent e) {
        APGuiItem item = gui.getItem(e.getSlot());
       if(item.getType() == Material.STAINED_CLAY){
           switch(1 + e.getSlot() / 9){
               case 1:
                   backSecond = (int) MathUtils.surround(backSecond += MathUtils.getValue(item.getName().substring(2)), 5, 45);
                   gui.getItem((byte) 4).setName("§6Secondes: §e" + backSecond);
                   gui.update((byte) 4);
                   break;
               case 2:
                   backRadius = (int) MathUtils.surround(backRadius += MathUtils.getValue(item.getName().substring(2)), 1, 10);
                   gui.getItem((byte) 13).setName("§6Rayon: §e" + backRadius);
                   gui.update((byte) 13);
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
               timeBomb.setSecond(backSecond);
               timeBomb.setRadius(backRadius);
               gui.getParent().getItem((byte) 11).setDescription(Collections.singletonList(timeBomb.getDescriptionHost()));
               gui.getParent().update((byte) 11);
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

    private void cancel() {
        backSecond = timeBomb.getSecond();
        backRadius = timeBomb.getRadius();
        gui.getItem((byte) 4).setName("§6Secondes: §e" + backSecond);
        gui.update((byte) 4);
        gui.getItem((byte) 13).setName("§6Rayon: §e" + backRadius);
        gui.update((byte) 13);
    }
}
