package fr.adrackiin.hydranium.uhc.guis.host;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.exceptions.APGuiNotFoundException;
import fr.adrackiin.hydranium.api.gui.APGui;
import fr.adrackiin.hydranium.api.gui.APGuiClickEvent;
import fr.adrackiin.hydranium.api.gui.APGuiItem;
import fr.adrackiin.hydranium.api.gui.APGuiListener;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.scenarios.*;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Configurable;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;
import fr.adrackiin.hydranium.uhc.utils.PubSub;
import org.bukkit.Material;

import java.util.HashMap;

public class APGScenarios extends APGuiListener {

    private APGui gui;
    private final HashMap<Byte, Scenario> scenarios = new HashMap<>();

    public APGScenarios() {
        scenarios.put((byte)10, Game.getGame().getScenariosManager().getScenario(CutClean.class));
        scenarios.put((byte)11, Game.getGame().getScenariosManager().getScenario(TimeBomb.class));
        scenarios.put((byte)12, Game.getGame().getScenariosManager().getScenario(FireLess.class));
        scenarios.put((byte)13, Game.getGame().getScenariosManager().getScenario(NoBow.class));
        scenarios.put((byte)14, Game.getGame().getScenariosManager().getScenario(NoRod.class));
        scenarios.put((byte)15, Game.getGame().getScenariosManager().getScenario(VeinMiner.class));
        scenarios.put((byte)16, Game.getGame().getScenariosManager().getScenario(HasteyBoys.class));
        scenarios.put((byte)19, Game.getGame().getScenariosManager().getScenario(Timber.class));
        scenarios.put((byte)20, Game.getGame().getScenariosManager().getScenario(KeepLighting.class));
        scenarios.put((byte)21, Game.getGame().getScenariosManager().getScenario(SoftBow.class));
        scenarios.put((byte)22, Game.getGame().getScenariosManager().getScenario(SuperHeroes.class));
        scenarios.put((byte)23, Game.getGame().getScenariosManager().getScenario(Kreinzinator.class));
        scenarios.put((byte)24, Game.getGame().getScenariosManager().getScenario(Permakill.class));
        scenarios.put((byte)25, Game.getGame().getScenariosManager().getScenario(NoEnchant.class));
        scenarios.put((byte)28, Game.getGame().getScenariosManager().getScenario(KillEffect.class));
        scenarios.put((byte)29, Game.getGame().getScenariosManager().getScenario(LavaLess.class));
        scenarios.put((byte)30, Game.getGame().getScenariosManager().getScenario(BloodEnchant.class));
        scenarios.put((byte)31, Game.getGame().getScenariosManager().getScenario(GoldenRetriever.class));
        scenarios.put((byte)32, Game.getGame().getScenariosManager().getScenario(FlowerPower.class));
        APGui parent;
        try {
            parent = APICore.getPlugin().getAPGuiManager().get("§cHost").getAPGui();
        } catch(APGuiNotFoundException e) {
            e.printStackTrace();
            return;
        }
        this.gui = new APGui("§cScénarios", (byte)6, new APGuiItem[]{
                new APGuiItem(Material.BLAZE_POWDER, (short)0, (byte)1, (byte)10, "§c" + scenarios.get((byte)10).getName(), scenarios.get((byte)10).getDescriptionHost()),
                new APGuiItem(Material.CHEST, (short)0, (byte)1, (byte)11, "§c" + scenarios.get((byte)11).getName(), scenarios.get((byte)11).getDescriptionHost()),
                new APGuiItem(Material.FLINT_AND_STEEL, (short)0, (byte)1, (byte)12, "§c" + scenarios.get((byte)12).getName(), scenarios.get((byte)12).getDescriptionHost()),
                new APGuiItem(Material.BOW, (short)0, (byte)1, (byte)13, "§c" + scenarios.get((byte)13).getName(), scenarios.get((byte)13).getDescriptionHost()),
                new APGuiItem(Material.FISHING_ROD, (short)0, (byte)1, (byte)14, "§c" + scenarios.get((byte)14).getName(), scenarios.get((byte)14).getDescriptionHost()),
                new APGuiItem(Material.IRON_PICKAXE, (short)0, (byte)1, (byte)15, "§c" + scenarios.get((byte)15).getName(), scenarios.get((byte)15).getDescriptionHost()),
                new APGuiItem(Material.DIAMOND_PICKAXE, (short)0, (byte)1, (byte)16, "§c" + scenarios.get((byte)16).getName(), scenarios.get((byte)16).getDescriptionHost()),
                new APGuiItem(Material.IRON_AXE, (short)0, (byte)1, (byte)19, "§c" + scenarios.get((byte)19).getName(), scenarios.get((byte)19).getDescriptionHost()),
                new APGuiItem(Material.TORCH, (short)0, (byte)1, (byte)20, "§c" + scenarios.get((byte)20).getName(), scenarios.get((byte)20).getDescriptionHost()),
                new APGuiItem(Material.BOW, (short)270, (byte)1, (byte)21, "§c" + scenarios.get((byte)21).getName(), scenarios.get((byte)21).getDescriptionHost()),
                new APGuiItem(Material.BEACON, (short)0, (byte)1, (byte)22, "§c" + scenarios.get((byte)22).getName(), scenarios.get((byte)22).getDescriptionHost()),
                new APGuiItem(Material.REDSTONE_BLOCK, (short)0, (byte)1, (byte)23, "§c" + scenarios.get((byte)23).getName(), scenarios.get((byte)23).getDescriptionHost()),
                new APGuiItem(Material.WATCH, (short)0, (byte)1, (byte)24, "§c" + scenarios.get((byte)24).getName(), scenarios.get((byte)24).getDescriptionHost()),
                new APGuiItem(Material.ENCHANTMENT_TABLE, (short)0, (byte)1, (byte)25, "§c" + scenarios.get((byte)25).getName(), scenarios.get((byte)25).getDescriptionHost()),
                new APGuiItem(Material.POTION, (short)8194, (byte)1, (byte)28, "§c" + scenarios.get((byte)28).getName(), scenarios.get((byte)28).getDescriptionHost()),
                new APGuiItem(Material.LAVA_BUCKET, (short)0, (byte)1, (byte)29, "§c" + scenarios.get((byte)29).getName(), scenarios.get((byte)29).getDescriptionHost()),
                new APGuiItem(Material.ENCHANTMENT_TABLE, (short)0, (byte)1, (byte)30, "§c" + scenarios.get((byte)30).getName(), scenarios.get((byte)30).getDescriptionHost()),
                new APGuiItem(Material.GOLDEN_APPLE, (short)0, (byte)1, (byte)31, "§c" + scenarios.get((byte)31).getName(), scenarios.get((byte)31).getDescriptionHost()),
                new APGuiItem(Material.YELLOW_FLOWER, (short)0, (byte)1, (byte)32, "§c" + scenarios.get((byte)32).getName(), scenarios.get((byte)32).getDescriptionHost())
        }, parent, true, true);
        parent.addChildren(this.gui, (byte)11);
    }

    /*WEREWOLVES(
    SUPERHEROES(
    PYROPHOBIA(
    CORRUPTEDWORLD(
    SKYHIGH(*/

    @Override
    public void onClick(APGuiClickEvent e) {
        Scenario sc = scenarios.get(e.getSlot());
        if(sc == null) return;
        if(e.isClickConfigure()){
            if(sc.getSource() instanceof Configurable){
                ((Configurable) sc.getSource()).configure(e.getPlayer());
                if(sc.isActive()){
                    return;
                }
            }
        }
        sc.setScenario(!sc.isActive());
        if(sc.isActive()){
            Game.getGame().getScenarios().add(sc);
            PubSub.addScenario(sc);
        } else {
            Game.getGame().getScenarios().remove(sc);
            PubSub.removeScenario(sc);
        }
        this.gui.getItem(e.getSlot()).setGlow(sc.isActive());
        this.gui.getItem(e.getSlot()).setName((sc.isActive() ? "§b" : "§c") + sc.getName());
        this.gui.update(e.getSlot());
    }

    @Override
    public APGui getAPGui() {
        return gui;
    }
}
