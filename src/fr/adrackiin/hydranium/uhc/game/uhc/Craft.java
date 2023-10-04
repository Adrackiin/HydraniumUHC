package fr.adrackiin.hydranium.uhc.game.uhc;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.utils.minecraft.Items;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class Craft implements Listener {

    public Craft(){
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
        ShapedRecipe speckledMelon = new ShapedRecipe(Items.simpleItem(Material.SPECKLED_MELON,(byte)0));
        speckledMelon.shape("GGG", "GHG", "GGG");
        speckledMelon.setIngredient('G', Material.GOLD_INGOT);
        speckledMelon.setIngredient('H', Material.MELON);
        UHCCore.getPlugin().getServer().addRecipe(speckledMelon);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCraftGoldenMelon(PrepareItemCraftEvent e){
        if(e.getRecipe().getResult().getType() == Material.SPECKLED_MELON && e.getInventory().contains(Material.GOLD_NUGGET)){
            e.getInventory().setResult(new ItemStack(Material.AIR));
        }
    }
}
