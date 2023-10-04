package fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.roles.villagers;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.DayEvent;
import fr.adrackiin.hydranium.uhc.events.NightEvent;
import fr.adrackiin.hydranium.uhc.events.UHCPlayerDeathEvent;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.WWUHCPlayer;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.WereWolves;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.events.DiscoverEvent;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.roles.Role;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.roles.Roles;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.*;

public class Chaman extends Role implements Listener {

    private final Map<WWUHCPlayer, Boolean> allowPowers = new HashMap<>();
    private final ArrayList<UUID> members = new ArrayList<>();

    public Chaman(){
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    @Override
    public void onDay(DayEvent e) {}

    @Override
    public void onNight(NightEvent e) {
        for(UUID uuid : members){
            WWUHCPlayer wwPlayer = WereWolves.getWereWolves().getWWPlayer(uuid);
            setAllowPower(wwPlayer,true);
            wwPlayer.sendMessage(Prefix.wereWolves + "§7" + "Vous avez une minute pour connaïtre l'assassin d'un mort avec la commande /lg murder <joueur>");
            Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), ()-> {
                setAllowPower(wwPlayer,false);
            }, 60 * 20L);
        }
    }

    @Override
    public void onDiscover(DiscoverEvent e) {
        for(UUID uuid : members){
            WWUHCPlayer wwPlayer = WereWolves.getWereWolves().getWWPlayer(uuid);
            presentation(wwPlayer);
            allowPowers.put(wwPlayer, false);
        }
    }

    public List<UUID> getMembers() {
        return members;
    }

    public void seeMurder(WWUHCPlayer sender, String[] args){
        if(!members.contains(sender.getUuid())){
            sender.sendMessage(Prefix.wereWolves + "§c" + "Vous ne pouvez pas utiliser cette commande");
            return;
        }
        if(!isAllowPower(sender)){
            sender.sendMessage(Prefix.wereWolves + "§c" + "Vous ne pouvez pas utiliser votre pouvoir actuellement");
            return;
        }
        WWUHCPlayer cible = WereWolves.getWereWolves().getWWPlayer(args[1]);
        if(cible == null){
            sender.sendMessage(Prefix.wereWolves + "§c" + "Ce joueur n'existe pas");
            return;
        }
        Bukkit.broadcastMessage(cible.getUHCPlayer().isDead() + " " + cible.getName() + " " + Game.getGame().getUHCPlayer(cible.getUuid()).isDead());
        if(!cible.getUHCPlayer().isDead()){
            sender.sendMessage(Prefix.wereWolves + "§c" + "Ce joueur n'est pas mort");
            return;
        }
        if(cible == sender){
            sender.sendMessage(Prefix.wereWolves + "§c" + "Je ne pense pas que cela soit utile :s");
            return;
        }
        UUID murder = cible.getKiller();
        if(murder == null){
            sender.sendMessage(Prefix.wereWolves + "§b" + cible.getName() + "§7" + " est mort du " + "§b" + "PvE");
        } else {
            sender.sendMessage(Prefix.wereWolves + "§b" + cible.getName() + "§7" + " est mort de la main de " + "§b" + Bukkit.getPlayer(murder).getName());
        }
        setAllowPower(sender, false);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(UHCPlayerDeathEvent e){
        if(e.isCancellable()){
            return;
        }
        if(e.getKiller() != null) {
            WereWolves.getWereWolves().getWWPlayer(e.getDeath().getAPPlayer().getUUID()).setKiller(e.getKiller().getAPPlayer().getUUID());
        }
    }

    private void presentation(WWUHCPlayer wwPlayer) {
        wwPlayer.sendMessage(Prefix.wereWolves + "§7" + "Vous êtes le " + "§b" + Roles.CHAMAN.getName());
        wwPlayer.sendMessage(Prefix.wereWolves + "§7" + "Votre but sera de gagner avec le Village. Au début de chaque nuit, vous pourrez connaitre l'assassin d'un joueur mort avec la commande " + "§b" + "/lg murder <joueur>");
    }

    private boolean isAllowPower(WWUHCPlayer wwPlayer){
        return allowPowers.get(wwPlayer);
    }

    private void setAllowPower(WWUHCPlayer wwPlayer, boolean allow){
        allowPowers.replace(wwPlayer, allow);
    }

}
