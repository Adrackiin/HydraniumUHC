package fr.adrackiin.hydranium.uhc.game;

import fr.adrackiin.api.AndosiaCore;
import fr.adrackiin.api.api.channel.AChannel;
import fr.adrackiin.api.api.event.PostInitEvent;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.EventsManager;
import fr.adrackiin.hydranium.uhc.events.GameStateChangeEvent;
import fr.adrackiin.hydranium.uhc.events.SecondEvent;
import fr.adrackiin.hydranium.uhc.game.border.BorderManager;
import fr.adrackiin.hydranium.uhc.game.core.UHCManager;
import fr.adrackiin.hydranium.uhc.game.core.players.PlayerManager;
import fr.adrackiin.hydranium.uhc.game.core.players.UHCPlayer;
import fr.adrackiin.hydranium.uhc.game.gameworld.GameNether;
import fr.adrackiin.hydranium.uhc.game.gameworld.GameWorld;
import fr.adrackiin.hydranium.uhc.game.gameworld.WorldGeneration;
import fr.adrackiin.hydranium.uhc.game.gameworld.worldsetting.CaveOres;
import fr.adrackiin.hydranium.uhc.game.gameworld.worldsetting.FlatLandLapis;
import fr.adrackiin.hydranium.uhc.game.platform.Platform;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.ScenariosManager;
import fr.adrackiin.hydranium.uhc.game.scoreboard.HealthManager;
import fr.adrackiin.hydranium.uhc.game.scoreboard.KillsScoreboard;
import fr.adrackiin.hydranium.uhc.game.scoreboard.ScoreboardManager;
import fr.adrackiin.hydranium.uhc.game.settings.Settings;
import fr.adrackiin.hydranium.uhc.game.state.GameState;
import fr.adrackiin.hydranium.uhc.game.state.GameStateManager;
import fr.adrackiin.hydranium.uhc.game.team.TeamManager;
import fr.adrackiin.hydranium.uhc.game.uhc.Craft;
import fr.adrackiin.hydranium.uhc.game.uhc.InvisiblePlayers;
import fr.adrackiin.hydranium.uhc.guis.APGBuffer;
import fr.adrackiin.hydranium.uhc.guis.APGHost;
import fr.adrackiin.hydranium.uhc.guis.APGPlayerSettings;
import fr.adrackiin.hydranium.uhc.guis.host.APGRules;
import fr.adrackiin.hydranium.uhc.guis.host.APGScenarios;
import fr.adrackiin.hydranium.uhc.guis.host.APGSettings;
import fr.adrackiin.hydranium.uhc.guis.host.APGWorlds;
import fr.adrackiin.hydranium.uhc.guis.host.scenarios.APGSoftBow;
import fr.adrackiin.hydranium.uhc.guis.host.scenarios.APGTimeBomb;
import fr.adrackiin.hydranium.uhc.guis.host.settings.*;
import fr.adrackiin.hydranium.uhc.guis.host.settings.config.*;
import fr.adrackiin.hydranium.uhc.guis.host.settings.death.APGHead;
import fr.adrackiin.hydranium.uhc.guis.host.settings.mob.APGHorse;
import fr.adrackiin.hydranium.uhc.guis.host.settings.nether.*;
import fr.adrackiin.hydranium.uhc.guis.host.settings.other.APGStrength;
import fr.adrackiin.hydranium.uhc.guis.host.worlds.APGOverworld;
import fr.adrackiin.hydranium.uhc.guis.host.worlds.APGWorldSetting;
import fr.adrackiin.hydranium.uhc.minigames.MiniGameManager;
import fr.adrackiin.hydranium.uhc.utils.MathUtils;
import fr.adrackiin.hydranium.uhc.utils.PubSub;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import fr.adrackiin.hydranium.uhc.utils.minecraft.Blocks;
import fr.adrackiin.hydranium.uhc.utils.minecraft.Items;
import fr.adrackiin.hydranium.uhc.utils.minecraft.Players;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Game implements Listener{

    private static Game game;
    private final Set<UUID> alivePlayers = new HashSet<>();
    private final Set<UUID> deadPlayers = new HashSet<>();
    private final Set<UUID> spectatorPlayers = new HashSet<>();
    private final Set<UUID> hostPlayers = new HashSet<>();
    private final Set<UUID> preWhitelistedPlayers = new HashSet<>();
    private final Set<UUID> whitelistedPlayers = new HashSet<>();
    private final Set<UUID> offlinePlayers = new HashSet<>();
    private final Set<Scenario> scenarios = new HashSet<>();
    private final Map<UUID, UHCPlayer> players = new HashMap<>();
    private final UHCManager uhcManager;
    private final MiniGameManager miniGameManager;
    private UUID host = null;
    private final GameWorld worldUHC;
    private final GameWorld worldNether;
    private final GameWorld worldEnder;
    private GameNether gameNether;
    private int pveDeath = 0;
    private boolean scUHC = true;
    private boolean chat = true;
    private int vScoreboard;
    private Timer timer;
    private final Settings settings;
    private final TeamManager teamManager;
    private final ScoreboardManager scoreboardManager;
    private final KillsScoreboard topKills;
    private final GameStateManager gameStateManager;
    private final BorderManager borderManager;
    private final ScenariosManager scenariosManager;
    private final PlayerManager playerManager;
    private final InvisiblePlayers invisiblePlayers;
    
    private final AChannel channelHost = new AChannel("host") {};

    private Platform platform;

    private GameWorld.Type gameWorldType;
    private WorldGeneration caveOre;
    private WorldGeneration lapis;

    private boolean stopSwitch = false;
    private boolean switchSc = false;
    
    private AChannel cOrespy;
    private AChannel cHelpop;
    private AChannel cDead;
    private AChannel cSpec;
    private AChannel cHost;

    public Game() {
        game = this;
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
        Bukkit.getServer().createWorld(new WorldCreator("uhc").type(WorldType.NORMAL).environment(World.Environment.NORMAL));
        Bukkit.getServer().createWorld(new WorldCreator("uhc_nether").environment(World.Environment.NETHER));
        Bukkit.getServer().createWorld(new WorldCreator("uhc_the_end").environment(World.Environment.THE_END));

        this.settings = new Settings();
        this.worldUHC = new GameWorld(Bukkit.getServer().getWorld("uhc"));
        this.worldNether = new GameWorld(Bukkit.getServer().getWorld("uhc_nether"));
        this.worldEnder = new GameWorld(Bukkit.getServer().getWorld("uhc_the_end"));

        new EventsManager();

        this.teamManager = new TeamManager();
        this.scoreboardManager = new ScoreboardManager();
        this.gameStateManager = new GameStateManager();
        this.borderManager = new BorderManager();
        this.gameNether = new GameNether();
        this.scenariosManager = new ScenariosManager();
        this.playerManager = new PlayerManager();

        new InventoryManager();
        this.uhcManager = new UHCManager();
        this.miniGameManager = new MiniGameManager();
        this.invisiblePlayers = new InvisiblePlayers();
        new HealthManager();
        new Craft();

        topKills = new KillsScoreboard();

        setGameWorldType(GameWorld.Type.NORMAL);
    }

    @EventHandler
    public void onPostInit(PostInitEvent e){
        caveOre = new CaveOres();
        lapis = new FlatLandLapis();

        APICore.getPlugin().getChannelManager().newChannel("channel.orespy", "§8[§dOres§8] ");
        APICore.getPlugin().getChannelManager().newChannel("channel.helpop", "§8[§6Helpop§8] ");
        APICore.getPlugin().getChannelManager().newChannel("channel.dead", "§8[§7Mort§8] ");
        APICore.getPlugin().getChannelManager().newChannel("channel.spec", "§8[§aSpec§8] ");
        APICore.getPlugin().getChannelManager().newChannel("channel.host", "§6§l[§c§lHost§6§l] ");
        
        APICore.getPlugin().getAPGuiManager().add(new APGPlayer());
        APICore.getPlugin().getAPGuiManager().add(new APGHost());
        APICore.getPlugin().getAPGuiManager().add(new APGPlayerSettings());
        APICore.getPlugin().getAPGuiManager().add(new APGBuffer());

        APICore.getPlugin().getAPGuiManager().add(new APGScenarios());
        APICore.getPlugin().getAPGuiManager().add(new APGSettings());
        APICore.getPlugin().getAPGuiManager().add(new APGRules());
        APICore.getPlugin().getAPGuiManager().add(new APGWorlds());

        APICore.getPlugin().getAPGuiManager().add(new APGTimeBomb());
        APICore.getPlugin().getAPGuiManager().add(new APGSoftBow());
        APICore.getPlugin().getAPGuiManager().add(new APGConfig());
        APICore.getPlugin().getAPGuiManager().add(new APGDeath());
        APICore.getPlugin().getAPGuiManager().add(new APGMob());
        APICore.getPlugin().getAPGuiManager().add(new APGNether());
        APICore.getPlugin().getAPGuiManager().add(new APGOther());
        APICore.getPlugin().getAPGuiManager().add(new APGOverworld());
        APICore.getPlugin().getAPGuiManager().add(new APGWorldSetting());

        AndosiaCore.getPlugin().getGuiManager().addGui(new APGTimeBomb());
        AndosiaCore.getPlugin().getGuiManager().addGui(new APGSoftBow());
        AndosiaCore.getPlugin().getGuiManager().addGui(new APGConfig());
        AndosiaCore.getPlugin().getGuiManager().addGui(new APGDeath());
        AndosiaCore.getPlugin().getGuiManager().addGui(new APGMob());
        AndosiaCore.getPlugin().getGuiManager().addGui(new APGNether());
        AndosiaCore.getPlugin().getGuiManager().addGui(new APGOther());
        AndosiaCore.getPlugin().getGuiManager().addGui(new APGOverworld());
        AndosiaCore.getPlugin().getGuiManager().addGui(new APGWorldSetting());

        AndosiaCore.getPlugin().getGuiManager().addGui(new APGPlayers());
        AndosiaCore.getPlugin().getGuiManager().addGui(new APGTime());
        AndosiaCore.getPlugin().getGuiManager().addGui(new APGBorder());
        AndosiaCore.getPlugin().getGuiManager().addGui(new APGApple());
        AndosiaCore.getPlugin().getGuiManager().addGui(new APGFlint());
        AndosiaCore.getPlugin().getGuiManager().addGui(new APGHead());
        AndosiaCore.getPlugin().getGuiManager().addGui(new APGHorse());
        AndosiaCore.getPlugin().getGuiManager().addGui(new APGPotionStrength());
        AndosiaCore.getPlugin().getGuiManager().addGui(new APGPotionSpeed());
        AndosiaCore.getPlugin().getGuiManager().addGui(new APGPotionDamage());
        AndosiaCore.getPlugin().getGuiManager().addGui(new APGPotionFireResistance());
        AndosiaCore.getPlugin().getGuiManager().addGui(new APGPotionHeal());
        AndosiaCore.getPlugin().getGuiManager().addGui(new APGPotionInvisibility());
        AndosiaCore.getPlugin().getGuiManager().addGui(new APGPotionPoison());
        AndosiaCore.getPlugin().getGuiManager().addGui(new APGPotionRegeneration());
        AndosiaCore.getPlugin().getGuiManager().addGui(new APGPotionSlowness());
        AndosiaCore.getPlugin().getGuiManager().addGui(new APGPotionWeakness());
        AndosiaCore.getPlugin().getGuiManager().addGui(new APGStrength());
        UHCCore.getPlugin().initCommands();
        worldUHC.newWorld();
        worldNether.newWorld();
        worldEnder.newWorld();
    }

    public boolean hasGameStart(){
        return getGameStateManager().getGameState().getId() >= GameState.START.getId();
    }

    public void addPlayingPlayer(UUID uuid){
        this.alivePlayers.add(uuid);
        this.updatePlayersOnline();
    }

    public void removePlayingPlayer(UUID uuid){
        this.alivePlayers.remove(uuid);
        this.updatePlayersOnline();
    }

    public boolean isOfflinePlayer(UUID uuid){
        return this.offlinePlayers.contains(uuid);
    }

    public void addOfflinePlayer(UUID uuid){
        this.offlinePlayers.add(uuid);
    }

    public void removeOfflinePlayer(UUID uuid){
        this.offlinePlayers.remove(uuid);
    }

    public void finalHeal(){
        for(UHCPlayer player : players.values()){
            if(player.getAPPlayer().isConnected()){
                if(player.isPlaying()){
                    player.getAPPlayer().setHealth(player.getAPPlayer().getMaxHealth());
                    player.getAPPlayer().sendMessage(Prefix.uhc + "§a" + "Vous avez été soigné");
                }
            }
        }
    }

    public void difficulty(){
        worldUHC.getWorld().setDifficulty(Difficulty.HARD);
        worldUHC.getWorld().setWaterAnimalSpawnLimit(5);
        worldUHC.getWorld().setMonsterSpawnLimit(20);
        worldUHC.getWorld().setAnimalSpawnLimit(50);
        worldUHC.getWorld().setAmbientSpawnLimit(5);
        worldNether.getWorld().setDifficulty(Difficulty.HARD);
        worldNether.getWorld().setWaterAnimalSpawnLimit(5);
        worldNether.getWorld().setMonsterSpawnLimit(20);
        worldNether.getWorld().setAnimalSpawnLimit(50);
        worldNether.getWorld().setAmbientSpawnLimit(5);
        worldEnder.getWorld().setDifficulty(Difficulty.HARD);
        worldEnder.getWorld().setWaterAnimalSpawnLimit(5);
        worldEnder.getWorld().setMonsterSpawnLimit(20);
        worldEnder.getWorld().setAnimalSpawnLimit(50);
        worldEnder.getWorld().setAmbientSpawnLimit(5);
    }

    public void pvp(){
        worldUHC.getWorld().setPVP(true);
        worldNether.getWorld().setPVP(true);
        worldEnder.getWorld().setPVP(true);
    }

    public void start(){
        for(UUID uuid : Game.getGame().getAlivePlayers().copy()){
            if(Bukkit.getPlayer(uuid) != null){
                Player p = Bukkit.getPlayer(uuid);
                p.removePotionEffect(PotionEffectType.BLINDNESS);
                p.removePotionEffect(PotionEffectType.SLOW);
                p.removePotionEffect(PotionEffectType.WEAKNESS);
                p.removePotionEffect(PotionEffectType.JUMP);
                p.setGameMode(GameMode.SURVIVAL);
                p.setFoodLevel(20);
                p.setSaturation(20.0F);
                p.getInventory().clear();
                p.setLevel(0);
                p.getInventory().setItem(0, new ItemStack(Material.COOKED_BEEF, 5));
            }
        }
    }

    public void victory(){
        if(getGameStateManager().isGameState(GameState.VICTORY)){
            return;
        }
        switchToKills();
        cancelSwitchTask();
        getBorderManager().reset();
        getTimer().stop();
        UHCCore.getPlugin().getServer().getPluginManager().callEvent(new GameStateChangeEvent(GameState.VICTORY));
        if(this.getAlivePlayers().getSize() == 0){
            Bukkit.broadcastMessage(Prefix.uhc + "§cAucun gagnant n'a été détecté :'(");
            return;
        }
        if(isTeam() && getUHCPlayer(this.getAlivePlayers().first()).getGameTeam().getAliveMembers().getSize() > 1){
            StringBuilder winnersSb = new StringBuilder();
            for(UUID uuid : getUHCPlayer(this.getAlivePlayers().first()).getGameTeam().getAliveMembers().copy()){
                winnersSb.append(Bukkit.getPlayer(uuid).getName()).append(", ");
                APICore.getPlugin().getAPPlayer(uuid).getStats().add("win");
            }
            String winners = winnersSb.substring(0, winnersSb.length() - 2);
            Bukkit.broadcastMessage(Prefix.uhc + "§a" + "Félicitations à " + "§6" + winners + "§a" + " pour leur victoire !");
        } else {
            APPlayer player = APICore.getPlugin().getAPPlayer(this.getAlivePlayers().first());
            player.getStats().add("win");
            Bukkit.broadcastMessage(Prefix.uhc + "§a" + "Félicitations à " + "§6" + player.getName() + "§a" + " pour sa victoire !");
            //player.sendMessage(Prefix.worldUHC + "§a" + "Vous gagnez 1 Pré-Whitelist");
        }
    }

    public void switchToKills(){
        this.getScoreboardManager().showKills();
        vScoreboard = 0;
        scUHC = false;
    }

    public void startSwitchScoreboard(){
        vScoreboard = 0;
        switchSc = true;
    }

    public void cancelSwitchTask(){
        stopSwitch = true;
    }

    public UHCPlayer getUHCPlayer(UUID uuid){
        if(players.get(uuid) != null){
            return players.get(uuid);
        } else {
            if(Bukkit.getPlayer(uuid) == null){
                return null;
            }
            players.put(uuid, new UHCPlayer(uuid));
            return players.get(uuid);
        }
    }

    public UHCPlayer getUHCPlayer(String name){
        return getUHCPlayer(Players.getOfflinePlayer(name).getUniqueId());
    }

    public boolean isPlayerExists(String name){
        return isPlayerExists(Players.getOfflinePlayer(name).getUniqueId());
    }

    public boolean isPlayerExists(UUID uuid){
        return players.contains(uuid);
    }

    public void addPveDeaths(int amount) {
        this.pveDeath += amount;
        Game.getGame().getScoreboardManager().addPveScoreboard();
    }

    public void updatePlayersOnline(){
        this.getScoreboardManager().updatePlayers();
        if(this.getGameStateManager().getGameState().getId() >= GameState.WAITING_WHITELIST.getId()){
            PubSub.playersUpdate();
        }
    }

    public void disableNether() {
        settings.setNether(false);
        for(UUID uuid : this.getAlivePlayers().copy()){
            UHCPlayer player = getUHCPlayer(uuid);
            if(player.getAPPlayer().getLocation().getWorld() == worldNether.getWorld()){
               int x = player.getAPPlayer().getLocation().getBlockX();
               int z = player.getAPPlayer().getLocation().getBlockZ();
                if(Math.abs(x) < 50) x = MathUtils.addOrMinus(x, 50);
                player.getAPPlayer().teleport(new Location(worldUHC.getWorld(), x + 0.5D, Blocks.getHighestBlock(worldUHC.getWorld(), x, z) + 1.5D, z + 0.5D));
                player.getAPPlayer().sendMessageBar(Prefix.uhc + "§7" + "Vous avez été téléporté, le Nether est maintenant" + "§c" + " fermé");
            }
        }
    }

    public void reset(){
        Bukkit.getServer().getPluginManager().callEvent(new GameStateChangeEvent(GameState.WAITING_HOST));
        this.setHost(null);
        this.getHostPlayers().clear();
        this.getAlivePlayers().clear();
        this.getWhitelistedPlayers().clear();
        this.getPreWhitelistedPlayers().clear();
        this.getDeadPlayers().clear();
        this.getSpectatorPlayers().clear();
        this.getOfflinePlayers().clear();
        this.getUHCPlayers().clear();
        this.updatePlayersOnline();
        PubSub.resetUHC();
    }

    public void dropGapple(Location location) {
        location.getWorld().dropItem(location, new ItemStack(Material.GOLDEN_APPLE));
    }

    public void dropGhead(Location location) {
        location.getWorld().dropItem(location, Items.getItem(Material.GOLDEN_APPLE, "§6Golden Head", 1, 0, Arrays.asList(
                "",
                "§b" + "Les Têtes d'or",
                "§b" + "régénèrent de 4 coeurs")));
    }

    public void checkVictory() {
        if(this.isTeam()){
            UHCCore.getPlugin().logServer(this.getTeamManager().getAliveTeams().toString());
            if(this.getTeamManager().getAliveTeams().getSize() == 1){
                this.victory();
            }
        } else {
            UHCCore.getPlugin().logServer(this.getAlivePlayers().toString());
            if(this.getAlivePlayers().getSize() == 1){
                this.victory();
            }
        }
    }

    public AChannel getchannelHost(){
        return channelHost;
    }

    public boolean isGameEmpty(){
        return this.getGameStateManager().isGameState(GameState.WAITING_HOST);
    }

    public boolean isGameWhitelist(){
        return this.getGameStateManager().isGameState(GameState.WHITELIST);
    }

    public boolean isGameWillEmpty(){
        return this.getGameStateManager().isGameState(GameState.CONFIG) && Bukkit.getOnlinePlayers().size() == 1;
    }

    public Set<UUID> getAlivePlayers(){
        return Collections.unmodifiableSet(alivePlayers);
    }

    public Set<UUID> getDeadPlayers() {
        return Collections.unmodifiableSet(deadPlayers);
    }

    public Set<UUID> getSpectatorPlayers() {
        return Collections.unmodifiableSet(spectatorPlayers);
    }

    public Set<UUID> getHostPlayers() {
        return Collections.unmodifiableSet(hostPlayers);
    }

    public Set<UUID> getOfflinePlayers() {
        return Collections.unmodifiableSet(offlinePlayers);
    }

    public UUID getHost() {
        return host;
    }

    public void setHost(UUID host) {
        this.host = host;
        Game.getGame().getScoreboardManager().setHost();
    }

    public HashSet<UUID> getPreWhitelistedPlayers() {
        return preWhitelistedPlayers;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    public GameWorld getWorldUHC() {
        return worldUHC;
    }

    public GameWorld getWorldNether() {
        return worldNether;
    }

    public GameWorld getWorldEnder() {
        return worldEnder;
    }

    public GameNether getGameNether() {
        return gameNether;
    }

    public void setGameNether(GameNether gameNether) {
        this.gameNether = gameNether;
    }

    public GameWorld.Type getGameWorldType() {
        return gameWorldType;
    }

    public void setGameWorldType(GameWorld.Type gameWorldType) {
        this.gameWorldType = gameWorldType;
    }

    public HashSet<Scenario> getScenarios() {
        return scenarios;
    }

    public Settings getSettings() {
        return settings;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public BorderManager getBorderManager() {
        return borderManager;
    }

    public HashSet<UUID> getWhitelistedPlayers() {
        return whitelistedPlayers;
    }

    public boolean isSwitchSc() {
        return switchSc;
    }

    public int getPveDeath() {
        return pveDeath;
    }

    public KillsScoreboard getTopKills() {
        return topKills;
    }

    public boolean isTeam(){
        return getSettings().getTeam() > 1;
    }

    public boolean isScUHC() {
        return scUHC;
    }

    public boolean isTchat() {
        return chat;
    }

    public void setTchat(boolean chat) {
        this.chat = chat;
    }

    public ScenariosManager getScenariosManager() {
        return scenariosManager;
    }

    public InvisiblePlayers getInvisiblePlayers() {
        return invisiblePlayers;
    }

    public UHCManager getUHCManager() {
        return uhcManager;
    }

    public WorldGeneration getCaveOre() {
        return caveOre;
    }

    public WorldGeneration getLapis() {
        return lapis;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public MiniGameManager getMiniGameManager(){
        return miniGameManager;
    }

    @EventHandler
    private void onSwitchScoreboard(SecondEvent e){
        if(!switchSc || stopSwitch){
            return;
        }
        if(scUHC) {
            if(vScoreboard == 20){
                this.getScoreboardManager().showKills();
                vScoreboard = 0;
                scUHC = false;
            }
        } else {
            if(vScoreboard == 10){
                this.getScoreboardManager().showUHC();
                vScoreboard = 0;
                scUHC = true;
            }
        }
        vScoreboard ++;
    }
    
    public static Game getGame() {
        return game;
    }
}
