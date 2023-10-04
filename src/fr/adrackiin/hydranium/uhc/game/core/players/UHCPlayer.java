package fr.adrackiin.hydranium.uhc.game.core.players;

import fr.adrackiin.api.api.player.APlayer;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.UHCPlayerDeathEvent;
import fr.adrackiin.hydranium.uhc.events.UHCPlayerReviveEvent;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.state.GameState;
import fr.adrackiin.hydranium.uhc.game.team.GameTeam;
import fr.adrackiin.hydranium.uhc.guis.AGPlayer;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import fr.adrackiin.hydranium.uhc.utils.minecraft.Blocks;
import fr.adrackiin.hydranium.uhc.utils.minecraft.Items;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.NameTagVisibility;

import java.util.*;

public class UHCPlayer extends APlayer {
    
    private UHCPlayerStatus status = UHCPlayerStatus.BEFORE;
    private boolean isHost = false;
    private Location spawnLocation;
    private GameState decoGameState;
    private Integer offlineFood;
    private Integer offlineLevel;
    private Float offlineSaturation;
    private Location offlineLocation;
    private ItemStack[] offlineInventory;
    private ItemStack[] offlineArmor;
    private final ArrayList<PotionEffect> offlineEffect = new ArrayList<>();
    private GameTeam gameTeam = null;
    private int decoTaskId;
    private boolean canRecconect = true;
    private UUID lastDamager = null;
    private int lastDamage = 0;
    private final boolean hasBeenTeleported = false;
    private UUID killer;
    private int kills;
    private int diamond;
    private int gold;
    private EntityType lastEntityAttack;
    private final Set<GameTeam> invitedInTeams = new HashSet<>();
    private int id;

    public UHCPlayer(UUID uuid, String name){
        super(uuid, name);
    }

    public void setDead() {
        this.setStatus(UHCPlayerStatus.DEAD);
    }

    public void setPlaying() {
        this.setStatus(UHCPlayerStatus.PLAYING);
    }

    public void setSpectator() {
        this.setStatus(UHCPlayerStatus.SPECTATOR);
    }

    public boolean isStatus(UHCPlayerStatus status){
        return this.getStatus() == status;
    }

    public void addDiamond(int amount){
        this.diamond += amount;
        this.getAPPlayer().getStats().add("diamond-mined", amount);
    }

    public void addGold(int amount){
        this.gold += amount;
        this.getAPPlayer().getStats().add("gold-mined", amount);
    }

    public void addKill(int amount){
        this.kills += amount;
        this.getAPPlayer().getStats().add("killed-players", amount);
        Game.getGame().getScoreboardManager().setKill(this);
        if(Game.getGame().isTeam() && this.hasTeam()){
            this.getGameTeam().addKills(amount);
            Game.getGame().getScoreboardManager().setTeamKill(this);
        }
        Game.getGame().getScoreboardManager().setTopKillScoreboard(this);
    }

    public boolean hasTeam(){
        return this.gameTeam != null;
    }

    public boolean isInvitedInTeam(GameTeam team){
        return invitedInTeams.contains(team);
    }

    public void setInvisible(){
        if(this.hasTeam()){
            this.getGameTeam().getInvisible().addEntry(this.getAPPlayer().getName());
        } else {
            Bukkit.getScoreboardManager().getMainScoreboard().getTeam("uhcffa" + id).setNameTagVisibility(NameTagVisibility.NEVER);
        }
    }

    public void setVisible(){
        if(this.hasTeam()){
            this.getGameTeam().getTeam().addEntry(this.getAPPlayer().getName());
        } else {
            Bukkit.getScoreboardManager().getMainScoreboard().getTeam("uhcffa" + id).setNameTagVisibility(NameTagVisibility.ALWAYS);
        }
    }

    public boolean canReconnect() {
        return canRecconect;
    }

    public boolean hasDisconnectTeleportation(){
        return this.getDecoGameState() == GameState.TELEPORATTION;
    }

    public boolean hasFindOre(Block block){
        switch(block.getType()){
            case DIAMOND_ORE:
                if(block.hasMetadata("DiamondVeinPlayer")) {
                    return block.getMetadata("DiamondVeinPlayer").get(0).asString().equals(this.getAPPlayer().getName());
                }
                return false;
            case GOLD_ORE:
                if(block.hasMetadata("GoldVeinPlayer")) {
                    return block.getMetadata("GoldVeinPlayer").get(0).asString().equals(this.getAPPlayer().getName());
                }
                return false;
            default:
                return false;
        }
    }

    public boolean hasPlacedBlock(Block block) {
        if(block.hasMetadata("BlockPlacePlayer")){
            for(MetadataValue v : block.getMetadata("BlockPlacePlayer")){
                if(v.value() == this.getAPPlayer().getUUID()){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isPlacedBlock(Block block){
        return block.hasMetadata("BlockPlacePlayer");
    }

    public void kill(UHCPlayer killer, EntityType killerPVE, boolean offline, EntityDamageEvent.DamageCause cause, DeathCause deathCause, boolean keepInventory, ItemStack[] loots, ItemStack[] armor, boolean cancellable) {
        UHCCore.getPlugin().getServer().getPluginManager().callEvent(new UHCPlayerDeathEvent(this, killer, killerPVE, offline, cause, deathCause, keepInventory, loots, armor, cancellable));
    }

    public void offlineKill() {
        if(!this.isPlaying() || this.getAPPlayer().isConnected()){
            return;
        }
        setCanRecconect(false);
        kill(null, null, true, EntityDamageEvent.DamageCause.CUSTOM, DeathCause.OFFLINE, false, this.getOfflineInventory(), this.getOfflineArmor(), true);
    }

    public void revive() {
        if(!this.isStatus(UHCPlayerStatus.DEAD)){
            try {
                APICore.getPlugin().getChannelManager().getChannel("channel.host").sendMessage("§cUne tentative de Revive a échoué, " + this.getAPPlayer().getName() + " n'est pas mort ou est en spectateur");
            } catch(ChannelNotFoundException e) {
                e.printStackTrace();
                return;
            }
            return;
        }
        this.getAPPlayer().setGameMode(GameMode.SURVIVAL);
        int x = UHCCore.getRandom().nextInt(Game.getGame().getSettings().getSize() - 60) + 50;
        int z = UHCCore.getRandom().nextInt(Game.getGame().getSettings().getSize() - 60) + 50;
        this.getAPPlayer().teleport(new Location(Game.getGame().getWorldUHC().getWorld(), x + 0.5D, Blocks.getHighestBlock(Game.getGame().getWorldUHC().getWorld(), x, z) + 1.5D, z + 0.5D));
        this.getAPPlayer().getInventory().setContents(getOfflineInventory());
        this.getAPPlayer().getInventory().setArmorContents(getOfflineArmor());
        this.getAPPlayer().setHealth(20.0);
        this.getAPPlayer().setFoodLevel(getOfflineFood());
        this.getAPPlayer().setSaturation(getOfflineSaturation());
        this.getAPPlayer().setLevel(getOfflineLevel());
        this.getAPPlayer().addPotionsEffect(getOfflineEffect());
        this.setCanRecconect(true);
        this.setPlaying();
        if(killer != null) {
            Game.getGame().getUHCPlayer(this.killer).addKill(-1);
        } else {
            Game.getGame().addPveDeaths(-1);
        }
        if(Game.getGame().isTeam()){
            if(!Game.getGame().getTeamManager().getAliveTeams().contains(this.getGameTeam())){
                Bukkit.broadcastMessage(Prefix.uhc + "§7L'équipe " + this.getGameTeam().getPrefix() + this.getGameTeam().getName() + "§7 est revenu §cà la vie");
                Game.getGame().getTeamManager().getAliveTeams().add(this.getGameTeam());
            }
            Game.getGame().getTeamManager().getAliveTeams().add(this.getGameTeam());
            this.getGameTeam().getAliveMembers().add(this.getAPPlayer().getUUID());
        }
        UHCCore.getPlugin().getServer().getPluginManager().callEvent(new UHCPlayerReviveEvent(this));
    }

    public void createScoreboard(){
        if(Game.getGame().isScUHC()){
            Game.getGame().getScoreboardManager().createScoreboardMain(this);
        }
    }

    public void destroyScoreboard(){
        Game.getGame().getScoreboardManager().destroy(this.getAPPlayer());
    }

    public void savePlayer(){
        this.offlineLocation = this.getAPPlayer().getLocation();
        this.offlineInventory = this.getAPPlayer().getInventory().getContents();
        this.offlineArmor = this.getAPPlayer().getInventory().getArmorContents();
        this.offlineLevel = this.getAPPlayer().getLevel();
        this.offlineFood = this.getAPPlayer().getFoodLevel();
        this.offlineSaturation = this.getAPPlayer().getSaturation();
        offlineEffect.clear();
        offlineEffect.addAll(this.getAPPlayer().getActivePotionEffects());
    }

    public void openInventoryPlayer(UHCPlayer target){
        Inventory invShow = Bukkit.createInventory(null, 54, "§cInventaire de " + target.getAPPlayer().getName());
        if(target.getAPPlayer().isConnected()){
            PlayerInventory targetInventory = target.getAPPlayer().getInventory();
            for (int i = 18; i < 45; i++) {
                invShow.setItem(i, targetInventory.getItem(i - 9));
            }
            for (int i = 45; i < 54; i++) {
                invShow.setItem(i, targetInventory.getItem(i - 45));
            }
            invShow.setItem(0, targetInventory.getHelmet());
            invShow.setItem(1, targetInventory.getChestplate());
            invShow.setItem(2, targetInventory.getLeggings());
            invShow.setItem(3, targetInventory.getBoots());
        } else {
            for (int i = 18; i < 45; i++) {
                invShow.setItem(i, target.getOfflineInventory()[i - 9]);
            }
            for (int i = 45; i < 54; i++) {
                invShow.setItem(i, target.getOfflineInventory()[i - 45]);
            }
            for(int i = target.getOfflineArmor().length - 1; i >= 0; i--){
                invShow.setItem(i, target.getOfflineArmor()[i]);
            }
        }
        invShow.setItem(13, Items.getHead("§6" + "Stats", 1, Arrays.asList(
                "§7" + "§7Stats de " + "§6" + target.getAPPlayer().getName(),
                "§7" + "§7Ping: " + "§6" + target.getAPPlayer().getPing(),
                "",
                "§7" + "§7Ors minés: " + "§6" + target.getGold(),
                "§7" + "§7Diamants minés: " + "§6" + target.getDiamond(),
                "",
                "§7" + "§7Kills: " + "§6" + target.getKills()
                /*"§7" + "§7Kills total: " + "§6" + target.getTotalKills()*/), target.getAPPlayer().getName()));
        this.getAPPlayer().openInventory(invShow);
    }

    public void joinTeam(GameTeam gameTeam){
        gameTeam.addPlayer(this.getAPPlayer().getUUID(), true);
    }

    public void leaveTeam(){
        this.getGameTeam().removePlayer(this.getAPPlayer().getUUID(), true);
    }

    public void addOfflinePlayer(){
        Game.getGame().addOfflinePlayer(this.getAPPlayer().getUUID());
    }

    public void removeOfflinePlayer(){
        Game.getGame().removeOfflinePlayer(this.getAPPlayer().getUUID());
    }

    public List<Player> checkStalk(double radius){
        ArrayList<Player> stalker = new ArrayList<>();
        Player player;
        int x = apPlayer.getLocation().getBlockX();
        int z = apPlayer.getLocation().getBlockZ();
        int x2;
        int z2;
        for(UUID uuid : Game.getGame().getAlivePlayers().copy()){
            player = Bukkit.getPlayer(uuid);
            x2 = x - player.getLocation().getBlockX();
            z2 = z - player.getLocation().getBlockZ();
            if (Math.sqrt(x2 * x2 + z2 * z2) < radius) {
                stalker.add(player);
            }
        }
        stalker.remove(this.getAPPlayer().getPlayer());
        return stalker;
    }

    public void startOfflineDeath() {
        decoTaskId = Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), this::offlineKill, 10 * 60 * 20L);
    }

    public void stopOfflineDeath(){
        Bukkit.getScheduler().cancelTask(decoTaskId);
    }

    public void resetSpawn(){
        this.getAPPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
        this.getAPPlayer().removePotionEffect(PotionEffectType.SLOW);
        this.getAPPlayer().removePotionEffect(PotionEffectType.WEAKNESS);
        this.getAPPlayer().removePotionEffect(PotionEffectType.JUMP);
        this.getAPPlayer().teleport(this.getSpawnLocation());
        this.getAPPlayer().setGameMode(GameMode.SURVIVAL);
        this.getAPPlayer().setFoodLevel(20);
        this.getAPPlayer().setSaturation(20.0F);
        this.getAPPlayer().clearInventory();
        this.getAPPlayer().setLevel(0);
        this.getAPPlayer().setBarLevel((byte)0);
    }

    public boolean isPlayingUHC() {
        return this.isConnected() && isPlaying();
    }

    public boolean isDead() {
        return this.status == UHCPlayerStatus.DEAD;
    }

    public boolean isPlaying() {
        return this.status == UHCPlayerStatus.PLAYING;
    }

    public boolean isSpectator() {
        return this.status == UHCPlayerStatus.SPECTATOR;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean host) {
        isHost = host;
        if(host) {
            if(Game.getGame().getHost() == null) {
                Game.getGame().getGameStateManager().setGameState(GameState.CONFIG);
                Game.getGame().setHost(this.getUuid());
                this.sendMessage(Prefix.uhc + "§7Bienvenue dans votre UHC. Vous pouvez à présent le configurer. Bon host !");
            }
            Game.getGame().getchannelHost().addPlayer(this);
            this.setStatus(UHCPlayerStatus.SPECTATOR);
            Game.getGame().getHostPlayers().add(this.getUuid())
            ((AGPlayer)APICore.getPlugin().getAPGuiManager().getSpecial("container.inventory")).giveHostTool(this.getAPPlayer().getPlayer());
            Game.getGame().getPlayerManager().giveHostPermissions(this.getAPPlayer().getUUID());
            this.setHelper(true);
            this.setOrespy(true);
            this.getAPPlayer().setSocialSpy(true);
        } else {
            try {
                APICore.getPlugin().getChannelManager().getChannel("channel.host").removePlayer(this.getAPPlayer().getUUID());
            } catch(ChannelNotFoundException e) {
                e.printStackTrace();
                return;
            }
            Game.getGame().getHostPlayers().remove(this.getAPPlayer().getUUID());
            /*try {
                ((APGPlayer)APICore.getPlugin().getAPGuiManager().getSpecial("container.inventory")).removeHostTool(this.getAPPlayer().getPlayer());
            } catch(APGuiNotFoundException e) {
                e.printStackTrace();
            }*/
            this.setHelper(false);
            this.setOrespy(false);
            this.getAPPlayer().setSocialSpy(false);
            Game.getGame().getPlayerManager().removeHostPermissions(this.getAPPlayer().getUUID());
            this.setStatus(UHCPlayerStatus.PLAYING);
        }
    }

    public UHCPlayerStatus getStatus() {
        return status;
    }

    public void setStatus(UHCPlayerStatus status) {
        UUID uuid = this.getAPPlayer().getUUID();
        Channel deadChannel;
        Channel specChannel;
        try {
            deadChannel = APICore.getPlugin().getChannelManager().getChannel("channel.dead");
            specChannel = APICore.getPlugin().getChannelManager().getChannel("channel.spec");
        } catch(ChannelNotFoundException e) {
            e.printStackTrace();
            return;
        }
        if(this.status != null){
            switch(this.status){
                case PLAYING:
                    Game.getGame().removePlayingPlayer(uuid);
                    break;
                case DEAD:
                    Game.getGame().getDeadPlayers().remove(uuid);
                    deadChannel.removePlayer(uuid);
                    this.getAPPlayer().showForOther(Bukkit.getOnlinePlayers());
                    break;
                case SPECTATOR:
                    Game.getGame().getSpectatorPlayers().remove(uuid);
                    specChannel.removePlayer(uuid);
                    if(!UHCCore.getPlugin().isDebug()){
                        if(this.getAPPlayer().isConnected()){
                            this.getAPPlayer().reset(new Location(Game.getGame().getWorldUHC().getWorld(), Game.getGame().getWorldUHC().getX() + 0.5, Game.getGame().getWorldUHC().getY() + 5.0, Game.getGame().getWorldUHC().getZ() + 0.5, 0L, 0L));
                        }
                    } else {
                        this.getAPPlayer().setGameMode(GameMode.SURVIVAL);
                    }
                    break;
            }
        }
        this.status = status;
        if(status != null){
            switch(status){
                case PLAYING:
                    Game.getGame().getTeamManager().setAlive(this);
                    if(this.canReconnect()){
                        this.stopOfflineDeath();
                    }
                    if(this.getAPPlayer().isConnected()){
                        Game.getGame().addPlayingPlayer(uuid);
                        this.getAPPlayer().hidePlayers(Game.getGame().getDeadPlayers().copy());
                        if(this.hasDisconnectTeleportation()){
                            this.resetSpawn();
                        }
                    }
                    break;
                case DEAD:
                    Game.getGame().getTeamManager().setDead(this);
                    if(this.getAPPlayer().isConnected()){
                        Game.getGame().getDeadPlayers().add(uuid);
                        deadChannel.addPlayer(uuid);
                        this.savePlayer();
                        this.getAPPlayer().setGameMode(GameMode.SPECTATOR);
                        this.getAPPlayer().sendMessage(Prefix.uhc + "§cVous êtes mort");
                        this.getAPPlayer().sendMessage(Prefix.uhc + "§7Merci d'avoir joué ! Vous pouvez rester ici pour voir le déroulement de la partie");
                        this.getAPPlayer().sendMessage(Prefix.uhc + "§7Les joueurs encore en vie ne peuvent pas voir vos messages");
                        this.getAPPlayer().hideForOther(Game.getGame().getAlivePlayers().copy());
                        Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), () -> {
                            if(!this.getAPPlayer().isConnected()){
                                return;
                            }
                            if(!this.getAPPlayer().hasPermission("hydranium.uhc.spec")){
                                this.getAPPlayer().reset(new Location(Game.getGame().getWorldUHC().getWorld(), Game.getGame().getWorldUHC().getX() + 0.5, Game.getGame().getWorldUHC().getY() + 5.0, Game.getGame().getWorldUHC().getZ() + 0.5, 0L, 0L));
                            } else {
                                this.setStatus(UHCPlayerStatus.SPECTATOR);
                            }
                        }, 20 * 5L);
                    }
                    break;
                case SPECTATOR:
                    if(this.getAPPlayer().isConnected()){
                        Game.getGame().getSpectatorPlayers().add(uuid);
                        specChannel.addPlayer(uuid);
                        this.getAPPlayer().setGameMode(GameMode.SPECTATOR);
                        this.getAPPlayer().sendMessage(Prefix.uhc + "§7Vous êtes maintenant §bspectateur §7de l'UHC");
                    }
                    break;
            }
        }
    }

    public Location getOfflineLocation() {
        return offlineLocation;
    }

    /*public int getTotalKills() {
        return apPlayer.getStat(Statistics.Type.KILL);
    }*/

    public Integer getOfflineFood() {
        return offlineFood;
    }

    public Integer getOfflineLevel() {
        return offlineLevel;
    }

    public Float getOfflineSaturation() {
        return offlineSaturation;
    }

    public ItemStack[] getOfflineInventory() {
        return offlineInventory;
    }

    public ItemStack[] getOfflineArmor() {
        return offlineArmor;
    }

    public List<PotionEffect> getOfflineEffect() {
        return offlineEffect;
    }

    public int getDiamond() {
        return diamond;
    }

    public int getGold() {
        return gold;
    }

    public int getKills() {
        return kills;
    }

    public GameTeam getGameTeam() {
        return gameTeam;
    }

    public void setGameTeam(GameTeam gameTeam) {
        this.gameTeam = gameTeam;
    }

    public HashSet<GameTeam> getInvitedInTeams(){
        return invitedInTeams;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public boolean isHelper() {
        try {
            return APICore.getPlugin().getChannelManager().getChannel("channel.helpop").isMember(this.getAPPlayer().getUUID());
        } catch(ChannelNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setHelper(boolean helper) {
        Channel helpop;
        try {
            helpop = APICore.getPlugin().getChannelManager().getChannel("channel.helpop");
        } catch(ChannelNotFoundException e) {
            e.printStackTrace();
            return;
        }
        if(helper){
            this.getAPPlayer().sendMessage(helpop.getDisplayName() + "§aActivé: Vous voyez les demandes des joueurs");
            helpop.addPlayer(this.getAPPlayer().getUUID());
            helpop.sendMessage("§6" + this.getAPPlayer().getName() + "§a voit les demandes d'aide", this.getAPPlayer().getUUID());
        } else {
            this.getAPPlayer().sendMessage(helpop.getDisplayName() + "§cDésactivé: Vous ne voyez plus les demandes des joueurs");
            helpop.removePlayer(this.getAPPlayer().getUUID());
            helpop.sendMessage("§6" + this.getAPPlayer().getName() + "§c ne voit plus les demandes d'aide");
        }
    }

    public boolean isOrespyer(){
        try {
            return APICore.getPlugin().getChannelManager().getChannel("channel.orespy").isMember(this.getAPPlayer().getUUID());
        } catch(ChannelNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public UUID getKiller() {
        return killer;
    }

    public void setKiller(UUID killer) {
        this.killer = killer;
    }

    public UUID getLastDamager() {
        return lastDamager;
    }

    public void setLastDamager(UUID lastDamager) {
        this.lastDamager = lastDamager;
    }

    public int getLastDamage() {
        return lastDamage;
    }

    public void setLastDamage(int lastDamage) {
        this.lastDamage = lastDamage;
    }

    public GameState getDecoGameState() {
        return decoGameState;
    }

    public void setDecoGameState(GameState decoGameState) {
        this.decoGameState = decoGameState;
    }

    public APScoreboard getScoreboard() {
        return scoreboard;
    }

    public void setScoreboard(APScoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    public EntityType getLastEntityAttack(){
        return this.lastEntityAttack;
    }

    public void setLastEntityAttack(EntityType entity){
        this.lastEntityAttack = entity;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setCanRecconect(boolean canRecconect) {
        this.canRecconect = canRecconect;
    }

    public void setOrespy(boolean orespyer){
        Channel orespy;
        try {
            orespy = APICore.getPlugin().getChannelManager().getChannel("channel.orespy");
        } catch(ChannelNotFoundException e){
            e.printStackTrace();
            return;
        }
        if(orespyer){
            this.getAPPlayer().sendMessage(orespy.getDisplayName() + "§aActivé: Vous voyez les minerais minés des joueurs");
            orespy.addPlayer(this.getAPPlayer().getUUID());
            orespy.sendMessage("§6" + this.getAPPlayer().getName() + "§a voit les minerais des joueurs", this.getAPPlayer().getUUID());
        } else {
            this.getAPPlayer().sendMessage(orespy.getDisplayName() + "§cDésactivé: Vous ne voyez plus les minerais minés des joueurs");
            orespy.removePlayer(this.getAPPlayer().getUUID());
            orespy.sendMessage("§6" + this.getAPPlayer().getName() + "§c ne voit plus les minerais des joueurs");
        }
    }

    public void setWhitelisted(boolean whitelist) {
        if(whitelist) {
            this.getAPPlayer().sendMessage(Prefix.uhc + "§7Vous êtes entré avec une Pré-Whitelist. Celle ci sera utilisé lorsque la Pré-Whitelist fermera. Vous pouvez quitter avant pour ne pas l'utiliser");
            Game.getGame().getPreWhitelistedPlayers().add(this.getAPPlayer().getUUID());
        } else {
            Game.getGame().getPreWhitelistedPlayers().remove(this.getAPPlayer().getUUID());
        }
    }

    public enum DeathCause {

        DARKNESS,
        OFFLINE

    }

    public enum UHCPlayerStatus {

        BEFORE,
        PLAYING,
        DEAD,
        SPECTATOR

    }

}