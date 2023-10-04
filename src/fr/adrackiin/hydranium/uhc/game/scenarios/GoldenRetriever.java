package fr.adrackiin.hydranium.uhc.game.scenarios;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.UHCPlayerDeathEvent;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;

public class GoldenRetriever extends Scenario implements Listener {

    private final String loreGHead = "§c♥♥♥♥";
    private final ItemStack goldenHead = new ItemStack(Material.GOLDEN_APPLE, 1, (short)0);
    public GoldenRetriever(){
        super("GoldenRetriever",
                "Vous obtenez une Golden Head lors d'un kill",
                new String[]{"", "§7Vous obtenez une Golden", "§7Head lors d'un kill"});
        ItemMeta goldenHead = this.goldenHead.getItemMeta();
        goldenHead.setDisplayName("§bGolden Head");
        goldenHead.setLore(Collections.singletonList(loreGHead));
        this.goldenHead.setItemMeta(goldenHead);
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

    @EventHandler
    public void onDeath(UHCPlayerDeathEvent e){
        Location loc = e.getDeath().getAPPlayer().getPlayer().getLocation();
        loc.getWorld().dropItem(loc, goldenHead);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onConsumeGapple(PlayerItemConsumeEvent e){
        if(isItemGoldenHead(e.getItem())) {
            Player player = e.getPlayer();
            player.removePotionEffect(PotionEffectType.REGENERATION);
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10 * 20, 1));
        }
    }

    private boolean isItemGoldenHead(ItemStack item){
        if(item.getType() == Material.GOLDEN_APPLE) {
            return item.hasItemMeta() && item.getItemMeta().getLore().get(0).equals(this.loreGHead);
        }
        return false;
    }

}
