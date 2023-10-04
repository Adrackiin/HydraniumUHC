package fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.roles;

import fr.adrackiin.hydranium.uhc.events.DayEvent;
import fr.adrackiin.hydranium.uhc.events.NightEvent;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.events.DiscoverEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Role {

    private final List<UUID> members = new ArrayList<>();
    public abstract void onDay(DayEvent e);
    public abstract void onNight(NightEvent e);
    public abstract void onDiscover(DiscoverEvent e);
    public abstract List<UUID> getMembers();

}
