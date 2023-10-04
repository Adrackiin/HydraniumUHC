package fr.adrackiin.hydranium.uhc.game.scenarios;

import fr.adrackiin.hydranium.api.utils.APText;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.GameTeamJoinedEvent;
import fr.adrackiin.hydranium.uhc.events.GameTeamLeftEvent;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;
import fr.adrackiin.hydranium.uhc.game.team.GameTeam;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class BackPack extends Scenario implements Listener, CommandExecutor {

    private final HashMap<GameTeam, Inventory> backpacks = new HashMap<>();

    public BackPack(){
        super(
                "BackPack",
                "Les équipes possèdent un coffre en commun accessible avec /bp",
                new String[]{"", "Les équipes possèdent", "un coffre en commun"}
        );

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
        UHCCore.getPlugin().getCommand("backpack").setExecutor(this);
        //APICore.getPlugin().getCommandManager().addCommandPermission("hydranium.uhc.command.backpack", "backpack", "bp");
        APText t = new APText("   §6» §e/backpack §f– §7Ouvrir l'inventaire de l'équipe");
        t.showText(" §6» §eOuvrir §f– §b/backpack");
        t.suggestCommand("/backpack");
        //APICore.getPlugin().getCommandManager().addCommandHelp("backpack", t);
    }

    @Override
    public void disable(){
        HandlerList.unregisterAll(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args){
        return true;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onTeamJoined(GameTeamJoinedEvent e){
        this.getBackpacks().put(e.getTeam(), Bukkit.createInventory(null, 3 * 9, e.getTeam().getPrefix() + "Sac à dos"));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onTeamLeaved(GameTeamLeftEvent e){
        this.getBackpacks().remove(e.getTeam());
    }

    private HashMap<GameTeam, Inventory> getBackpacks(){
        return backpacks;
    }

}
