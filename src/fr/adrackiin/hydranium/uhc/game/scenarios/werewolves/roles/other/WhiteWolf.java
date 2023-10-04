package fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.roles.other;

import fr.adrackiin.hydranium.uhc.events.DayEvent;
import fr.adrackiin.hydranium.uhc.events.NightEvent;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.WWUHCPlayer;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.WereWolves;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.events.DiscoverEvent;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.roles.Role;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.roles.Roles;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.UUID;

public class WhiteWolf extends Role {

    private final ArrayList<UUID> members = new ArrayList<>();

    @Override
    public void onDay(DayEvent e) {
        for(UUID uuid : members){
            dayEffect(WereWolves.getWereWolves().getWWPlayer(uuid));
        }
    }

    @Override
    public void onNight(NightEvent e) {
        for(UUID uuid : members){
            nightEffect(WereWolves.getWereWolves().getWWPlayer(uuid));
        }
    }

    @Override
    public void onDiscover(DiscoverEvent e) {
        for(UUID uuid : members){
            WWUHCPlayer wwPlayer = WereWolves.getWereWolves().getWWPlayer(uuid);
            presentation(wwPlayer);
            giveEffect(wwPlayer);
            setHealth(wwPlayer);
        }
    }

    public ArrayList<UUID> getMembers() {
        return members;
    }

    private void setHealth(WWUHCPlayer wwPlayer){
        wwPlayer.setMaxHealth(30);
    }

    private void dayEffect(WWUHCPlayer wwPlayer){
        wwPlayer.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
    }

    private void nightEffect(WWUHCPlayer wwPlayer){
        wwPlayer.addPotionEffect(PotionEffectType.INCREASE_DAMAGE, 1_000_000, 0, true);
    }

    private void presentation(WWUHCPlayer wwPlayer){
        wwPlayer.sendMessage(Prefix.wereWolves + "§7" + "Vous êtes le" + "§b" + Roles.WHITE_WOLF.getName());
        wwPlayer.sendMessage(Prefix.wereWolves + "§7" + "Votre but sera de gagner seul, mais les Loups-Garous vont verront comme un allié. Pour cela vous disposer des mêmes pouvoirs que les Loups-Garous, soit l'effet "
                + "§b" + "Force" + "§7" + " uniquement la nuit, ainsi que de l'effet " + "§b" + "Vision Nocturne");
    }

    private void giveEffect(WWUHCPlayer wwPlayer){
        wwPlayer.addPotionEffect(PotionEffectType.NIGHT_VISION, 1_000_000, 0, true);
    }
}
