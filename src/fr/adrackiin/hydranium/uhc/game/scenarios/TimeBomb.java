package fr.adrackiin.hydranium.uhc.game.scenarios;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.exceptions.APGuiNotFoundException;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.UHCPlayerDeathEvent;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Configurable;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;
import fr.adrackiin.hydranium.uhc.utils.minecraft.Items;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TimeBomb extends Scenario implements Listener, Configurable {

    private int second;
    private int radius;

    private final Map<Block, Inventory> chests = new HashMap<>();

    public TimeBomb() {
        super(
                "TimeBomb",
                "Lorsqu'un joueur meurt, son stuff est entreposé dans un coffre qui explose sur un rayon de §65 blocs §7au bout de §625 secondes",
                new String[]{"", "§7Lorsqu'un joueur meurt", "§7son stuff est entreposé dans un coffre", "§7qui explose sur un rayon de §65 blocs", "§7au bout de §625 secondes"}
        );

        second = 25;
        radius = 5;
    }

    @Override
    public void setScenario(boolean status) {
        if(status){
            enable();
        } else {
            disable();
        }
        this.setActive(status);
    }

    @Override
    public void enable() {
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    @Override
    public void disable() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void configure(APPlayer player) {
        try {
            APICore.getPlugin().getAPGuiManager().get("§cTimeBomb").getAPGui().open(player);
        } catch(APGuiNotFoundException e) {
            e.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onDeath(UHCPlayerDeathEvent e) {
        Player player = e.getDeath().getAPPlayer().getPlayer();
        Location chest = e.getDeath().getAPPlayer().getLocation().getBlock().getLocation();
        Inventory stuff = Bukkit.createInventory(null, 54, "§a" + player.getName());
        ItemStack[] loots = e.getLoots();
        e.setKeepInventory(true);
        chest.getBlock().setType(Material.TRAPPED_CHEST);
        chest.getBlock().getRelative(BlockFace.NORTH).setType(Material.TRAPPED_CHEST);
        chests.put(chest.getBlock(), stuff);
        chests.put(chest.getBlock().getRelative(BlockFace.NORTH), stuff);
        for (int i = 0; i < loots.length; i++) {
            stuff.setItem(i, loots[i]);
        }
        for(int i = 0; i < 4; i ++){
            stuff.setItem(i + 36, e.getArmorLoots()[i]);
        }
        if (Game.getGame().getSettings().isGapple()) {
            stuff.setItem(firstEmpty(stuff), new ItemStack(Material.GOLDEN_APPLE));
        }
        if (Game.getGame().getSettings().isGhead()) {
            stuff.setItem(firstEmpty(stuff), Items.getItem(Material.GOLDEN_APPLE, "§6" + "Golden Head", 1, 0, Arrays.asList(
                    "",
                    "§b" + "Les Têtes d'or",
                    "§b" + "régénèrent de 4 coeurs")));
        }
        if (Game.getGame().getSettings().isDropHead()) {
            stuff.setItem(firstEmpty(stuff), Items.getHead(player.getName(), 1, null, player.getName()));
        }
        ArmorStand temp = (ArmorStand) chest.getWorld().spawnEntity(chest.clone().add(0.5D, 0.8D, 0), EntityType.ARMOR_STAND);
        temp.setGravity(false);
        temp.setVisible(false);
        temp.setMarker(true);
        temp.setCustomNameVisible(true);

        ArmorStand temp2 = (ArmorStand) chest.getWorld().spawnEntity(chest.clone().add(0.5D, 1.1D, 0), EntityType.ARMOR_STAND);
        temp2.setCustomName("§f" + e.getDeath().getAPPlayer().getName());
        temp2.setGravity(false);
        temp2.setVisible(false);
        temp2.setMarker(true);
        temp2.setCustomNameVisible(true);

        new Timer(second, temp2, temp, chest.getBlock(), stuff);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onOpenChest(PlayerInteractEvent e){
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK){
            return;
        }
        if(e.getClickedBlock() == null){
            return;
        }
        if(e.getClickedBlock().getType() != Material.TRAPPED_CHEST){
            return;
        }
        if(chests.containsKey(e.getClickedBlock())){
            e.setCancelled(true);
            e.getPlayer().openInventory(chests.get(e.getClickedBlock()));
            e.getPlayer().playSound(e.getClickedBlock().getLocation(), Sound.CHEST_OPEN, 1F, 1F);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCloseChest(InventoryCloseEvent e){
        if(e.getPlayer() instanceof Player) {
            if (chests.containsValue(e.getInventory())) {
                ((Player) e.getPlayer()).playSound(e.getPlayer().getLocation(), Sound.CHEST_CLOSE, 1F, 1F);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBreakChest(BlockBreakEvent e){
        if(e.getBlock().getType() != Material.TRAPPED_CHEST){
            return;
        }
        if(chests.containsKey(e.getBlock())){
            e.setCancelled(true);
        }
    }

    public void remove(Block block){
        if(chests.containsKey(block) && chests.containsKey(block.getRelative(BlockFace.NORTH))) {
            chests.remove(block);
            chests.remove(block.getRelative(BlockFace.NORTH));
        }
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
        this.setDescription("Lorsqu'un joueur meurt, son stuff est entreposé dans un coffre qui explose sur un rayon de §6" + radius + " blocs §7au bout de §6" + second + " secondes");
        this.setDescriptionHost(new String[]{"", "§7Lorsqu'un joueur meurt", "§7son stuff est entreposé dans un coffre", "§7qui explose sur un rayon de §6" + radius + " blocs", "§7au bout de §6" + second + " secondes"});

    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        this.setDescription("Lorsqu'un joueur meurt, son stuff est entreposé dans un coffre qui explose sur un rayon de §6" + radius + " blocs §7au bout de §6" + second + " secondes");
        this.setDescriptionHost(new String[]{"", "§7Lorsqu'un joueur meurt", "§7son stuff est entreposé dans un coffre", "§7qui explose sur un rayon de §6" + radius + " blocs", "§7au bout de §6" + second + " secondes"});
    }

    private int firstEmpty(Inventory inv){
        if(inv.getSize() < 36){
            return inv.getSize() - 1;
        }
        for(int i = 36; i < inv.getSize(); i ++){
            if(inv.getItem(i) == null) {
                return i;
            }
        }
        return inv.getSize() - 1;
    }

    private class Timer implements Runnable {

        private final int id;
        private int timeLeft;
        private final ArmorStand displayName;
        private final ArmorStand displayTimer;
        private final Block chest;
        private final Inventory stuff;

        public Timer(int timeLeft, ArmorStand displayName, ArmorStand displayTimer, Block chest, Inventory stuff) {
            this.timeLeft = timeLeft;
            this.displayName = displayName;
            this.displayTimer = displayTimer;
            this.chest = chest;
            this.stuff = stuff;
            id = Bukkit.getScheduler().scheduleSyncRepeatingTask(UHCCore.getPlugin(), this, 0L, 20L);
        }

        @Override
        public void run() {
            if (timeLeft <= 0) {
                displayTimer.remove();
                displayName.remove();
                chest.setType(Material.AIR);
                chest.getRelative(BlockFace.NORTH).setType(Material.AIR);
                ArrayList<HumanEntity> viewers = new ArrayList<>(stuff.getViewers());
                for (HumanEntity human : viewers) {
                    human.closeInventory();
                }
                remove(chest);
                chest.getWorld().createExplosion(chest.getX(), chest.getY(), chest.getZ(), getRadius(), false, true);
                stop();
                return;
            }
            if (timeLeft == 1) {
                chest.getWorld().playSound(chest.getLocation(), Sound.CLICK, 1.0F, 1.3F);
                displayTimer.setCustomName("§4" + timeLeft + "s");
            } else if (timeLeft == 2) {
                chest.getWorld().playSound(chest.getLocation(), Sound.CLICK, 1.0F, 1.2F);
                displayTimer.setCustomName("§c" + timeLeft + "s");
            } else if (timeLeft == 3) {
                chest.getWorld().playSound(chest.getLocation(), Sound.CLICK, 1.0F, 1.1F);
                displayTimer.setCustomName("§6" + timeLeft + "s");
            } else if (timeLeft <= 5) {
                chest.getWorld().playSound(chest.getLocation(), Sound.CLICK, 1.0F, 1.0F);
                displayTimer.setCustomName("§e" + timeLeft + "s");
            } else if (timeLeft <= 15) {
                displayTimer.setCustomName("§a" + timeLeft + "s");
            } else {
                displayTimer.setCustomName("§2" + timeLeft + "s");
            }
            timeLeft -= 1;
        }

        private void stop(){
            Bukkit.getScheduler().cancelTask(id);
        }

    }
}
