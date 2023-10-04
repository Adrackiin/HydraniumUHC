package fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.roles.villagers;

import fr.adrackiin.hydranium.uhc.events.DayEvent;
import fr.adrackiin.hydranium.uhc.events.NightEvent;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.events.DiscoverEvent;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.roles.Role;

import java.util.List;
import java.util.UUID;

public class Fox extends Role {
    @Override
    public void onDay(DayEvent e) {

    }

    @Override
    public void onNight(NightEvent e) {

    }

    @Override
    public void onDiscover(DiscoverEvent e) {

    }

    @Override
    public List<UUID> getMembers() {
        return null;
    }
}
