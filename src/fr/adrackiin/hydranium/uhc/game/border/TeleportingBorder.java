package fr.adrackiin.hydranium.uhc.game.border;

import fr.adrackiin.hydranium.api.utils.APList;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.GameStateChangeEvent;
import fr.adrackiin.hydranium.uhc.events.SecondEvent;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.scoreboard.ScoreboardManager;
import fr.adrackiin.hydranium.uhc.game.settings.Settings;
import fr.adrackiin.hydranium.uhc.game.state.GameState;
import fr.adrackiin.hydranium.uhc.utils.MathUtils;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.UUID;

public class TeleportingBorder implements Listener {

    private static int id;
    private final Settings settings = Game.getGame().getSettings();
    private final APList<Integer> reductionsStep = new APList<>();
    private int reduction;
    private double speed;
    private int timer;
    private int size;

    public void initBorder() {
        size = settings.getSize();
        int sizeWithFinal = size - settings.getFinalSize();
        while(sizeWithFinal > 0){
            sizeWithFinal = MathUtils.roundMin((sizeWithFinal / 4 * 3), (byte)3);
            if(sizeWithFinal != 0){
                reductionsStep.add(sizeWithFinal);
            }
        }
        reductionsStep.add((int)settings.getFinalSize());
        reductionsStep.seeList();
        speed = (double) (size - settings.getFinalSize()) / (settings.getMeetup() * 60);
    }

    public void startBorder(){
        ScoreboardManager scoreboardManager = Game.getGame().getScoreboardManager();
        reduction = reductionsStep.first();
        reductionsStep.remove();
        timer = (int) ((settings.getSize() - reduction) / speed);
        scoreboardManager.setReducSize(reduction);
        scoreboardManager.setReducTime(timer);
        scoreboardManager.setActualSize(size);
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    public void stop(){
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onTimer(SecondEvent e){
        borderTp();
    }

    private void borderTp(){
        if(size == settings.getFinalSize()){
            stop();
            return;
        }
        if((timer <= 5 && timer != 0) ||
                timer == 30 ||
                timer == 15 ||
                timer == 10){
            Bukkit.broadcastMessage(Prefix.uhc + "§7" + "Réduction de la Bordure en " + "§c" + "-" + reduction + "/" + reduction + "§7" + " dans " + "§c" + timer + "§7" + " secondes");
        }
        if(timer == 0){
            Bukkit.broadcastMessage(Prefix.uhc + "§7" + "Réduction de la Bordure en " + "§c" + "-" + reduction + "/" + reduction);
            teleportPlayerShrinking();
            if(reductionsStep.isEmpty()){
                stop();
                Game.getGame().getScoreboardManager().setActualSize(Game.getGame().getSettings().getFinalSize());
                Bukkit.getServer().getPluginManager().callEvent(new GameStateChangeEvent(GameState.MEETUP));
                Game.getGame().getWorldUHC().getWorld().getWorldBorder().setSize(settings.getFinalSize() * 2);
                return;
            } else {
                size = reduction;
                reduction = reductionsStep.first();
                timer = (int) ((size - reduction) / speed);
                if(reductionsStep.getSize() == 1) {
                    timer = Game.getGame().getTimer().getMeetup() - Game.getGame().getTimer().getTimer();
                }
                Game.getGame().getScoreboardManager().setReducSize(reduction);
                Game.getGame().getScoreboardManager().setActualSize(size);
            }
            Game.getGame().getWorldUHC().getWorld().getWorldBorder().setSize(size * 2);
            reductionsStep.remove();
        }
        Game.getGame().getScoreboardManager().setReducTime(timer);
        timer --;
    }

    private void teleportPlayerShrinking(){
        Player player;
        Location location;
        Location newLocation;
        for(UUID uuid : Game.getGame().getAlivePlayers().copy()){
            player = Bukkit.getPlayer(uuid);
            if(player != null){
                location = player.getLocation();
                if(location.getX() >= reduction ||
                        location.getZ() >= reduction ||
                        location.getX() <= -reduction ||
                        location.getZ() <= -reduction){
                    newLocation = location.clone();
                    if((location.getX() >= reduction) ||
                            (location.getX() <= -reduction)) {
                        if (location.getX() > 0) {
                            newLocation.setX(((reduction - 10) + UHCCore.getRandom().nextInt(10)) - 0.5D);
                        } else {
                            newLocation.setX(((-reduction + 10) - UHCCore.getRandom().nextInt(10)) + 0.5D);
                        }
                    }
                    if((location.getZ() >= reduction) ||
                            (location.getZ() <= -reduction)){
                        if (location.getZ() > 0) {
                            newLocation.setZ(((reduction - 10) + UHCCore.getRandom().nextInt(10)) - 0.5D);
                        } else {
                            newLocation.setZ(((-reduction + 10) - UHCCore.getRandom().nextInt(10)) + 0.5D);
                        }
                    }
                    newLocation.setY(newLocation.getWorld().getHighestBlockYAt(newLocation.getBlockX(), newLocation.getBlockZ()) + 0.5D);
                    if(newLocation.getY() > location.getY() && location.getY() < 56){
                        newLocation.setY(location.getY() + 0.5D);
                    }
                    newLocation.getWorld().getChunkAt(newLocation).load(false);
                    if(newLocation.getBlock().getType() != Material.AIR ||
                            newLocation.getBlock().getRelative(BlockFace.UP).getType() != Material.AIR &&
                                    newLocation.getBlock().getRelative(BlockFace.UP, 2).getType() != Material.AIR){
                        for(int i = 0; i < 3; i ++){
                            newLocation.getBlock().getRelative(BlockFace.UP, i).setType(Material.AIR);
                        }
                    }
                    player.teleport(newLocation);
                }
            }
        }
    }

}
