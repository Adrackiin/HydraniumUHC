package fr.adrackiin.hydranium.uhc.game.scenarios;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.GameStateChangeEvent;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;
import fr.adrackiin.hydranium.uhc.game.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class Kreinzinator extends Scenario implements Listener {

    public Kreinzinator(){
        super("Kreinzinator",
                "Craftez un diamant avec 9 blocks de redstone",
                new String[]{"", "§7Craftez un diamant", "§7avec 9 blocks de redstone"});
    }

    @Override
    public void setScenario(boolean status){
        if(status){
            enable();
        } else {
            disable();
        }
        this.setActive(status);
    }

    @Override
    public void enable(){
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    @Override
    public void disable(){
        HandlerList.unregisterAll(this);
    }

    /**
     * When Event is triggered, register new recipe, 9 blocks of redstone = 1 diamond
     *
     * @param e Event of game start
     */
    @EventHandler
    public void onStartUHC(GameStateChangeEvent e){
        if(e.getNewGameState() == GameState.START){
            ShapedRecipe kreinzinator = new ShapedRecipe(new ItemStack(Material.DIAMOND));
            kreinzinator.shape("RRR", "RRR", "RRR");
            kreinzinator.setIngredient('R', Material.REDSTONE_BLOCK);
            Bukkit.getServer().addRecipe(kreinzinator);
        }
    }
}
