package fr.adrackiin.hydranium.uhc.game.core;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.events.PostInitEvent;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.GameStateChangeEvent;
import fr.adrackiin.hydranium.uhc.events.UHCPlayerBreakBlockEvent;
import fr.adrackiin.hydranium.uhc.events.WorldCheckFinishEvent;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.Timer;
import fr.adrackiin.hydranium.uhc.game.border.UHCBorder;
import fr.adrackiin.hydranium.uhc.game.core.hblocks.blockmanagers.Drops;
import fr.adrackiin.hydranium.uhc.game.core.hblocks.blockmanagers.HBlock;
import fr.adrackiin.hydranium.uhc.game.core.hblocks.blockmanagers.TypeID;
import fr.adrackiin.hydranium.uhc.game.core.players.UHCPlayer;
import fr.adrackiin.hydranium.uhc.game.gameworld.GameWorld;
import fr.adrackiin.hydranium.uhc.game.gameworld.Pregeneration;
import fr.adrackiin.hydranium.uhc.game.platform.Platform;
import fr.adrackiin.hydranium.uhc.game.state.GameState;
import fr.adrackiin.hydranium.uhc.game.state.Invulnerability;
import fr.adrackiin.hydranium.uhc.game.state.PreGame;
import fr.adrackiin.hydranium.uhc.game.state.Teleportation;
import fr.adrackiin.hydranium.uhc.utils.PubSub;
import fr.adrackiin.hydranium.uhc.utils.enumeration.ItemsAllowed;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import fr.adrackiin.hydranium.uhc.utils.minecraft.Blocks;
import fr.adrackiin.hydranium.uhc.utils.minecraft.Items;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UHCManager implements Listener {

    private final Map<TypeID, HBlock.Type> uhcblocksId = new HashMap<>();
    private final Map<HBlock.Type, HBlock> uhcblocks = new HashMap<>();
    private PreGame preGame;
    private Pregeneration pregeneration;
    private Invulnerability invulnerability;
    public UHCManager(){
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
        Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), this::initUHCBlocks);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onWorldLoaded(PostInitEvent event){
        Game.getGame().setPlatform(new Platform());
        preGame = new PreGame();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGameStateChange(GameStateChangeEvent e){
        GameState gameState = e.getNewGameState();
        Game.getGame().getGameStateManager().setGameState(gameState);
        switch(gameState){
            case WAITING_HOST:
                break;
            case CONFIG:
                break;
            case PREGEN:
                this.doPregen();
                break;
            case WAITING_WHITELIST:
                Bukkit.broadcastMessage(Prefix.uhc + "§7" + "La Prégénération est " + "§a" + "terminée" + "§7" + ". L'UHC est prêt");
                break;
            case WHITELIST:
                Bukkit.broadcastMessage(Prefix.uhc + "§a" + "La Pré-Whitelist est ouverte. Les joueurs possédant une Pré-Whitelist peuvent entrer");
                break;
            case CLOSE_WHITELIST:
                Bukkit.broadcastMessage(Prefix.uhc + "§c" + "La Pré-Whitelist est fermée");
                for(UUID uuid : Game.getGame().getPreWhitelistedPlayers().copy()) {
                    Bukkit.getPlayer(uuid).sendMessage(Prefix.uhc + "§7" + "Vous venez de dépenser une " + "§6" + "Pré-Whitelist");
                    //APICore.getPlugin().getDataManager().add(uuid, "whitelist", -1);
                }
                break;
            case OPEN:
                Bukkit.broadcastMessage(Prefix.uhc + "§a" + "Ouverture de la Whitelist");
                UHCCore.getPlugin().getServer().dispatchCommand(UHCCore.getPlugin().getServer().getConsoleSender(), "timings reset");
                break;
            case CLOSE:
                Bukkit.broadcastMessage(Prefix.uhc + "§c" + "Fermeture de la Whitelist");
                break;
            case START:
                int i = 1;
                UHCPlayer player;
                if(Game.getGame().isTeam()){
                    for(APPlayer pl : APICore.getPlugin().getAPPlayers()){
                        pl.removePermission("hydranium.uhc.command.team");
                        pl.addPermission("hydranium.uhc.command.coords");
                    }
                }
                for(UUID uuid : Game.getGame().getAlivePlayers().copy()) {
                    player = Game.getGame().getUHCPlayer(uuid);
                    Game.getGame().getWhitelistedPlayers().add(uuid);
                    player.getAPPlayer().clearInventory();
                    player.getAPPlayer().getStats().add("game-played");
                    if(player.getAPPlayer().hasPermission("hydranium.api.command.fly")){
                        player.getAPPlayer().removePermission("hydranium.api.command.fly");
                    }
                    player.setId(i);
                    if(Game.getGame().isTeam()){
                        if(Bukkit.getScoreboardManager().getMainScoreboard().getTeam("uhcffa" + i) != null){
                            Bukkit.getScoreboardManager().getMainScoreboard().getTeam("uhcffa" + i).unregister();
                        }
                        Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("uhcffa" + i).setPrefix("§f");
                    }
                    i++;
                }
                PubSub.sendWhitelist();
                Bukkit.broadcastMessage(Prefix.uhc + "§a" + "Démarrage de l'UHC" + "§7" + ". La déconnexion / reconnexion est possible si elle dure moins de 10 minutes. Bonne chance !");
                if(Game.getGame().isTeam()) {
                    Game.getGame().getTeamManager().fillTeams();
                    Game.getGame().getTeamManager().setAliveTeam();
                } else {
                    for(UUID uuid : Game.getGame().getAlivePlayers().copy()){
                        player = Game.getGame().getUHCPlayer(uuid);
                        if(player != null) {
                            Game.getGame().getTeamManager().setAlive(player);
                        }
                    }
                }
                Bukkit.getServer().getPluginManager().callEvent(new GameStateChangeEvent(GameState.TELEPORATTION));
                break;
            case TELEPORATTION:
                Bukkit.broadcastMessage(Prefix.uhc + "§7" + "Merci de ne pas vous déconnecter durant la téléportation !");
                preGame.stop();
                new Teleportation();
                break;
            case INVULNERABILITY:
                Game.getGame().setTimer(new Timer());
                Game.getGame().getTimer().start();
                invulnerability = new Invulnerability();
                Game.getGame().difficulty();
                Game.getGame().getScoreboardManager().remove(4);
                Game.getGame().getScoreboardManager().remove(3);
                Game.getGame().getScoreboardManager().setKills();
                if(Game.getGame().isTeam()) {
                    Game.getGame().getScoreboardManager().setTeamsKills();
                }
                break;
            case MINING:
                Bukkit.broadcastMessage(Prefix.uhc + "§c" + "Fin de l'invincibilité");
                invulnerability.stop();
                break;
            case PVP:
                Game.getGame().getScoreboardManager().remove(7);
                Bukkit.broadcastMessage(Prefix.uhc + "§c" + "Activation du PvP !");
                Game.getGame().pvp();
                break;
            case BORDER:
                if(Game.getGame().getGameWorldType() == GameWorld.Type.NORMAL) {
                    Bukkit.broadcastMessage(Prefix.uhc + "§c" + "Réduction de la bordure" + "§7" + ". Veuillez remonter !");
                } else {
                    Bukkit.broadcastMessage(Prefix.uhc + "§c" + "Réduction de la bordure");
                }
                Game.getGame().getBorderManager().init();
                Game.getGame().getBorderManager().start();
                Game.getGame().disableNether();
                break;
            case MEETUP:
                if(Game.getGame().getSettings().getBorderType() == UHCBorder.Type.BORDERMOVE){
                } else {
                    Game.getGame().getScoreboardManager().remove(7);
                }
                break;
            case VICTORY:
                UHCCore.getPlugin().getServer().dispatchCommand(UHCCore.getPlugin().getServer().getConsoleSender(), "timings paste");
                break;
        }
    }

    public void doPregen(){
        Bukkit.broadcastMessage(Prefix.uhc + "§aUHC validé§7. Vous ne pourrez plus §c modifier les scénarios et certaines options");
        pregeneration = new Pregeneration();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void chunkUnload(ChunkUnloadEvent e) {
        Entity[] arrayOfEntity;
        int j = (arrayOfEntity = e.getChunk().getEntities()).length;
        for (int i = 0; i < j; i ++) {
            Entity m = arrayOfEntity[i];
            if (((m instanceof Animals)) || (((m instanceof LivingEntity)) && (m.getType() != EntityType.VILLAGER) && (!(m instanceof Player)))) {
                m.remove();
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCheck(WorldCheckFinishEvent e){
        pregeneration.stop();
        PubSub.teamUpdate();
        PubSub.borderUpdate();
        PubSub.pvpUpdate();
        PubSub.maxPlayersUpdate();
        UHCCore.getPlugin().getServer().getPluginManager().callEvent(new GameStateChangeEvent(GameState.WAITING_WHITELIST));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(UHCPlayerBreakBlockEvent e){
        if(e.getBlock().getType() == Material.AIR){
            return;
        }
        boolean noDrops = true;
        HBlock hBlock = getUHCBlock(getUHCBlock(e.getBlock().getType(), e.getBlock().getData()));
        List<ItemStack> drops = hBlock.getDrops(e.getPlayer().getAPPlayer().getItemInHand());
        if(Items.isTool(e.getPlayer().getAPPlayer().getItemInHand().getType())){
            ItemStack tool = e.getPlayer().getAPPlayer().getItemInHand();
            if(!Blocks.isInstantMining(e.getBlock().getType())){
                if(tool.getDurability() >= tool.getType().getMaxDurability()){
                    Items.breakItem(tool, e.getPlayer().getAPPlayer().getPlayer());
                } else {
                    tool.setDurability((short) (tool.getDurability() + 1));
                }
            }
        }
        Location b = e.getBlock().getLocation().add(0.5D, 0.5D, 0.5D);
        e.getBlock().setType(HBlock.Type.AIR.getType());
        for(ItemStack toDrop : drops){
            if(toDrop.getType() != Material.AIR) {
                noDrops = false;
                b.getWorld().dropItem(b, toDrop);
            }
        }
        if(e.getExp() > 0) {
            b.getWorld().spawn(b, ExperienceOrb.class).setExperience(e.getExp());
        }
        if(noDrops) {
            if(hBlock.getReplace() != HBlock.Type.AIR){
                e.getBlock().setType(hBlock.getReplace().getType());
            }
            if(hBlock.getEntity() != null) {
                b.getWorld().spawn(b, hBlock.getEntity().getEntityClass());
            }
        }
        if(e.getBlock().getType() == Material.AIR){
            if(e.getBlock().hasMetadata("BlockPlacePlayer")){
                e.getBlock().removeMetadata("BlockPlacePlayer", UHCCore.getPlugin());
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventory(InventoryClickEvent e){
        if(e.getCurrentItem() == null){
            return;
        }
        if(e.getCurrentItem().getType() == Material.AIR){
            return;
        }
        if(e.getWhoClicked().getGameMode() == GameMode.CREATIVE){
            if(APICore.getPlugin().getAPPlayer(e.getWhoClicked().getUniqueId()).hasPermission("hydranium.uhc.bypass")){
                return;
            }
            boolean isAllowed = false;
            Material typeItemClicked = e.getCurrentItem().getType();
            for(Material allowed : ItemsAllowed.itemsAllowed){
                if(typeItemClicked == allowed){
                    isAllowed = true;
                    break;
                }
            }
            if(!isAllowed){
                e.setCancelled(true);
            }
        }
    }

    public void addUHCBlock(TypeID id, HBlock.Type type, HBlock hBlock){
        uhcblocksId.put(id, type);
        uhcblocks.put(type, hBlock);
    }

    public HBlock.Type getUHCBlock(Material mat, short data){
        return uhcblocksId.get(new TypeID(mat, data));
    }

    public HBlock getUHCBlock(HBlock.Type type){
        return uhcblocks.get(type);
    }

    private void initUHCBlocks(){
        new HBlock(HBlock.Type.STONE,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE},
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.COBBLESTONE)}, new ItemStack[]{Items.simpleItem(HBlock.Type.STONE)}));
        new HBlock(HBlock.Type.GRANITE,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.POLISHED_GRANITE,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.DIORITE,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.POLISHED_DIORITE,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.ANDESITE,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.POLISHED_ANDESITE,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.GRASS,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.DIRT)}, new ItemStack[]{Items.simpleItem(HBlock.Type.GRASS)}));
        new HBlock(HBlock.Type.DIRT);
        new HBlock(HBlock.Type.COARSE_DIRT);
        new HBlock(HBlock.Type.PODZOL,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.DIRT)}, new ItemStack[]{Items.simpleItem(HBlock.Type.PODZOL)}));
        new HBlock(HBlock.Type.COBBLESTONE,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.OAK_PLANK);
        new HBlock(HBlock.Type.SPRUCE_PLANK);
        new HBlock(HBlock.Type.BIRCH_PLANK);
        new HBlock(HBlock.Type.JUNGLE_PLANK);
        new HBlock(HBlock.Type.ACACIA_PLANK);
        new HBlock(HBlock.Type.DARK_PLANK);
        new HBlock(HBlock.Type.OAK_SAPLING);
        new HBlock(HBlock.Type.SPRUCE_SAPLING);
        new HBlock(HBlock.Type.BIRCH_SAPLING);
        new HBlock(HBlock.Type.JUNGLE_SAPLING);
        new HBlock(HBlock.Type.ACACIA_SAPLING);
        new HBlock(HBlock.Type.DARK_SAPLING);
        new HBlock(HBlock.Type.BEDROCK,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}));
        new HBlock(HBlock.Type.FLOWING_WATER,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}));
        new HBlock(HBlock.Type.WATER,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}));
        new HBlock(HBlock.Type.FLOWING_LAVA,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}));
        new HBlock(HBlock.Type.LAVA,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}));
        new HBlock(HBlock.Type.SAND);
        new HBlock(HBlock.Type.RED_SAND);
        new HBlock(HBlock.Type.GRAVEL,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.GRAVEL), Items.simpleItem(Material.FLINT)}, new short[][]{{9000}, {10000}}, new ItemStack[]{Items.simpleItem(HBlock.Type.GRAVEL), Items.simpleItem(Material.FLINT)}, new short[][][]{{{9000-1400}, {9000-2500}, {0}}, {{10000}, {10000}, {10000}}}, new ItemStack[]{Items.simpleItem(HBlock.Type.GRAVEL), Items.simpleItem(Material.FLINT)}, new short[][]{{10000}, {0}}, true));
        new HBlock(HBlock.Type.GOLD_ORE,
                new Material[]{Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE},
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.GOLD_ORE)}));
        new HBlock(HBlock.Type.IRON_ORE,
                new Material[]{Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE},
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.IRON_ORE)}));
        new HBlock(HBlock.Type.COAL_ORE,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE},
                new Drops(new ItemStack[]{Items.simpleItem(Material.COAL)}, new short[][]{{10000}}, new ItemStack[]{Items.simpleItem(Material.COAL)}, new short[][][]{{{10000, 3333}, {10000, 5000, 2500}, {10000, 6000, 4000, 2000}}}, new ItemStack[]{Items.simpleItem(HBlock.Type.COAL_ORE)}, new short[][]{{10000}}, false));
        new HBlock(HBlock.Type.LAPIS_ORE,
                new Material[]{Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE},
                new Drops(new ItemStack[]{Items.simpleItem(Material.INK_SACK, 4)}, new short[][]{{10000, 10000, 10000, 10000, 5000, 5000, 5000, 5000}}, new ItemStack[]{Items.simpleItem(Material.INK_SACK, 4)}, new short[][][]{{{10000, 10000, 10000, 10000, 5000, 5000, 5000, 5000, 3334, 3333, 3333, 3333, 1666, 1667, 1667, 1667}, {10000, 10000, 10000, 10000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 1250, 1250, 1250, 1250}, {10000, 10000, 10000, 10000, 5000, 5000, 5000, 5000, 6000, 6000, 6000, 6000, 4000, 4000, 4000, 4000, 2000, 2000, 2000, 2000, 3000, 3000, 3000, 3000, 2000, 2000, 2000, 2000, 1000, 1000, 1000, 1000}}}, new ItemStack[]{Items.simpleItem(HBlock.Type.LAPIS_ORE)}, new short[][]{{10000}}));
        new HBlock(HBlock.Type.DIAMOND_ORE,
                new Material[]{Material.IRON_PICKAXE, Material.DIAMOND_PICKAXE},
                new Drops(new ItemStack[]{Items.simpleItem(Material.DIAMOND)}, new short[][]{{10000}}, new ItemStack[]{Items.simpleItem(Material.DIAMOND)}, new short[][][]{{{10000, 3333}, {10000, 5000, 2500}, {10000, 6000, 4000, 2000}}}, new ItemStack[]{Items.simpleItem(HBlock.Type.DIAMOND_ORE)}, new short[][]{{10000}}, false));
        new HBlock(HBlock.Type.REDSTONE_ORE,
                new Material[]{Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE},
                new Drops(new ItemStack[]{Items.simpleItem(Material.REDSTONE)}, new short[][]{{10000, 10000, 10000, 10000, 5000}}, new ItemStack[]{Items.simpleItem(Material.REDSTONE)}, new short[][][]{{{10000, 10000, 10000, 10000, 5000, 5000}, {10000, 10000, 10000, 10000, 5000, 5000, 5000}, {10000, 10000, 10000, 10000, 5000, 5000, 5000, 5000}}}, new ItemStack[]{Items.simpleItem(HBlock.Type.REDSTONE_ORE)}, new short[][]{{10000}}));
        new HBlock(HBlock.Type.GLOWING_REDSTONE_ORE,
                new Material[]{Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE},
                new Drops(new ItemStack[]{Items.simpleItem(Material.REDSTONE)}, new short[][]{{10000, 10000, 10000, 10000, 5000}}, new ItemStack[]{Items.simpleItem(Material.REDSTONE)}, new short[][][]{{{10000, 10000, 10000, 10000, 5000, 5000}, {10000, 10000, 10000, 10000, 5000, 5000, 5000}, {10000, 10000, 10000, 10000, 5000, 5000, 5000, 5000}}}, new ItemStack[]{Items.simpleItem(HBlock.Type.REDSTONE_ORE)}, new short[][]{{10000}}));
        new HBlock(HBlock.Type.QUARTZ_ORE,
                new Material[]{Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE},
                new Drops(new ItemStack[]{Items.simpleItem(Material.QUARTZ)}, new short[][]{{10000}}, new ItemStack[]{Items.simpleItem(Material.QUARTZ)}, new short[][][]{{{10000, 3333}, {10000, 5000, 2500}, {10000, 6000, 4000, 2000}}}, new ItemStack[]{Items.simpleItem(HBlock.Type.QUARTZ_ORE)}, new short[][]{{10000}}, false));
        new HBlock(HBlock.Type.EMERALD_ORE,
                new Material[]{Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE},
                new Drops(new ItemStack[]{Items.simpleItem(Material.EMERALD)}, new short[][]{{10000}}, new ItemStack[]{Items.simpleItem(Material.EMERALD)}, new short[][][]{{{10000, 3333}, {10000, 5000, 2500}, {10000, 6000, 4000, 2000}}}, new ItemStack[]{Items.simpleItem(HBlock.Type.EMERALD_ORE)}, new short[][]{{10000}}, false));
        new HBlock(HBlock.Type.OAK_WOOD);
        new HBlock(HBlock.Type.SPRUCE_WOOD);
        new HBlock(HBlock.Type.BIRCH_WOOD);
        new HBlock(HBlock.Type.JUNGLE_WOOD);
        new HBlock(HBlock.Type.ACACIA_WOOD);
        new HBlock(HBlock.Type.DARK_WOOD);
        new HBlock(HBlock.Type.OAK_LEAVES,
                new Drops(new ItemStack[]{Items.simpleItem(Material.SAPLING, 0), Items.simpleItem(Material.APPLE)}, new short[][]{{500}, {50}}, new ItemStack[]{Items.simpleItem(Material.SAPLING, 0), Items.simpleItem(Material.APPLE)}, new short[][][]{{{600}, {800}, {1000}}, {{60, 70, 80}}}, new ItemStack[]{Items.simpleItem(HBlock.Type.OAK_LEAVES)}, new short[][]{{10000}}, new ItemStack[]{Items.simpleItem(HBlock.Type.OAK_LEAVES)}, new Material[][]{{Material.SHEARS}}, new short[][]{{10000}}));
        new HBlock(HBlock.Type.SPRUCE_LEAVES,
                new Drops(new ItemStack[]{Items.simpleItem(Material.SAPLING, 1), Items.simpleItem(Material.APPLE)}, new short[][]{{500}, {50}}, new ItemStack[]{Items.simpleItem(Material.SAPLING, 1), Items.simpleItem(Material.APPLE)}, new short[][][]{{{600}, {800}, {1000}}, {{60, 70, 80}}}, new ItemStack[]{Items.simpleItem(HBlock.Type.SPRUCE_LEAVES)}, new short[][]{{10000}}, new ItemStack[]{Items.simpleItem(HBlock.Type.SPRUCE_LEAVES)}, new Material[][]{{Material.SHEARS}}, new short[][]{{10000}}));
        new HBlock(HBlock.Type.BIRCH_LEAVES,
                new Drops(new ItemStack[]{Items.simpleItem(Material.SAPLING, 2), Items.simpleItem(Material.APPLE)}, new short[][]{{500}, {50}}, new ItemStack[]{Items.simpleItem(Material.SAPLING, 2), Items.simpleItem(Material.APPLE)}, new short[][][]{{{600}, {800}, {1000}}, {{60, 70, 80}}}, new ItemStack[]{Items.simpleItem(HBlock.Type.BIRCH_LEAVES)}, new short[][]{{10000}}, new ItemStack[]{Items.simpleItem(HBlock.Type.BIRCH_LEAVES)}, new Material[][]{{Material.SHEARS}}, new short[][]{{10000}}));
        new HBlock(HBlock.Type.JUNGLE_LEAVES,
                new Drops(new ItemStack[]{Items.simpleItem(Material.SAPLING, 3), Items.simpleItem(Material.APPLE)}, new short[][]{{500}, {50}}, new ItemStack[]{Items.simpleItem(Material.SAPLING, 3), Items.simpleItem(Material.APPLE)}, new short[][][]{{{600}, {800}, {1000}}, {{60, 70, 80}}}, new ItemStack[]{Items.simpleItem(HBlock.Type.JUNGLE_LEAVES)}, new short[][]{{10000}}, new ItemStack[]{Items.simpleItem(HBlock.Type.JUNGLE_LEAVES)}, new Material[][]{{Material.SHEARS}}, new short[][]{{10000}}));
        new HBlock(HBlock.Type.ACACIA_LEAVES,
                new Drops(new ItemStack[]{Items.simpleItem(Material.SAPLING, 4), Items.simpleItem(Material.APPLE)}, new short[][]{{500}, {50}}, new ItemStack[]{Items.simpleItem(Material.SAPLING, 4), Items.simpleItem(Material.APPLE)}, new short[][][]{{{600}, {800}, {1000}}, {{60, 70, 80}}}, new ItemStack[]{Items.simpleItem(HBlock.Type.ACACIA_LEAVES)}, new short[][]{{10000}}, new ItemStack[]{Items.simpleItem(HBlock.Type.ACACIA_LEAVES)}, new Material[][]{{Material.SHEARS}}, new short[][]{{10000}}));
        new HBlock(HBlock.Type.DARK_LEAVES,
                new Drops(new ItemStack[]{Items.simpleItem(Material.SAPLING, 5), Items.simpleItem(Material.APPLE)}, new short[][]{{500}, {50}}, new ItemStack[]{Items.simpleItem(Material.SAPLING, 5), Items.simpleItem(Material.APPLE)}, new short[][][]{{{600}, {800}, {1000}}, {{60, 70, 80}}}, new ItemStack[]{Items.simpleItem(HBlock.Type.DARK_LEAVES)}, new short[][]{{10000}}, new ItemStack[]{Items.simpleItem(HBlock.Type.DARK_LEAVES)}, new Material[][]{{Material.SHEARS}}, new short[][]{{10000}}));
        new HBlock(HBlock.Type.SPONGE);
        new HBlock(HBlock.Type.WET_SPONGE);
        new HBlock(HBlock.Type.GLASS,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.GLASS)}));
        new HBlock(HBlock.Type.LAPIS_BLOCK,
                new Material[]{Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.DISPENSER,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.SANDSTONE,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.CHISELED_SANDSTONE,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.SMOOTH_SANDSTONE,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.NOTE_BLOCK);
        new HBlock(HBlock.Type.BED,
                new Drops(new ItemStack[]{Items.simpleItem(Material.BED)}));
        new HBlock(HBlock.Type.BED_TOP,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}));
        new HBlock(HBlock.Type.POWERED_RAIL);
        new HBlock(HBlock.Type.DETECTOR_RAIL);
        new HBlock(HBlock.Type.STICKY_PISTON);
        new HBlock(HBlock.Type.COBWEB,
                new Material[]{Material.WOOD_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_SWORD, Material.SHEARS},
                new Drops(new ItemStack[]{Items.simpleItem(Material.STRING)}, new ItemStack[]{Items.simpleItem(HBlock.Type.COBWEB)}));
        new HBlock(HBlock.Type.SHRUB,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}));
        new HBlock(HBlock.Type.TALL_GRASS,
                new Drops(new ItemStack[]{Items.simpleItem(Material.SEEDS)}, new short[][]{{1250}}, new ItemStack[]{Items.simpleItem(Material.SEEDS)}, new short[][][]{{{1250, 1250, 1250}, {1250, 1250, 1250, 1250, 1250}, {1250, 1250, 1250, 1250, 1250, 1250, 1250}}}, new ItemStack[]{Items.simpleItem(HBlock.Type.TALL_GRASS)}, new short[][]{{10000}}, new ItemStack[]{Items.simpleItem(HBlock.Type.TALL_GRASS), Items.simpleItem(Material.SEEDS)}, new Material[][]{{Material.SHEARS}, {Material.WOOD_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.GOLD_HOE, Material.DIAMOND_HOE}}, new short[][]{{10000}, {2500}}));
        new HBlock(HBlock.Type.FERN,
                new Drops(new ItemStack[]{Items.simpleItem(Material.SEEDS)}, new short[][]{{1250}}, new ItemStack[]{Items.simpleItem(Material.SEEDS)}, new short[][][]{{{1250, 1250, 1250}, {1250, 1250, 1250, 1250, 1250}, {1250, 1250, 1250, 1250, 1250, 1250, 1250}}}, new ItemStack[]{Items.simpleItem(HBlock.Type.FERN)}, new short[][]{{10000}}, new ItemStack[]{Items.simpleItem(HBlock.Type.FERN), Items.simpleItem(Material.SEEDS)}, new Material[][]{{Material.SHEARS}, {Material.WOOD_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.GOLD_HOE, Material.DIAMOND_HOE}}, new short[][]{{10000}, {2500}}));
        new HBlock(HBlock.Type.DEAD_BUSH,
                new Drops(new ItemStack[]{Items.simpleItem(Material.STICK)}, new short[][]{{5000, 5000, 5000}}, new ItemStack[]{Items.simpleItem(Material.STICK)}, new short[][][]{{{5000, 5000, 5000, 5000}, {5000, 5000, 5000, 5000, 5000}, {5000, 5000, 5000, 5000, 5000, 5000}}}, new ItemStack[]{Items.simpleItem(HBlock.Type.DEAD_BUSH)}, new short[][]{{10000}}, new ItemStack[]{Items.simpleItem(HBlock.Type.DEAD_BUSH)}, new Material[][]{{Material.SHEARS}}, new short[][]{{10000}}));
        new HBlock(HBlock.Type.PISTON);
        new HBlock(HBlock.Type.PISTON_HEAD,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}));
        new HBlock(HBlock.Type.WHITE_WOOL);
        new HBlock(HBlock.Type.ORANGE_WOOL);
        new HBlock(HBlock.Type.MAGENTA_WOOL);
        new HBlock(HBlock.Type.LIGHT_BLUE_WOOL);
        new HBlock(HBlock.Type.YELLOW_WOOL);
        new HBlock(HBlock.Type.LIME_WOOL);
        new HBlock(HBlock.Type.PINK_WOOL);
        new HBlock(HBlock.Type.GRAY_WOOL);
        new HBlock(HBlock.Type.LIGHT_GRAY_WOOL);
        new HBlock(HBlock.Type.CYAN_WOOL);
        new HBlock(HBlock.Type.PURPLE_WOOL);
        new HBlock(HBlock.Type.BLUE_WOOL);
        new HBlock(HBlock.Type.BROWN_WOOL);
        new HBlock(HBlock.Type.GREEN_WOOL);
        new HBlock(HBlock.Type.RED_WOOL);
        new HBlock(HBlock.Type.BLACK_WOOL);
        new HBlock(HBlock.Type.DANDELION);
        new HBlock(HBlock.Type.POPPY);
        new HBlock(HBlock.Type.BLUE_ORCHID);
        new HBlock(HBlock.Type.ALIUM);
        new HBlock(HBlock.Type.AZURE_BLUET);
        new HBlock(HBlock.Type.RED_TULIP);
        new HBlock(HBlock.Type.ORANGE_TULIP);
        new HBlock(HBlock.Type.WHITE_TULIP);
        new HBlock(HBlock.Type.PINK_TULIP);
        new HBlock(HBlock.Type.OXEYE_DAISY);
        new HBlock(HBlock.Type.BROWN_MUSHROOM);
        new HBlock(HBlock.Type.RED_MUSHROOM);
        new HBlock(HBlock.Type.GOLD_BLOCK,
                new Material[]{Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.IRON_BLOCK,
                new Material[]{Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.DOUBLE_STONE_SLAB,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE},
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.STONE_SLAB, (byte)2)}));
        new HBlock(HBlock.Type.DOUBLE_SANDSTONE_SLAB,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE},
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.SANDSTONE_SLAB, (byte)2)}));
        new HBlock(HBlock.Type.DOUBLE_WOODEN_SLAB,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.OAK_SLAB, (byte)2)}));
        new HBlock(HBlock.Type.DOUBLE_COBBLESTONE_SLAB,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE},
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.COBBLESTONE_SLAB, (byte)2)}));
        new HBlock(HBlock.Type.DOUBLE_BRICK_SLAB,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE},
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.BRICK_SLAB, (byte)2)}));
        new HBlock(HBlock.Type.DOUBLE_STONEBRICK_SLAB,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE},
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.STONEBRICK_SLAB, (byte)2)}));
        new HBlock(HBlock.Type.DOUBLE_NETHERBRICK_SLAB,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE},
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.NETHERBRICK_SLAB, (byte)2)}));
        new HBlock(HBlock.Type.DOUBLE_QUARTZ_SLAB,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE},
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.QUARTZ_SLAB, (byte)2)}));
        new HBlock(HBlock.Type.STONE_SLAB,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.SANDSTONE_SLAB,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.COBBLESTONE_SLAB,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.BRICK_SLAB,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.STONEBRICK_SLAB,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.NETHERBRICK_SLAB,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.QUARTZ_SLAB,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.BRICK,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.TNT);
        new HBlock(HBlock.Type.BOOKSHELF,
                new Drops(new ItemStack[]{Items.simpleItem(Material.BOOK, 0, (byte)3)}, new ItemStack[]{Items.simpleItem(HBlock.Type.BOOKSHELF)}));
        new HBlock(HBlock.Type.MOSSY_COBBLESTONE);
        new HBlock(HBlock.Type.OBSIDIAN,
                new Material[]{Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.TORCH);
        new HBlock(HBlock.Type.FIRE,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}));
        new HBlock(HBlock.Type.MOB_SPAWNER,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE},
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}));
        new HBlock(HBlock.Type.OAK_PLANK_STAIR);
        new HBlock(HBlock.Type.COBBLESTONE_STAIR,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.BRICK_STAIR,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.STONEBRICK_STAIR,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.CHEST);
        new HBlock(HBlock.Type.REDSTONE_WIRE,
                new Drops(new ItemStack[]{Items.simpleItem(Material.REDSTONE)}));
        new HBlock(HBlock.Type.DIAMOND_BLOCK,
                new Material[]{Material.IRON_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.CRAFTING_TABLE);
        new HBlock(HBlock.Type.WHEAT_CROPS,
                new Drops(new ItemStack[]{Items.simpleItem(Material.SEEDS)}));
        new HBlock(HBlock.Type.WHEAT_CROPS_MATURE,
                new Drops(new ItemStack[]{Items.simpleItem(Material.SEEDS), Items.simpleItem(Material.WHEAT)}, new short[][]{{10000, 5000, 5000}, {10000}}, new ItemStack[]{Items.simpleItem(Material.SEEDS), Items.simpleItem(Material.WHEAT)}, new short[][][]{{{10000, 5000, 5000, 5000}, {10000, 5000, 5000, 5000, 5000}, {10000, 5000, 5000, 5000, 5000, 5000}}, {{10000, 100}, {10000, 500}, {10000, 1000}}}, new ItemStack[]{Items.simpleItem(Material.WHEAT)}, new short[][]{{10000, 10000}}));
        new HBlock(HBlock.Type.FARMLAND,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.DIRT)}));
        new HBlock(HBlock.Type.FURNACE,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.BURNING_FURNACE,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE},
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.FURNACE)}));
        new HBlock(HBlock.Type.GROUND_SIGN,
                new Drops(new ItemStack[]{Items.simpleItem(Material.SIGN)}));
        new HBlock(HBlock.Type.WALL_SIGN,
                new Drops(new ItemStack[]{Items.simpleItem(Material.SIGN)}));
        new HBlock(HBlock.Type.OAK_DOOR,
                new Drops(new ItemStack[]{Items.simpleItem(Material.WOOD_DOOR)}));
        new HBlock(HBlock.Type.IRON_DOOR,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE},
                new Drops(new ItemStack[]{Items.simpleItem(Material.IRON_DOOR)}));
        new HBlock(HBlock.Type.LADDER);
        new HBlock(HBlock.Type.RAIL);
        new HBlock(HBlock.Type.LEVER);
        new HBlock(HBlock.Type.STONE_PRESSURE_PLATE,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.OAK_PRESSURE_PLATE);
        new HBlock(HBlock.Type.REDSTONE_TORCH_ON);
        new HBlock(HBlock.Type.REDSTONE_TORCH_OFF,
                new Drops(new ItemStack[]{Items.simpleItem(Material.REDSTONE_TORCH_ON)}));
        new HBlock(HBlock.Type.STONE_BUTTON);
        new HBlock(HBlock.Type.SNOW_LAYER,
                new Material[]{Material.WOOD_SPADE, Material.STONE_SPADE, Material.IRON_SPADE, Material.GOLD_SPADE, Material.DIAMOND_SPADE},
                new Drops(new ItemStack[]{Items.simpleItem(Material.SNOW_BALL)}, new ItemStack[]{Items.simpleItem(HBlock.Type.SNOW_BLOCK)}));
        new HBlock(HBlock.Type.SNOW_BLOCK,
                new Material[]{Material.WOOD_SPADE, Material.STONE_SPADE, Material.IRON_SPADE, Material.GOLD_SPADE, Material.DIAMOND_SPADE},
                new Drops(new ItemStack[]{Items.simpleItem(Material.SNOW_BALL, 0, (byte)4)}, new ItemStack[]{Items.simpleItem(HBlock.Type.SNOW_BLOCK)}));
        new HBlock(HBlock.Type.ICE,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.ICE)}),
                HBlock.Type.WATER);
        new HBlock(HBlock.Type.CACTUS);
        new HBlock(HBlock.Type.CLAY,
                new Drops(new ItemStack[]{Items.simpleItem(Material.CLAY_BALL, 0, (byte)4)}, new ItemStack[]{Items.simpleItem(HBlock.Type.CLAY)}));
        new HBlock(HBlock.Type.SUGAR_CANE,
                new Drops(new ItemStack[]{Items.simpleItem(Material.SUGAR_CANE)}));
        new HBlock(HBlock.Type.JUKEBOX);
        new HBlock(HBlock.Type.OAK_FENCE);
        new HBlock(HBlock.Type.PUMPKIN);
        new HBlock(HBlock.Type.NETHERRACK,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.SOUL_SAND);
        new HBlock(HBlock.Type.GLOWSTONE,
                new Drops(new ItemStack[]{Items.simpleItem(Material.GLOWSTONE_DUST)}, new short[][]{{10000, 10000, 5000, 5000}}, new ItemStack[]{Items.simpleItem(Material.GLOWSTONE_DUST)}, new short[][][]{{{10000, 10000, 7500, 5000}, {10000, 10000, 8000, 6000}, {10000, 10000, 10000, 6000}}}, new ItemStack[]{Items.simpleItem(HBlock.Type.GLOWSTONE)}, new short[][]{{10000}}));
        new HBlock(HBlock.Type.NETHER_PORTAL,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}));
        new HBlock(HBlock.Type.GLOWING_PUMPKIN);
        new HBlock(HBlock.Type.CAKE,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}));
        new HBlock(HBlock.Type.REDSTONE_REPEATER_ON,
                new Drops(new ItemStack[]{Items.simpleItem(Material.DIODE)}));
        new HBlock(HBlock.Type.REDSTONE_REPEATER_OFF,
                new Drops(new ItemStack[]{Items.simpleItem(Material.DIODE)}));
        new HBlock(HBlock.Type.WHITE_GLASS,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.WHITE_GLASS)}));
        new HBlock(HBlock.Type.ORANGE_GLASS,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.ORANGE_GLASS)}));
        new HBlock(HBlock.Type.MAGENTA_GLASS,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.MAGENTA_GLASS)}));
        new HBlock(HBlock.Type.LIGHT_BLUE_GLASS,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.LIGHT_BLUE_GLASS)}));
        new HBlock(HBlock.Type.YELLOW_GLASS,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.YELLOW_GLASS)}));
        new HBlock(HBlock.Type.LIME_GLASS,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.LIME_GLASS)}));
        new HBlock(HBlock.Type.PINK_GLASS,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.PINK_GLASS)}));
        new HBlock(HBlock.Type.GRAY_GLASS,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.GRAY_GLASS)}));
        new HBlock(HBlock.Type.LIGHT_GRAY_GLASS,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.LIGHT_GRAY_GLASS)}));
        new HBlock(HBlock.Type.CYAN_GLASS,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.CYAN_GLASS)}));
        new HBlock(HBlock.Type.PURPLE_GLASS,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.PURPLE_GLASS)}));
        new HBlock(HBlock.Type.BLUE_GLASS,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.BLUE_GLASS)}));
        new HBlock(HBlock.Type.BROWN_GLASS,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.BROWN_GLASS)}));
        new HBlock(HBlock.Type.GREEN_GLASS,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.GREEN_GLASS)}));
        new HBlock(HBlock.Type.RED_GLASS,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.RED_GLASS)}));
        new HBlock(HBlock.Type.BLACK_GLASS,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.BLACK_GLASS)}));
        new HBlock(HBlock.Type.OAK_TRAPDOOR);
        new HBlock(HBlock.Type.STONE_SILVERFISH,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.STONE)}),
                EntityType.SILVERFISH);
        new HBlock(HBlock.Type.COBBLESTONE_SILVERFISH,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.COBBLESTONE)}),
                EntityType.SILVERFISH);
        new HBlock(HBlock.Type.STONEBRICK_SILVERFISH,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.STONEBRICK)}),
                EntityType.SILVERFISH);
        new HBlock(HBlock.Type.MOSSY_STONEBRICK_SILVERFISH,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.MOSSY_STONEBRICK)}),
                EntityType.SILVERFISH);
        new HBlock(HBlock.Type.CRACKED_STONEBRICK_SILVERFISH,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.CRACKED_STONEBRICK)}),
                EntityType.SILVERFISH);
        new HBlock(HBlock.Type.CHISELED_STONEBRICK_SILVERFISH,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.CHISELED_STONEBRICK)}),
                EntityType.SILVERFISH);
        new HBlock(HBlock.Type.STONEBRICK,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.MOSSY_STONEBRICK,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.CRACKED_STONEBRICK,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.CHISELED_STONEBRICK,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.GIANT_BROWN_MUSHROOM,
                new Drops(new ItemStack[]{Items.simpleItem(Material.BROWN_MUSHROOM)}, new short[][]{{1667, 1666}}, new ItemStack[]{Items.simpleItem(Material.BROWN_MUSHROOM)}, new short[][][]{{{1334, 1333, 1333}, {1250, 1250, 1250, 1250}, {1200, 1200, 1200, 1200, 1200}}}, new ItemStack[]{Items.simpleItem(HBlock.Type.GIANT_BROWN_MUSHROOM)}, new short[][]{{10000}}));
        new HBlock(HBlock.Type.GIANT_RED_MUSHROOM,
                new Drops(new ItemStack[]{Items.simpleItem(Material.RED_MUSHROOM)}, new short[][]{{1667, 1666}}, new ItemStack[]{Items.simpleItem(Material.RED_MUSHROOM)}, new short[][][]{{{1334, 1333, 1333}, {1250, 1250, 1250, 1250}, {1200, 1200, 1200, 1200, 1200}}}, new ItemStack[]{Items.simpleItem(HBlock.Type.GIANT_RED_MUSHROOM)}, new short[][]{{10000}}));
        new HBlock(HBlock.Type.IRON_BARS,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.GLASS_PANE,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.GLASS_PANE)}));
        new HBlock(HBlock.Type.MELON,
                new Drops(new ItemStack[]{Items.simpleItem(Material.MELON)}, new short[][]{{10000, 10000, 10000, 5000, 5000, 5000, 5000}}, new ItemStack[]{Items.simpleItem(Material.MELON)}, new short[][][]{{{10000, 10000, 10000, 5000, 5000, 5000, 5000, 5000}, {10000, 10000, 10000, 5000, 5000, 5000, 5000, 5000, 5000}, {10000, 10000, 10000, 6000, 6000, 6000, 6000, 6000, 6000}}}, new ItemStack[]{Items.simpleItem(HBlock.Type.MELON)}, new short[][]{{10000}}));
        new HBlock(HBlock.Type.MELON_CROPS,
                new Drops(new ItemStack[]{Items.simpleItem(Material.MELON_SEEDS)}, new short[][]{{2500}}, new ItemStack[]{Items.simpleItem(Material.MELON_SEEDS)}, new short[][][]{{{3000}, {4000}, {5000}}}, new ItemStack[]{Items.simpleItem(Material.MELON_SEEDS)}, new short[][]{{10000}}));
        new HBlock(HBlock.Type.MELON_CROPS_MATURE,
                new Drops(new ItemStack[]{Items.simpleItem(Material.MELON_SEEDS)}, new short[][]{{10000, 5000}}, new ItemStack[]{Items.simpleItem(Material.MELON_SEEDS)}, new short[][][]{{{10000, 6000}, {10000, 7000, 1000}, {10000, 8000, 3000}}}, new ItemStack[]{Items.simpleItem(Material.MELON_SEEDS)}, new short[][]{{10000}}));
        new HBlock(HBlock.Type.PUMPKIN_CROPS,
                new Drops(new ItemStack[]{Items.simpleItem(Material.PUMPKIN_SEEDS)}, new short[][]{{2500}}, new ItemStack[]{Items.simpleItem(Material.PUMPKIN_SEEDS)}, new short[][][]{{{3000}, {4000}, {5000}}}, new ItemStack[]{Items.simpleItem(Material.PUMPKIN_SEEDS)}, new short[][]{{10000}}));
        new HBlock(HBlock.Type.PUMPKIN_CROPS_MATURE,
                new Drops(new ItemStack[]{Items.simpleItem(Material.PUMPKIN_SEEDS)}, new short[][]{{10000, 5000}}, new ItemStack[]{Items.simpleItem(Material.PUMPKIN_SEEDS)}, new short[][][]{{{10000, 6000}, {10000, 7000, 1000}, {10000, 8000, 3000}}}, new ItemStack[]{Items.simpleItem(Material.PUMPKIN_SEEDS)}, new short[][]{{10000}}));
        new HBlock(HBlock.Type.VINE,
                new Material[]{Material.SHEARS});
        new HBlock(HBlock.Type.OAK_FENCE_GATE);
        new HBlock(HBlock.Type.MYCELIUM,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.DIRT)}, new ItemStack[]{Items.simpleItem(HBlock.Type.MYCELIUM)}));
        new HBlock(HBlock.Type.LILYPAD);
        new HBlock(HBlock.Type.NETHER_BRICK,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.NETHER_BRICK_FENCE,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.NETHER_BRICK_STAIR,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.NETHER_WART,
                new Drops(new ItemStack[]{Items.simpleItem(Material.NETHER_STALK)}));
        new HBlock(HBlock.Type.NETHER_WART_MATURE,
                new Drops(new ItemStack[]{Items.simpleItem(Material.NETHER_STALK)}, new short[][]{{10000, 10000, 5000, 5000}}, new ItemStack[]{Items.simpleItem(Material.NETHER_STALK)}, new short[][][]{{{10000, 10000, 5000, 5000, 5000}, {10000, 10000, 5000, 5000, 5000, 5000}, {10000, 10000, 5000, 5000, 5000, 5000, 5000}}}, new ItemStack[]{Items.simpleItem(Material.NETHER_STALK)}, new short[][]{{10000, 10000, 10000, 10000}}));
        new HBlock(HBlock.Type.ENCHANTMENT_TABLE,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.BREWING_STAND,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE},
                new Drops(new ItemStack[]{Items.simpleItem(Material.BREWING_STAND_ITEM)}));
        new HBlock(HBlock.Type.CAULDRON,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.END_PORTAL,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}));
        new HBlock(HBlock.Type.END_STONE,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.DRAGON_EGG);
        new HBlock(HBlock.Type.REDSTONE_LAMP_ON,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.REDSTONE_LAMP_OFF)}));
        new HBlock(HBlock.Type.REDSTONE_LAMP_OFF);
        new HBlock(HBlock.Type.DOUBLE_OAK_SLAB,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.OAK_SLAB, (byte)2)}));
        new HBlock(HBlock.Type.DOUBLE_SPRUCE_SLAB,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.SPRUCE_SLAB, (byte)2)}));
        new HBlock(HBlock.Type.DOUBLE_BIRCH_SLAB,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.BIRCH_SLAB, (byte)2)}));
        new HBlock(HBlock.Type.DOUBLE_JUNGLE_SLAB,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.JUNGLE_SLAB, (byte)2)}));
        new HBlock(HBlock.Type.DOUBLE_ACACIA_SLAB,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.ACACIA_SLAB, (byte)2)}));
        new HBlock(HBlock.Type.DOUBLE_DARK_SLAB,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.DARK_SLAB, (byte)2)}));
        new HBlock(HBlock.Type.OAK_SLAB);
        new HBlock(HBlock.Type.SPRUCE_SLAB);
        new HBlock(HBlock.Type.BIRCH_SLAB);
        new HBlock(HBlock.Type.JUNGLE_SLAB);
        new HBlock(HBlock.Type.ACACIA_SLAB);
        new HBlock(HBlock.Type.DARK_SLAB);
        new HBlock(HBlock.Type.COCOA,
                new Drops(new ItemStack[]{Items.simpleItem(Material.INK_SACK, 3)}));
        new HBlock(HBlock.Type.COCOA_MATURE,
                new Drops(new ItemStack[]{Items.simpleItem(Material.INK_SACK, 3)}, new short[][]{{10000, 10000, 5000}}, new ItemStack[]{Items.simpleItem(Material.INK_SACK, 3)}, new short[][][]{{{10000, 10000, 5000, 5000}, {10000, 10000, 7000, 7000}, {10000, 10000, 6000, 6000, 4000}}}, new ItemStack[]{Items.simpleItem(Material.INK_SACK, 3)}, new short[][]{{10000, 10000, 10000}}));
        new HBlock(HBlock.Type.SANDSTONE_STAIR,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.ENDER_CHEST,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE},
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.OBSIDIAN, (byte)8)}, new ItemStack[]{Items.simpleItem(HBlock.Type.ENDER_CHEST)}));
        new HBlock(HBlock.Type.STRING_WIRE_HOOK);
        new HBlock(HBlock.Type.STRING_WIRE,
                new Drops(new ItemStack[]{Items.simpleItem(Material.STRING)}));
        new HBlock(HBlock.Type.EMERALD_BLOCK,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.SPRUCE_STAIR);
        new HBlock(HBlock.Type.BIRCH_STAIR);
        new HBlock(HBlock.Type.JUNGLE_STAIR);
        new HBlock(HBlock.Type.COMMAND_BLOCK,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}));
        new HBlock(HBlock.Type.BEACON,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.COBBLESTONE_WALL,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.MOSSY_COBBLESTONE_WALL,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.FLOWER_POT,
                new Drops(new ItemStack[]{Items.simpleItem(Material.FLOWER_POT_ITEM)}));
        new HBlock(HBlock.Type.CARROT_CROPS,
                new Drops(new ItemStack[]{Items.simpleItem(Material.CARROT_ITEM)}));
        new HBlock(HBlock.Type.CARROT_CROPS_MATURE,
                new Drops(new ItemStack[]{Items.simpleItem(Material.CARROT_ITEM)}, new short[][]{{10000, 5000, 5000, 5000}}, new ItemStack[]{Items.simpleItem(Material.CARROT_ITEM)}, new short[][][]{{{10000, 5000, 5000, 5000, 5000}, {10000, 6000, 6000, 6000, 6000}, {10000, 5000, 5000, 5000, 5000, 5000}}}, new ItemStack[]{Items.simpleItem(Material.CARROT_ITEM)}, new short[][]{{10000, 10000}}));
        new HBlock(HBlock.Type.POTATO_CROPS,
                new Drops(new ItemStack[]{Items.simpleItem(Material.POTATO_ITEM)}));
        new HBlock(HBlock.Type.POTATO_CROPS_MATURE,
                new Drops(new ItemStack[]{Items.simpleItem(Material.POTATO_ITEM)}, new short[][]{{10000, 5000, 5000, 5000}}, new ItemStack[]{Items.simpleItem(Material.POTATO_ITEM)}, new short[][][]{{{10000, 5000, 5000, 5000, 5000}, {10000, 6000, 6000, 6000, 6000}, {10000, 5000, 5000, 5000, 5000, 5000}}}, new ItemStack[]{Items.simpleItem(Material.POTATO_ITEM)}, new short[][]{{10000, 10000}}));
        new HBlock(HBlock.Type.OAK_BUTTON);
        new HBlock(HBlock.Type.HEAD);
        new HBlock(HBlock.Type.ANVIL,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.TRAPPED_CHEST);
        new HBlock(HBlock.Type.GOLD_PRESSURE_PLATE,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.IRON_PRESSURE_PLATE,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.REDSTONE_COMPARATOR_ON,
                new Drops(new ItemStack[]{Items.simpleItem(Material.REDSTONE_COMPARATOR)}));
        new HBlock(HBlock.Type.REDSTONE_COMPARATOR_OFF,
                new Drops(new ItemStack[]{Items.simpleItem(Material.REDSTONE_COMPARATOR)}));
        new HBlock(HBlock.Type.DAYLIGHT_SENSOR);
        new HBlock(HBlock.Type.REDSTONE_BLOCK,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.HOPPER,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.QUARTZ,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.CHISELED_QUARTZ,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.PILLAR_QUARTZ,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.QUARTZ_STAIR,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.ACTIVATOR_RAIL);
        new HBlock(HBlock.Type.DROPPER,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.WHITE_HARDENED_CLAY,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.ORANGE_HARDENED_CLAY,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.MAGENTA_HARDENED_CLAY,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.LIGHT_BLUE_HARDENED_CLAY,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.YELLOW_HARDENED_CLAY,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.LIME_HARDENED_CLAY,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.PINK_HARDENED_CLAY,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.GRAY_HARDENED_CLAY,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.LIGHT_GRAY_HARDENED_CLAY,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.CYAN_HARDENED_CLAY,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.PURPLE_HARDENED_CLAY,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.BLUE_HARDENED_CLAY,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.BROWN_HARDENED_CLAY,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.GREEN_HARDENED_CLAY,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.RED_HARDENED_CLAY,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.BLACK_HARDENED_CLAY,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.WHITE_GLASS_PANE,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.WHITE_GLASS_PANE)}));
        new HBlock(HBlock.Type.ORANGE_GLASS_PANE,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.ORANGE_GLASS_PANE)}));
        new HBlock(HBlock.Type.MAGENTA_GLASS_PANE,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.MAGENTA_GLASS_PANE)}));
        new HBlock(HBlock.Type.LIGHT_BLUE_GLASS_PANE,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.LIGHT_BLUE_GLASS_PANE)}));
        new HBlock(HBlock.Type.YELLOW_GLASS_PANE,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.YELLOW_GLASS_PANE)}));
        new HBlock(HBlock.Type.LIME_GLASS_PANE,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.LIME_GLASS_PANE)}));
        new HBlock(HBlock.Type.PINK_GLASS_PANE,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.PINK_GLASS_PANE)}));
        new HBlock(HBlock.Type.GRAY_GLASS_PANE,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.GRAY_GLASS_PANE)}));
        new HBlock(HBlock.Type.LIGHT_GRAY_GLASS_PANE,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.LIGHT_GRAY_GLASS_PANE)}));
        new HBlock(HBlock.Type.CYAN_GLASS_PANE,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.CYAN_GLASS_PANE)}));
        new HBlock(HBlock.Type.PURPLE_GLASS_PANE,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.PURPLE_GLASS_PANE)}));
        new HBlock(HBlock.Type.BLUE_GLASS_PANE,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.BLUE_GLASS_PANE)}));
        new HBlock(HBlock.Type.BROWN_GLASS_PANE,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.BROWN_GLASS_PANE)}));
        new HBlock(HBlock.Type.GREEN_GLASS_PANE,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.GREEN_GLASS_PANE)}));
        new HBlock(HBlock.Type.RED_GLASS_PANE,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.RED_GLASS_PANE)}));
        new HBlock(HBlock.Type.BLACK_GLASS_PANE,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.BLACK_GLASS_PANE)}));
        new HBlock(HBlock.Type.ACACIA_STAIR);
        new HBlock(HBlock.Type.DARK_STAIR);
        new HBlock(HBlock.Type.SLIME);
        new HBlock(HBlock.Type.BARRIER,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}));
        new HBlock(HBlock.Type.IRON_TRAPDOOR,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.PRISMARINE,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.PRISMARINE_BRICK,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.PRISMARINE_DARK,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.SEA_LANTERN,
                new Drops(new ItemStack[]{Items.simpleItem(Material.PRISMARINE_SHARD)}, new short[][]{{10000, 10000, 5000}}, new ItemStack[]{Items.simpleItem(Material.PRISMARINE_SHARD)}, new short[][][]{{{10000, 10000, 5000, 5000}, {10000, 10000, 5000, 5000, 5000}, {10000, 10000, 6000, 6000, 6000}}}, new ItemStack[]{Items.simpleItem(HBlock.Type.SEA_LANTERN)}, new short[][]{{10000}}));
        new HBlock(HBlock.Type.HAY);
        new HBlock(HBlock.Type.WHITE_CARPET);
        new HBlock(HBlock.Type.ORANGE_CARPET);
        new HBlock(HBlock.Type.MAGENTA_CARPET);
        new HBlock(HBlock.Type.LIGHT_BLUE_CARPET);
        new HBlock(HBlock.Type.YELLOW_CARPET);
        new HBlock(HBlock.Type.LIME_CARPET);
        new HBlock(HBlock.Type.PINK_CARPET);
        new HBlock(HBlock.Type.GRAY_CARPET);
        new HBlock(HBlock.Type.LIGHT_GRAY_CARPET);
        new HBlock(HBlock.Type.CYAN_CARPET);
        new HBlock(HBlock.Type.PURPLE_CARPET);
        new HBlock(HBlock.Type.BLUE_CARPET);
        new HBlock(HBlock.Type.BROWN_CARPET);
        new HBlock(HBlock.Type.GREEN_CARPET);
        new HBlock(HBlock.Type.RED_CARPET);
        new HBlock(HBlock.Type.BLACK_CARPET);
        new HBlock(HBlock.Type.HARDENED_CLAY,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.COAL_BLOCK,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.PACKED_ICE,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}, new ItemStack[]{Items.simpleItem(HBlock.Type.PACKED_ICE)}));
        new HBlock(HBlock.Type.SUNFLOWER);
        new HBlock(HBlock.Type.LILAC);
        new HBlock(HBlock.Type.HUGE_GRASS,
                new Drops(new ItemStack[]{Items.simpleItem(Material.SEEDS)}, new short[][]{{1250}}, new ItemStack[]{Items.simpleItem(Material.SEEDS)}, new short[][][]{{{1250, 1250, 1250}, {1250, 1250, 1250, 1250, 1250}, {1250, 1250, 1250, 1250, 1250, 1250, 1250}}}, new ItemStack[]{Items.simpleItem(HBlock.Type.HUGE_GRASS)}, new short[][]{{10000}}, new ItemStack[]{Items.simpleItem(HBlock.Type.HUGE_GRASS), Items.simpleItem(Material.SEEDS)}, new Material[][]{{Material.SHEARS}, {Material.WOOD_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.GOLD_HOE, Material.DIAMOND_HOE}}, new short[][]{{10000}, {2500}}));
        new HBlock(HBlock.Type.HUGE_FERN,
                new Drops(new ItemStack[]{Items.simpleItem(Material.SEEDS)}, new short[][]{{1250}}, new ItemStack[]{Items.simpleItem(Material.SEEDS)}, new short[][][]{{{1250, 1250, 1250}, {1250, 1250, 1250, 1250, 1250}, {1250, 1250, 1250, 1250, 1250, 1250, 1250}}}, new ItemStack[]{Items.simpleItem(HBlock.Type.HUGE_FERN)}, new short[][]{{10000}}, new ItemStack[]{Items.simpleItem(HBlock.Type.HUGE_FERN), Items.simpleItem(Material.SEEDS)}, new Material[][]{{Material.SHEARS}, {Material.WOOD_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.GOLD_HOE, Material.DIAMOND_HOE}}, new short[][]{{10000}, {2500}}));
        new HBlock(HBlock.Type.ROSE_BUSH);
        new HBlock(HBlock.Type.PEONY_BUSH);
        new HBlock(HBlock.Type.GROUND_BANNER);
        new HBlock(HBlock.Type.WALL_BANNER);
        new HBlock(HBlock.Type.MOON_SENSOR,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.DAYLIGHT_SENSOR)}));
        new HBlock(HBlock.Type.RED_SANDSTONE,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.CHISELED_RED_SANDSTONE,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.SMOOTH_RED_SANDSTONE,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.RED_SANDSTONE_STAIR,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.DOUBLE_RED_SANDSTONE_SLAB,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE},
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.RED_SANDSTONE_SLAB, (byte)2)}));
        new HBlock(HBlock.Type.RED_SANDSTONE_SLAB,
                new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE});
        new HBlock(HBlock.Type.SPRUCE_FENCE);
        new HBlock(HBlock.Type.SPRUCE_FENCE_GATE);
        new HBlock(HBlock.Type.BIRCH_FENCE);
        new HBlock(HBlock.Type.BIRCH_FENCE_GATE);
        new HBlock(HBlock.Type.JUNGLE_FENCE);
        new HBlock(HBlock.Type.JUNGLE_FENCE_GATE);
        new HBlock(HBlock.Type.DARK_FENCE);
        new HBlock(HBlock.Type.DARK_FENCE_GATE);
        new HBlock(HBlock.Type.ACACIA_FENCE);
        new HBlock(HBlock.Type.ACACIA_FENCE_GATE);
        new HBlock(HBlock.Type.SPRUCE_DOOR,
                new Drops(new ItemStack[]{Items.simpleItem(Material.SPRUCE_DOOR_ITEM)}));
        new HBlock(HBlock.Type.BIRCH_DOOR,
                new Drops(new ItemStack[]{Items.simpleItem(Material.BIRCH_DOOR_ITEM)}));
        new HBlock(HBlock.Type.JUNGLE_DOOR,
                new Drops(new ItemStack[]{Items.simpleItem(Material.JUNGLE_DOOR_ITEM)}));
        new HBlock(HBlock.Type.ACACIA_DOOR,
                new Drops(new ItemStack[]{Items.simpleItem(Material.ACACIA_DOOR_ITEM)}));
        new HBlock(HBlock.Type.DARK_DOOR,
                new Drops(new ItemStack[]{Items.simpleItem(Material.DARK_OAK_DOOR_ITEM)}));
        new HBlock(HBlock.Type.END_PORTAL_FRAME,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}));
        new HBlock(HBlock.Type.DOUBLE_PLANT_TECHNICAL,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}));
        new HBlock(HBlock.Type.AIR);
        new HBlock(HBlock.Type.OAK_DOOR_TOP,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}));
        new HBlock(HBlock.Type.ACACIA_DOOR_TOP,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}));
        new HBlock(HBlock.Type.BIRCH_DOOR_TOP,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}));
        new HBlock(HBlock.Type.DARK_DOOR_TOP,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}));
        new HBlock(HBlock.Type.IRON_DOOR_TOP,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}));
        new HBlock(HBlock.Type.JUNGLE_DOOR_TOP,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}));
        new HBlock(HBlock.Type.SPRUCE_DOOR_TOP,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.AIR)}));
        new HBlock(HBlock.Type.ANVIL_DAMAGED,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.ANVIL_DAMAGED)}));
        new HBlock(HBlock.Type.ANVIL_VERY_DAMAGED,
                new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.ANVIL_VERY_DAMAGED)}));
    }

}
