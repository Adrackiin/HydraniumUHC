package fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.roles.other;

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
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WildChildren extends Role implements Listener {

    private final Map<WWUHCPlayer, Boolean> isVillager = new HashMap<>();
    private final Map<WWUHCPlayer, WWUHCPlayer> modeles = new HashMap<>();
    private final ArrayList<UUID> members = new ArrayList<>();

    public WildChildren(){
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    @Override
    public void onDay(DayEvent e) {
        for(UUID uuid : members) {
            WWUHCPlayer wwPlayer = WereWolves.getWereWolves().getWWPlayer(uuid);
            if(WereWolves.getWereWolves().getRoleManager().getWolves().contains(uuid)){
                dayEffect(wwPlayer);
            }
        }
    }

    @Override
    public void onNight(NightEvent e) {
        for(UUID uuid : members) {
            WWUHCPlayer wwPlayer = WereWolves.getWereWolves().getWWPlayer(uuid);
            if(WereWolves.getWereWolves().getRoleManager().getWolves().contains(uuid)){
                nightEffect(wwPlayer);
            }
        }
    }

    @Override
    public void onDiscover(DiscoverEvent e) {
        for(UUID uuid : members){
            WWUHCPlayer wwPlayer = WereWolves.getWereWolves().getWWPlayer(uuid);
            modeles.put(wwPlayer, null);
            isVillager.put(wwPlayer, false);
            presentation(wwPlayer);
            Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), ()-> {
                if(getModele(wwPlayer) == null){
                    wwPlayer.sendMessage(Prefix.wereWolves + "§c" + "Vous n'avez plus qu'une minute pour choisir un modèle !");
                }
            }, 4 * 60 * 20L);
            Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), ()-> {
                if(getModele(wwPlayer) == null){
                    wwPlayer.sendMessage(Prefix.wereWolves + "§c" + "Vous n'avez pas choisi de modèle ! Vous n'êtes maintenant qu'un Simple Villageois");
                    setVilliager(wwPlayer, true);
                }
            }, 5 * 60 * 20L);
        }
    }

    public ArrayList<UUID> getMembers() {
        return members;
    }

    public void choose(WWUHCPlayer sender, String[] args){
        if(!members.contains(sender.getUuid())){
            sender.sendMessage(Prefix.wereWolves + "§c" + "Vous ne pouvez pas utiliser cette commande");
            return;
        }
        if(isVillager(sender)){
            sender.sendMessage(Prefix.wereWolves + "§c" + "Vous n'avez pas choisi de modèle à temps ! Vous êtes un simple villageois");
            return;
        }
        if(getModele(sender) != null){
            sender.sendMessage(Prefix.wereWolves + "§c" + "Vous avez déjà un modèle");
            return;
        }
        if(args.length == 1){
            sender.sendMessage(Prefix.wereWolves + "§c" + "Veuillez choisir un joueur");
            return;
        }
        WWUHCPlayer cible = WereWolves.getWereWolves().getWWPlayer(args[1]);
        if(cible == null){
            sender.sendMessage(Prefix.wereWolves + "§c" + "Ce joueur n'existe pas");
            return;
        }
        if(!Game.getGame().getAlivePlayers().contains(cible.getUuid())){
            sender.sendMessage(Prefix.wereWolves + "§c" + "Vous ne pouvez pas choisir ce joueur");
            return;
        }
        if(cible == sender){
            sender.sendMessage(Prefix.wereWolves + "§c" + "Sérieusement ? Non.");
            return;
        }
        setModele(sender, cible);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(UHCPlayerDeathEvent e){
        if(e.isCancellable()){
            return;
        }
        for(UUID uuid : members){
            WWUHCPlayer wwPlayer = WereWolves.getWereWolves().getWWPlayer(uuid);
            if(getModele(wwPlayer) != null) {
                if (getModele(wwPlayer).getUuid() == e.getDeath().getAPPlayer().getUUID()) {
                    wolf(wwPlayer);
                }
            }
        }
    }

    private void dayEffect(WWUHCPlayer wwPlayer){
        wwPlayer.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
    }

    private void nightEffect(WWUHCPlayer wwPlayer) {
        wwPlayer.addPotionEffect(PotionEffectType.INCREASE_DAMAGE, 1_000_000, 0, true);
    }

    private void presentation(WWUHCPlayer wwPlayer) {
        wwPlayer.sendMessage(Prefix.wereWolves + "§7" + "Vous êtes l'" + "§b" + Roles.WILD_CHILDREN.getName());
        wwPlayer.sendMessage(Prefix.wereWolves + "§7" + "Votre but sera de gagner soit avec le Village, soit avec les Loups-Garous. Vous avez 5 minutes pour choisir un modèle avec /lg modele <joueur>. Si celui-ci meurt, vous deviendrez un Loup-Garou et obtiendrez leurs pouvoirs: " +
                "§b" + "Force" + "§7" + " la nuit et " + "§b" + "Vision Nocturne" + "§7" + ". Si vous n'en choisissez pas, vous deviendrez Simple Villageois");
    }

    private void wolf(WWUHCPlayer wwPlayer){
        wwPlayer.sendMessage(Prefix.wereWolves + "§7" + "Votre modèle vient de mourir, vous êtes maintenant un " + "§c" + "Loup-Garou !");
        WereWolves.getWereWolves().getRoleManager().getWolves().add(wwPlayer.getUuid());
    }

    private WWUHCPlayer getModele(WWUHCPlayer wwPlayer){
        return modeles.get(wwPlayer);
    }

    private boolean isVillager(WWUHCPlayer wwPlayer){
        return isVillager.get(wwPlayer);
    }

    private void setVilliager(WWUHCPlayer wwPlayer, boolean villager){
        isVillager.replace(wwPlayer, villager);
    }

    private void setModele(WWUHCPlayer wwPlayer, WWUHCPlayer modele){
        modeles.replace(wwPlayer, modele);
    }
}
