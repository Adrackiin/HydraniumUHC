package fr.adrackiin.hydranium.uhc.game.core.players;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.channel.Channel;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.events.AccountLoadedEvent;
import fr.adrackiin.hydranium.api.exceptions.APGuiNotFoundException;
import fr.adrackiin.hydranium.api.exceptions.CanTakeTimeException;
import fr.adrackiin.hydranium.api.exceptions.ChannelNotFoundException;
import fr.adrackiin.hydranium.api.utils.APText;
import fr.adrackiin.hydranium.commons.Rank;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.UHCPlayerBreakBlockEvent;
import fr.adrackiin.hydranium.uhc.events.UHCPlayerDeathEvent;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.settings.Settings;
import fr.adrackiin.hydranium.uhc.game.team.GameTeam;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import fr.adrackiin.hydranium.uhc.utils.minecraft.Blocks;
import fr.adrackiin.hydranium.uhc.utils.minecraft.Items;
import fr.adrackiin.hydranium.uhc.utils.minecraft.Mobs;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PlayerManager implements Listener{

    public PlayerManager(){
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(AccountLoadedEvent e){
        UHCPlayer player = Game.getGame().getUHCPlayer(e.getPlayer().getUUID());
        this.setDefaultPermissions(player.getAPPlayer());
        if(!Game.getGame().hasGameStart()){
            player.getAPPlayer().reset(new Location(Game.getGame().getWorldUHC().getWorld(), Game.getGame().getWorldUHC().getX() + 0.5, Game.getGame().getWorldUHC().getY() + 5.0, Game.getGame().getWorldUHC().getZ() + 0.5, 0L, 0L));
            player.getAPPlayer().fixVisibility();
            try {
                APICore.getPlugin().getAPGuiManager().get("container.inventory").getAPGui().open(player.getAPPlayer());
            } catch(APGuiNotFoundException e1){
                e1.printStackTrace();
            }
        }
        player.createScoreboard();
        if(Game.getGame().isGameEmpty() || player.isHost()){
            player.setHost(true);
            return;
        }
        if(player.isDead()){
            player.getAPPlayer().reset(new Location(Game.getGame().getWorldUHC().getWorld(), Game.getGame().getWorldUHC().getX() + 0.5, Game.getGame().getWorldUHC().getY() + 5.0, Game.getGame().getWorldUHC().getZ() + 0.5, 0L, 0L));
            if(player.getAPPlayer().hasPermission("hydranium.uhc.spec")){
                player.setSpectator();
            } else {
                Game.getGame().getTeamManager().setDeadReconnect(player);
                try {
                    APICore.getPlugin().getChannelManager().getChannel("channel.dead").addPlayer(player.getAPPlayer().getUUID());
                } catch(ChannelNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
            return;
        }
        if(player.isPlaying() || !Game.getGame().hasGameStart()){
            player.setPlaying();
        }
        if(Game.getGame().isGameWhitelist()){
            player.setWhitelisted(true);
        }
        Bukkit.broadcastMessage("§8[§a+§8]§7 " + player.getAPPlayer().getName() + "§8 [§3" + Game.getGame().getAlivePlayers().getSize() + "§7/§3" + Game.getGame().getSettings().getMaxPlayers() + "§8]");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        event.setQuitMessage(null);
        UHCPlayer player = Game.getGame().getUHCPlayer(event.getPlayer().getUniqueId());
        player.setDecoGameState(Game.getGame().getGameStateManager().getGameState());
        if(Game.getGame().isGameWhitelist()){
            player.setWhitelisted(false);
        }
        player.destroyScoreboard();
        if(player.isPlaying()) {
            player.savePlayer();
            Game.getGame().removePlayingPlayer(player.getAPPlayer().getUUID());
            Bukkit.broadcastMessage("§8[§c-§8]§7 " + player.getAPPlayer().getName() + "§8 [§3" + (Game.getGame().getAlivePlayers().getSize()) + "§7/§3" + Game.getGame().getSettings().getMaxPlayers() + "§8]");
            if(Game.getGame().hasGameStart()){
                player.addOfflinePlayer();
                player.startOfflineDeath();
            }
            if(player.hasTeam() && !Game.getGame().hasGameStart()){
                GameTeam team = player.getGameTeam();
                if(team.willEmpty(player.getAPPlayer().getUUID())){
                    team.removePlayer(player.getAPPlayer().getUUID(), false);
                }
            }
        }
        if(!Game.getGame().hasGameStart()){
            player.setStatus(null);
        }
        if(Game.getGame().isGameWillEmpty()){
            player.setHost(false);
            Game.getGame().reset();
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent e){
        if(!(e.getEntity() instanceof Player)){
            return;
        }
        UHCPlayer player = Game.getGame().getUHCPlayer(e.getEntity().getUniqueId());
        if(!player.isPlaying() || player.getAPPlayer().isGameMode(GameMode.CREATIVE, GameMode.SPECTATOR)){
            if(e.getCause() == EntityDamageEvent.DamageCause.VOID){
                Location loc = player.getAPPlayer().getLocation();
                player.getAPPlayer().teleport(new Location(loc.getWorld(), loc.getX(), loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()) + 0.5D, loc.getZ()));
            }
            e.setCancelled(true);
            return;
        }
        if(player.getAPPlayer().getHealth() <= e.getFinalDamage()){
            //if((int)(System.currentTimeMillis() / 1_000L) - uhcPlayer.getLastDamage() > 30){
            //    uhcPlayer.setLastDamager(null);
            //}
            player.kill((player.getLastDamager() == null ? null : Game.getGame().getUHCPlayer(player.getLastDamager())), player.getLastEntityAttack(), false, e.getCause(), null, false, player.getAPPlayer().getInventory().getContents(), player.getAPPlayer().getInventory().getArmorContents(), true);
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onDamageByEntity(EntityDamageByEntityEvent e){
        if(!(e.getEntity() instanceof Player)) {
            return;
        }
        UHCPlayer player = Game.getGame().getUHCPlayer(e.getEntity().getUniqueId());
        if (e.getDamager() instanceof Projectile && ((Projectile) e.getDamager()).getShooter() instanceof Player){
            UHCPlayer damager = Game.getGame().getUHCPlayer(((Player) ((Projectile) e.getDamager()).getShooter()).getUniqueId());
            if(!damager.isPlaying()){
                e.setCancelled(true);
                return;
            }
        }
        if(e.getDamager() instanceof Player){
            UHCPlayer damager = Game.getGame().getUHCPlayer(e.getDamager().getUniqueId());
            if(!damager.isPlaying()){
                e.setCancelled(true);
                return;
            }
            player.setLastDamager(damager.getAPPlayer().getUUID());
            player.setLastDamage((int) (System.currentTimeMillis() / 1_000L));
            player.setLastEntityAttack(null);
        } else if(e.getDamager() instanceof Projectile){
            if(((Projectile) e.getDamager()).getShooter() instanceof Player){
                UHCPlayer damager = Game.getGame().getUHCPlayer(((Player) ((Projectile) e.getDamager()).getShooter()).getUniqueId());
                player.setLastDamager(damager.getAPPlayer().getUUID());
                player.setLastEntityAttack(null);
            } else {
                player.setLastEntityAttack(((Entity)((Projectile)e.getDamager()).getShooter()).getType());
            }
        } else {
            player.setLastEntityAttack(e.getDamager().getType());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onDeath(UHCPlayerDeathEvent e){
        UHCPlayer player = e.getDeath();
        UHCPlayer killer = e.getKiller();
        player.getAPPlayer().getStats().add("death");
        System.out.println(Game.getGame().getTimer().getTimer());
        player.getAPPlayer().getStats().add("play-time", Game.getGame().getTimer().getTimer());
        String deathMessage = e.getDeathMessage();
        Bukkit.broadcastMessage(Prefix.kill + "§f" + deathMessage);
        player.setDead();
        if(!Game.getGame().isSwitchSc()){
            Game.getGame().startSwitchScoreboard();
        }
        if(killer == null){
            Game.getGame().addPveDeaths(1);
        } else {
            player.setKiller(killer.getAPPlayer().getUUID());
            killer.addKill(1);
        }
        if(!e.isKeepInventory()){
            if(Game.getGame().getSettings().isGapple()){
                Game.getGame().dropGapple(player.getOfflineLocation());
            }
            if(Game.getGame().getSettings().isGhead()){
                Game.getGame().dropGhead(player.getOfflineLocation());
            }
            if(Game.getGame().getSettings().isDropHead()){
                if(Game.getGame().getSettings().howDropHead() == Settings.DropHead.FENCE) {
                    Block block = player.getOfflineLocation().getBlock();
                    if(block.getBiome() == Biome.HELL){
                        block.setType(Material.NETHER_FENCE);
                    } else {
                        block.setType(Material.FENCE);
                    }
                    block.getRelative(BlockFace.UP).setType(Material.SKULL);
                    Blocks.head(block, player.getAPPlayer().getName(), player.getOfflineLocation());
                } else {
                    ItemStack item = Items.getHead(player.getAPPlayer().getName(), 1, null, player.getAPPlayer().getName());
                    player.getOfflineLocation().getWorld().dropItem(player.getOfflineLocation(), item);
                }
            }
            for(ItemStack item : e.getLoots()){
                if(item != null && item.getType() != Material.AIR) {
                    player.getOfflineLocation().getWorld().dropItem(player.getOfflineLocation(), item);
                }
            }
            for(ItemStack item : e.getArmorLoots()){
                if(item != null && item.getType() != Material.AIR) {
                    player.getOfflineLocation().getWorld().dropItem(player.getOfflineLocation(), item);
                }
            }
        }
        Game.getGame().switchToKills();
        Game.getGame().checkVictory();
    }

    @EventHandler
    public void onPickItem(PlayerPickupItemEvent e){
        UHCPlayer player = Game.getGame().getUHCPlayer(e.getPlayer().getUniqueId());
        if(player.isHost()){
            return;
        }
        if(!player.isPlaying() || !Game.getGame().hasGameStart()){
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent e){
        UHCPlayer player = Game.getGame().getUHCPlayer(e.getPlayer().getUniqueId());
        String message = e.getMessage();
        if(player.getAPPlayer().hasPermission("hydranium.api.chat.color")){
            message = message.replaceAll("&", "§");
        }
        boolean generalChat = message.substring(0, 1).equalsIgnoreCase("!");
        if(player.isDead()){
            try {
                APICore.getPlugin().getChannelManager().getChannel("channel.dead").sendMessage("§7" + player.getAPPlayer().getName() + " §8»§7 " + message);
                e.setCancelled(true);
                return;
            } catch(ChannelNotFoundException e1) {
                e1.printStackTrace();
                player.getAPPlayer().sendMessage("§cUne erreur est survenu");
                return;
            }
        }
        if(player.isSpectator()){
            Channel spec;
            try {
                spec = APICore.getPlugin().getChannelManager().getChannel("channel.spec");
            } catch(ChannelNotFoundException e1){
                e1.printStackTrace();
                return;
            }
            if(generalChat){
                e.setFormat(spec.getDisplayName() + player.getAPPlayer().getDisplayName() + "§8 » §f" + message.substring(1));
                return;
            } else {
                spec.sendMessage("§f" + player.getAPPlayer().getName() + " §8»§a " + message);
                e.setCancelled(true);
                return;
            }
        }
        if(player.isPlaying()){
            if(Game.getGame().isTeam()){
                if(player.hasTeam()){
                    if(!generalChat && player.getGameTeam().getMembers().getSize() > 1){
                        player.getGameTeam().sendMessageTeam(player.getAPPlayer().getDisplayName() + "§8 » §f" + message);
                        e.setCancelled(true);
                        return;
                    }
                }
            }
        }
        if(!Game.getGame().isTchat() && !player.getAPPlayer().hasPermission("hydranium.uhc.bypass")){
            e.setCancelled(true);
            player.getAPPlayer().sendMessage(Prefix.uhc + "§cLe tchat est désactivé");
            return;
        }
        if(generalChat){
            e.setFormat(player.getAPPlayer().getDisplayName() + "§8 » §f" + message.substring(1));
        } else {
            e.setFormat(player.getAPPlayer().getDisplayName() + "§8 » §f" + message);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBreakBlock(UHCPlayerBreakBlockEvent e){
        UHCPlayer player = e.getPlayer();
        APPlayer player1 = player.getAPPlayer();
        player1.getStats().add("mined-block");
        if(player.isPlacedBlock(e.getBlock())){
            return;
        }
        switch(e.getBlock().getType()){
            case DIAMOND_ORE: player.addDiamond(1);break;
            case GOLD_ORE: player.addGold(1);break;
            case EMERALD_ORE: player1.getStats().add("emerald-mined");break;
            case REDSTONE_ORE: player1.getStats().add("redstone-mined");break;
            case COAL_ORE: player1.getStats().add("coal-mined");break;
            case IRON_ORE: player1.getStats().add("iron-mined");break;
            case LAPIS_ORE: player1.getStats().add("lapis-mined");break;
            case QUARTZ_ORE: player1.getStats().add("quartz-mined");break;
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onRareOre(UHCPlayerBreakBlockEvent e){
        if(e.getBlock().getType() == Material.DIAMOND_ORE ||
                e.getBlock().getType() == Material.GOLD_ORE){
            UHCPlayer player = e.getPlayer();
            Block block = e.getBlock();
            if(player.isPlacedBlock(block) || player.hasFindOre(block)){
                return;
            }
            List<Block> vein;
            switch(block.getType()){
                case DIAMOND_ORE:
                    vein = Blocks.getVein(block, Collections.singletonList(Material.DIAMOND_ORE), "BlockPlacePlayer");
                    for(Block blockVein : vein){
                        blockVein.setMetadata("DiamondVeinPlayer", new FixedMetadataValue(UHCCore.getPlugin(), player.getAPPlayer().getName()));
                    }
                    try {
                        APText message = new APText("§6" + player.getAPPlayer().getName() + "§7 a trouvé§3 du diamant §b[" + (player.getDiamond() + vein.size() - 1) + "]");
                        message.showText("§7Se téléporter");
                        message.runCommand("/tp " + player.getAPPlayer().getName());
                        APICore.getPlugin().getChannelManager().getChannel("channel.orespy").sendMessage(message);
                    } catch(ChannelNotFoundException e1) {
                        e1.printStackTrace();
                    }
                    break;
                case GOLD_ORE:
                    vein = Blocks.getVein(block, Collections.singletonList(Material.GOLD_ORE), "BlockPlacePlayer");
                    for(Block blockVein : vein){
                        blockVein.setMetadata("GoldVeinPlayer", new FixedMetadataValue(UHCCore.getPlugin(), player.getAPPlayer().getName()));
                    }
                    try {
                        APText message = new APText("§6" + player.getAPPlayer().getName() + "§7 a trouvé§6 de l'or §b[" + (player.getGold() + vein.size() - 1) + "]");
                        message.showText("§7Se téléporter");
                        message.runCommand("/tp " + player.getAPPlayer().getName());
                        APICore.getPlugin().getChannelManager().getChannel("channel.orespy").sendMessage(message);
                    } catch(ChannelNotFoundException e1) {
                        e1.printStackTrace();
                    }
                    break;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent e){
        e.getBlock().setMetadata("BlockPlacePlayer", new FixedMetadataValue(UHCCore.getPlugin(), e.getPlayer().getUniqueId()));
        APICore.getPlugin().getAPPlayer(e.getPlayer().getUniqueId()).getStats().add("placed-block", 1);
    }

    @EventHandler
    public void onHostClick(PlayerInteractEntityEvent event){
        if(!(event.getRightClicked() instanceof Player) || event.getPlayer().getGameMode() != GameMode.SPECTATOR){
            return;
        }
        UHCPlayer player = Game.getGame().getUHCPlayer(event.getPlayer().getUniqueId());
        if(!player.isSpectator()){
            return;
        }
        player.openInventoryPlayer(Game.getGame().getUHCPlayer(event.getRightClicked().getUniqueId()));

    }

    @EventHandler
    public void playerFood(FoodLevelChangeEvent event) {
        if(event.getEntity() instanceof Player){
            if(Game.getGame().getUHCPlayer(event.getEntity().getUniqueId()).isDead()){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player){
            APICore.getPlugin().getAPPlayer(e.getDamager().getUniqueId()).getStats().add("damage-inflicted", (int)e.getFinalDamage());
            APICore.getPlugin().getAPPlayer(e.getEntity().getUniqueId()).getStats().add("damage-received", (int)e.getFinalDamage());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onKillMob(EntityDeathEvent e){
        if(e.getEntity().getKiller() == null){
            return;
        }
        if(Mobs.isAnimal(e.getEntityType())){
            APICore.getPlugin().getAPPlayer(e.getEntity().getKiller().getUniqueId()).getStats().add("mob-killed");
        }
        if(Mobs.isMonster(e.getEntityType())){
            APICore.getPlugin().getAPPlayer(e.getEntity().getKiller().getUniqueId()).getStats().add("monster-killed");
        }
        if(Mobs.isVenimous(e.getEntityType())){
            APICore.getPlugin().getAPPlayer(e.getEntity().getKiller().getUniqueId()).getStats().add("venimous-killed");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCraft(CraftItemEvent e){
        if(e.getRecipe().getResult().getType() == Material.TNT){
            APICore.getPlugin().getAPPlayer(e.getWhoClicked().getUniqueId()).getStats().add("tnt-crafted");
        }
        APICore.getPlugin().getAPPlayer(e.getWhoClicked().getUniqueId()).getStats().add("items-crafted");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMeltedItem(InventoryClickEvent e){
        if(e.getSlotType() == InventoryType.SlotType.RESULT){
            if(e.getWhoClicked().getOpenInventory().getTopInventory() instanceof FurnaceInventory){
                APICore.getPlugin().getAPPlayer(e.getWhoClicked().getUniqueId()).getStats().add("item-cooked");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEnchant(EnchantItemEvent e){
        APICore.getPlugin().getAPPlayer(e.getEnchanter().getUniqueId()).getStats().add("item-enchanted");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onArrow(EntityShootBowEvent e){
        if(e.getEntity() instanceof Player){
            APICore.getPlugin().getAPPlayer(e.getEntity().getUniqueId()).getStats().add("arrow-fired");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEnderpearl(PlayerInteractEvent e){
        if(e.getItem() == null){
            return;
        }
        if((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && e.getItem().getType() == Material.ENDER_PEARL){
            APICore.getPlugin().getAPPlayer(e.getPlayer().getUniqueId()).getStats().add("enderpearl-launched");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFood(PlayerItemConsumeEvent e){
        if(e.getItem().getType() == Material.GOLDEN_APPLE){
            if(Game.getGame().getSettings().getCraftGoldenHeadSetting().isItemGoldenHead(e.getItem())){
                APICore.getPlugin().getAPPlayer(e.getPlayer().getUniqueId()).getStats().add("ghead-eaten");
            } else {
                APICore.getPlugin().getAPPlayer(e.getPlayer().getUniqueId()).getStats().add("gapple-eaten");
            }
        } else {
            APICore.getPlugin().getAPPlayer(e.getPlayer().getUniqueId()).getStats().add("food-eaten");
        }
    }

    public void giveHostPermissions(UUID uuid){
        APPlayer player = APICore.getPlugin().getAPPlayer(uuid);
        player.addPermission("hydranium.api.command.socialspy");
        player.addPermission("hydranium.uhc.command.helpop");
        player.addPermission("hydranium.uhc.command.orespy");
        player.addPermission("hydranium.uhc.command.gamemode");
        player.addPermission("hydranium.uhc.command.host");
        player.addPermission("hydranium.uhc.command.sendhost");
        player.addPermission("hydranium.uhc.command.sethost");
        player.addPermission("hydranium.uhc.command.menu");
        player.addPermission("hydranium.uhc.command.uhc");
        player.addPermission("hydranium.uhc.command.clear");
        player.addPermission("hydranium.uhc.command.teleport");
        player.addPermission("hydranium.uhc.command.killoffline");
        player.addPermission("hydranium.uhc.command.invsee");
        player.addPermission("hydranium.uhc.command.stalk");
        player.addPermission("hydranium.uhc.command.welcome");
        player.addPermission("hydranium.uhc.command.player");
        player.addPermission("hydranium.uhc.command.revive");
        player.addPermission("hydranium.uhc.gui.host");
        player.addPermission("hydranium.anticheat.channel.anticheat");
    }

    public void removeHostPermissions(UUID uuid){
        APPlayer player = APICore.getPlugin().getAPPlayer(uuid);
        player.removePermission("hydranium.api.command.socialspy");
        player.removePermission("hydranium.uhc.command.helpop");
        player.removePermission("hydranium.uhc.command.orespy");
        player.removePermission("hydranium.uhc.command.gamemode");
        player.removePermission("hydranium.uhc.command.host");
        player.removePermission("hydranium.uhc.command.sendhost");
        player.removePermission("hydranium.uhc.command.sethost");
        player.removePermission("hydranium.uhc.command.menu");
        player.removePermission("hydranium.uhc.command.uhc");
        player.removePermission("hydranium.uhc.command.clear");
        player.removePermission("hydranium.uhc.command.teleport");
        player.removePermission("hydranium.uhc.command.killoffline");
        player.removePermission("hydranium.uhc.command.invsee");
        player.removePermission("hydranium.uhc.command.stalk");
        player.removePermission("hydranium.uhc.command.welcome");
        player.removePermission("hydranium.uhc.command.player");
        player.removePermission("hydranium.uhc.command.revive");
        player.removePermission("hydranium.uhc.gui.host");
        player.removePermission("hydranium.anticheat.channel.anticheat");
    }

    public void setDefaultPermissions(APPlayer player){
        player.addPermission("hydranium.uhc.command.helpop");
        player.addPermission("hydranium.uhc.command.rules");
        /*if(!Game.getGame().hasGameStart()){
            player.addPermission("hydranium.uhc.minigame.shifumi");
        }*/
        if(Game.getGame().isTeam()){
            if(Game.getGame().hasGameStart()){
                player.addPermission("hydranium.uhc.command.coords");
            } else {
                player.addPermission("hydranium.uhc.command.team");
            }
        }
        Bukkit.getScheduler().runTaskAsynchronously(APICore.getPlugin(), ()-> {
            try {
                Rank rank = player.getRank();
                Bukkit.getScheduler().scheduleSyncDelayedTask(APICore.getPlugin(), ()-> {
                    switch(rank){
                        case PLAYER:
                            break;
                        case FRIEND:
                            break;
                        case HOSTTEST:
                            player.addPermission("hydranium.uhc.command.rules.player");
                            player.addPermission("hydranium.uhc.spec");
                            break;
                        case HOST:
                            player.addPermission("hydranium.uhc.command.rules.player");
                            player.addPermission("hydranium.uhc.spec");
                            player.addPermission("hydranium.uhc.allowhost");
                            break;
                        case LEADHOST:
                            player.addPermission("hydranium.uhc.command.rules.player");
                            player.addPermission("hydranium.uhc.spec");
                            player.addPermission("hydranium.uhc.allowhost");
                            break;
                        case BUILDER:
                            break;
                        case ADMIN:
                            if(player.getUUID().toString().equals("43da311c-d869-4e88-9b78-f1d4fc193ed4")){
                                player.addPermission("hydranium.uhc.command.setdebug");
                            }
                            player.addPermission("hydranium.uhc.command.rules.player");
                            player.addPermission("hydranium.uhc.spec");
                            player.addPermission("hydranium.uhc.allowhost");
                            player.addPermission("hydranium.uhc.bypass");
                            player.addPermission("hydranium.uhc.command.debug");
                    }
                });
            } catch(CanTakeTimeException e){}
        });

    }

}
