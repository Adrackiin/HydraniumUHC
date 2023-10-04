package fr.adrackiin.hydranium.uhc.minigames.shifumi;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.utils.APHash;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.GameStateChangeEvent;
import fr.adrackiin.hydranium.uhc.events.WorldCheckFinishEvent;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class Shifumi implements Listener {

    private final APHash<UUID, SFMPlayer> sfmPlayer = new APHash<>();
    private Location player1;
    private Location player2;

    public Shifumi(){
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
        //APICore.getPlugin().getAPGuiManager().add(new APGShifumi());
        player1 = Game.getGame().getWorldUHC().getSpawn().clone().add(-1, 0, 0);
        player1.setYaw(90F);
        player2 = Game.getGame().getWorldUHC().getSpawn().clone().add(1, 0, 0);
        player2.setYaw(-90F);
    }

    @EventHandler
    public void onStart(EntityDamageByEntityEvent e){
        if(!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)){
        }
        //Commande
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        if(getSFMPlayers().contains(e.getPlayer().getUniqueId())){

        }
    }

    @EventHandler
    public void onStartUHC(GameStateChangeEvent e){
        if(e.getNewGameState() == GameState.START){
            HandlerList.unregisterAll(this);
            sfmPlayer.clear();
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onChangedWorld(WorldCheckFinishEvent e){
        player1 = Game.getGame().getWorldUHC().getSpawn().clone().add(-1, 0, 0);
        player1.setYaw(90F);
        player2 = Game.getGame().getWorldUHC().getSpawn().clone().add(1, 0, 0);
        player2.setYaw(-90F);
    }

    public SFMPlayer getSFMPlayer(UUID uuid){
        return sfmPlayer.get(uuid);
    }

    public void startDuel(UUID uuid1, UUID uuid2){
        APPlayer player1 = APICore.getPlugin().getAPPlayer(uuid1);
        APPlayer player2 = APICore.getPlugin().getAPPlayer(uuid2);
        Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), ()-> {
            player1.sendTitle("§cShi", "");
            player2.sendTitle("§cShi", "");
            Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), ()-> {
                player1.sendTitle("§cFu", "");
                player2.sendTitle("§cFu", "");
                Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), ()-> {
                    player1.sendTitle("§cMi", "");
                    player2.sendTitle("§cMi", "");
                }, 20L);
            }, 20L);
        }, 20L);
    }

    public APHash<UUID, SFMPlayer> getSFMPlayers(){
        return sfmPlayer;
    }

    public Location getPlayer1(){
        return player1;
    }

    public Location getPlayer2(){
        return player2;
    }

    public enum Object {

        DEFAULT,
        STONE,
        PAPER,
        CISSORS

    }

}
