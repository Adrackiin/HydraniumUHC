package fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.roles.other;

import fr.adrackiin.hydranium.uhc.events.DayEvent;
import fr.adrackiin.hydranium.uhc.events.NightEvent;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.WWUHCPlayer;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.WereWolves;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.events.DiscoverEvent;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.roles.Role;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.roles.Roles;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import fr.adrackiin.hydranium.uhc.utils.minecraft.Items;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class Assassin extends Role {

    private final ArrayList<UUID> members = new ArrayList<>();

    @Override
    public void onDay(DayEvent e){
        for(UUID uuid : members){
            dayEffect(WereWolves.getWereWolves().getWWPlayer(uuid));
        }
    }

    @Override
    public void onNight(NightEvent e){
        for(UUID uuid : members){
            nightEffect(WereWolves.getWereWolves().getWWPlayer(uuid));
        }
    }

    @Override
    public void onDiscover(DiscoverEvent e){
        for(UUID uuid : members){
            WWUHCPlayer wwPlayer = WereWolves.getWereWolves().getWWPlayer(uuid);
            presentation(wwPlayer);
            giveStuff(wwPlayer);
        }
    }

    public ArrayList<UUID> getMembers() {
        return members;
    }

    private void dayEffect(WWUHCPlayer wwPlayer){
        wwPlayer.addPotionEffect(PotionEffectType.INCREASE_DAMAGE, 1_000_000, 0, true);
    }

    private void nightEffect(WWUHCPlayer wwPlayer){
        wwPlayer.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
    }

    private void presentation(WWUHCPlayer wwPlayer){
        wwPlayer.sendMessage(Prefix.wereWolves + "§7" + "Vous êtes l'" + "§b" + Roles.ASSASSIN.getName());
        wwPlayer.sendMessage(Prefix.wereWolves + "§7" + "Votre but sera de gagner seul. Pour cela vous disposer de l'effet "
                + "§b" + "Force" + "§7" + " uniquement le jour, ainsi que trois " + "§b" + "livres enchantés Protection III, Tranchant III et Puissance III");
    }

    private void giveStuff(WWUHCPlayer wwPlayer){
        wwPlayer.give(Arrays.asList(
                Items.enchantedBook(Enchantment.PROTECTION_ENVIRONMENTAL, 3),
                Items.enchantedBook(Enchantment.DAMAGE_ALL, 3),
                Items.enchantedBook(Enchantment.ARROW_DAMAGE, 3)));
    }
}
