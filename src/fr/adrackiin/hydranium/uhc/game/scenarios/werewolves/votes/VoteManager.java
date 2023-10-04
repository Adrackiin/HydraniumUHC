package fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.votes;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.WWUHCPlayer;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.WereWolves;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.events.ResultVoteEvent;
import fr.adrackiin.hydranium.uhc.utils.MathUtils;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VoteManager implements Listener {

    private boolean allowVote = false;
    private WWUHCPlayer lastVoted = null;

    public VoteManager(){
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    public void startVote() {
        if(lastVoted != null){
            if(lastVoted.getPlayer().getMaxHealth() == 10) {
                lastVoted.setMaxHealth(20);
            }
        }
        Bukkit.broadcastMessage(Prefix.wereWolves + "§7" + "Vous pouvez voter pour un joueur avec /lg vote <pseudo>. Celui qui aura reçu le plus de vote verra sa vie dimininué de moitié");
        allowVote = true;
        Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), this::stopVote, 60*20L);
    }

    public void commandVote(WWUHCPlayer sender, String[] args) {
        if(!allowVote){
            sender.sendMessage(Prefix.wereWolves + "§c" + "Ce n'est pas le moment de voter !");
            return;
        }
        if(args.length < 2){
            sender.sendMessage(Prefix.wereWolves + "§c" + "Merci de spécifier un joueur !");
            return;
        }
        if(sender.isHasVoted()){
            sender.sendMessage(Prefix.wereWolves + "§c" + "Vous ne pouvez voter qu'une seule fois !");
            return;
        }
        if(!addVote(args[1])){
            sender.sendMessage(Prefix.wereWolves + "§c" + "Ce joueur n'existe pas !");
        } else {
            sender.sendMessage(Prefix.wereWolves + "§a" + "Votre vote a bien été pris en compte !");
            sender.setHasVoted(true);
        }
    }

    public void resetVotes(){
        allowVote = false;
        for(WWUHCPlayer wwPlayer : WereWolves.getWereWolves().getWwuhcPlayers().values()){
            wwPlayer.resetVote();
        }
    }

    public WWUHCPlayer getLastVoted() {
        return lastVoted;
    }

    public void setLastVoted(WWUHCPlayer lastVoted) {
        this.lastVoted = lastVoted;
    }

    private boolean addVote(String name){
        WWUHCPlayer cible = WereWolves.getWereWolves().getWWPlayer(name);
        if(cible.getUHCPlayer().getAPPlayer().isConnected() && !cible.getUHCPlayer().isDead()){
            cible.addVote(1);
            return true;
        } else {
            return false;
        }
    }

    private void stopVote(){
        Map<WWUHCPlayer, Integer> votes = new HashMap<>();
        for(WWUHCPlayer wwPlayer : WereWolves.getWereWolves().getWwuhcPlayers().values()){
            votes.put(wwPlayer, wwPlayer.getVote());
        }
        int max = MathUtils.getMaxInt(votes.values());
        if(max == 0){
            noVotes();
            votes.clear();
            return;
        }
        ArrayList<WWUHCPlayer> choosen = new ArrayList<>();
        for(WWUHCPlayer wwPlayer : votes.keySet()){
            if(votes.get(wwPlayer) == max){
                choosen.add(wwPlayer);
            }
        }
        WWUHCPlayer voted;
        if(choosen.size() == 1){
            voted = choosen.get(0);
        } else {
            voted = choosen.get(UHCCore.getRandom().nextInt(choosen.size()));
        }
        UHCCore.getPlugin().getServer().getPluginManager().callEvent(new ResultVoteEvent(voted));
        choosen.clear();
        votes.clear();
        resetVotes();
    }

    private void noVotes(){
        Bukkit.broadcastMessage(Prefix.wereWolves + "§7" + "Le Village n'a pas décidé de voter");
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void resultVote(ResultVoteEvent e){
        if(e.isCancelled()){
            return;
        }
        Bukkit.broadcastMessage(Prefix.wereWolves + "§c" + "Le village a décidé de voter contre " +
                "§6" + e.getVoted().getName() + "§7" + ". Il perdra la moitié de sa vie jusqu'au prochain vote");
        e.getVoted().setMaxHealth(10);
        lastVoted = e.getVoted();
    }
}
