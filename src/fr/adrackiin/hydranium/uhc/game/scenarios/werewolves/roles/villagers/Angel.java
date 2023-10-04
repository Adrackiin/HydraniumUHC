package fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.roles.villagers;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.DayEvent;
import fr.adrackiin.hydranium.uhc.events.NightEvent;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.WWUHCPlayer;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.WereWolves;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.events.DiscoverEvent;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.events.ResultVoteEvent;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.roles.Role;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.roles.Roles;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.UUID;

public class Angel extends Role implements Listener{

    private final ArrayList<UUID> members = new ArrayList<>();

    public Angel(){
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    @Override
    public void onDay(DayEvent e) {}

    @Override
    public void onNight(NightEvent e) {}

    @Override
    public void onDiscover(DiscoverEvent e) {
        for(UUID uuid : members) {
            WWUHCPlayer wwPlayer = WereWolves.getWereWolves().getWWPlayer(uuid);
            presentation(wwPlayer);
        }
    }

    public ArrayList<UUID> getMembers() {
        return members;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onResultVote(ResultVoteEvent e){
        if(e.getVoted().getRole() == Roles.ANGEL){
            e.setCancelled(true);
            Bukkit.broadcastMessage(Prefix.wereWolves + "§c" + "Le village a décidé de voter contre " +
                    "§6" + e.getVoted().getName() + "§7" + ". Il perdra la moitié de sa vie jusqu'au prochain vote");
            e.getVoted().sendMessage(Prefix.wereWolves + "§7" + "Le Village a décidé de voter contre vous ! Au lieu de perdre 5 coeurs, vous gagnez un coeur permanent !");
            e.getVoted().setMaxHealth(e.getVoted().getPlayer().getMaxHealth() + 2);
            WereWolves.getWereWolves().getVoteManager().setLastVoted(e.getVoted());
        }
    }

    private void presentation(WWUHCPlayer wwPlayer) {
        wwPlayer.sendMessage(Prefix.wereWolves + "§7" + "Vous êtes l'" + "§b" + Roles.ANGEL.getName());
        wwPlayer.sendMessage(Prefix.wereWolves + "§7" + "Votre but sera de gagner avec le Village. Si vous êtes voté par le village, vous ne perdrez pas de vie et" + "§b" + " gagnerez un coeur permanent");
    }
}
