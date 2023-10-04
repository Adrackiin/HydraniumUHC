package fr.adrackiin.hydranium.uhc;


import fr.adrackiin.hydranium.uhc.commands.*;
import fr.adrackiin.hydranium.uhc.events.PostInitWorldEvent;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.minigames.shifumi.ShifumiCmd;
import fr.adrackiin.hydranium.uhc.utils.PubSub;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import fr.adrackiin.hydranium.uhc.utils.pluginmessage.PluginMessageManager;
import net.minecraft.server.v1_8_R3.BiomeBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Random;

public class UHCCore extends JavaPlugin implements Listener {

    private static UHCCore plugin;
    private static final Random random = new Random();
    private RulesCmd rulesCmd;
    private boolean debug = false;

    @Override
    public void onEnable(){

        plugin = this;

        logServer("========== HydraniumUHC ==========");
        logServer("           Par Adrackiin");
        logServer("           Pour Hydranium");
        logServer("========== HydraniumUHC ==========");

        new Game();

        pluginMessage();
        this.getServer().getPluginManager().registerEvents(this, this);
        logServer("Enabled");
        Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), ()-> getServer().getPluginManager().callEvent(new PostInitWorldEvent()), 1L);
    }

    @Override
    public void onDisable(){

        for(Player player : Bukkit.getOnlinePlayers()){
            player.sendMessage(Prefix.uhc + "§c" + "Serveur UHC en cours d'extinction");
        }
        PubSub.resetUHC();

    }

    @Override
    public void onLoad(){
        try {
            Field f = BiomeBase.class.getDeclaredField("biomes");
            f.setAccessible(true);
            if((f.get(null) instanceof BiomeBase[])){
                BiomeBase[] biomes = (BiomeBase[])f.get(null);
                biomes[BiomeBase.DEEP_OCEAN.id] = BiomeBase.PLAINS;
                biomes[BiomeBase.FROZEN_OCEAN.id] = BiomeBase.FOREST;
                biomes[BiomeBase.OCEAN.id] = BiomeBase.FOREST;
            }
        } catch(IllegalAccessException | NoSuchFieldException e){
            e.printStackTrace();
        }
    }

    public void initCommands() {
        new SethostCmd();
        new ClearCmd();
        new DebugCmd();
        new GamemodeCmd();
        new HelperCmd();
        new HelpopCmd();
        new HostCmd();
        new InvseeCmd();
        new KillofflineCmd();
        new MenuCmd();
        new OrespyCmd();
        new PlayerCmd();
        new ReviveCmd();
        rulesCmd = new RulesCmd();
        new SendhostCmd();
        new SpecCmd();
        new StalkCmd();
        new TeamCmd();
        new TpCmd();
        new UHCCmd();
        new WelcomeCmd();
        new CoordsCmd();
        new SetdebugCmd();
        new ShifumiCmd();
    }

    public void logServer(String message){
        System.out.println("[HydraniumUHC] " + message);
    }

    public RulesCmd getRulesCmd() {
        return rulesCmd;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    private void pluginMessage(){
        PluginMessageManager.registerChannels(this);
    }

    public static UHCCore getPlugin() {
        return plugin;
    }

    public static Random getRandom() {
        return random;
    }
}
