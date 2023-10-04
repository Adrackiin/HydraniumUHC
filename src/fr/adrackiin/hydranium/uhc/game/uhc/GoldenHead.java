package fr.adrackiin.hydranium.uhc.game.uhc;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.GameStateChangeEvent;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.settings.Configurable;
import fr.adrackiin.hydranium.uhc.game.state.GameState;
import fr.adrackiin.hydranium.uhc.utils.minecraft.Items;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class GoldenHead extends Configurable implements Listener {

    private final String[] loreGHead = new String[]{"",
            "§bLes Têtes d'or",
            "§brégénèrent de 4 coeurs"};

    public GoldenHead(){
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    public void activate() {}

    public void deactivate() {}

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onStart(GameStateChangeEvent e){
        if(e.getNewGameState() == GameState.INVULNERABILITY){
            if(Game.getGame().getSettings().isCraftGoldenHead()){
                ShapedRecipe goldenHead = new ShapedRecipe(Items.getItem(Material.GOLDEN_APPLE, "§6" + "Golden Head", 1, 0, Arrays.asList(loreGHead)));
                goldenHead.shape("GGG", "GHG", "GGG");
                goldenHead.setIngredient('G', Material.GOLD_INGOT);
                goldenHead.setIngredient('H', Material.SKULL_ITEM, 3);
                UHCCore.getPlugin().getServer().addRecipe(goldenHead);
            } else {
                HandlerList.unregisterAll(this);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onConsumeGapple(PlayerItemConsumeEvent e){
        if(isItemGoldenHead(e.getItem())) {
            Player player = e.getPlayer();
            player.removePotionEffect(PotionEffectType.ABSORPTION);
            player.removePotionEffect(PotionEffectType.REGENERATION);
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 120 * 20, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10 * 20, 1));
            APICore.getPlugin().getAPPlayer(e.getPlayer().getUniqueId()).getStats().add("ghead-eaten");
        }
    }

    public boolean isItemGoldenHead(ItemStack item){
        if(item.getType() == Material.GOLDEN_APPLE) {
                return item.hasItemMeta() && isItemLoreGoldenHead(item.getItemMeta().getLore());
        }
        return false;
    }

    private boolean isItemLoreGoldenHead(List<String> desc){
        Iterator<String> test = desc.iterator();
        for(String lore : loreGHead){
            if(!test.next().equals(lore)){
                return false;
            }
        }
        return true;
    }

}
