package fr.adrackiin.hydranium.uhc.game.scenarios;

import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;
import org.bukkit.event.Listener;

public class GoneFishin extends Scenario implements Listener {

    private final boolean active = false;

    public GoneFishin(String name, String description, String[] descriptionHost) {
        super(name, description, descriptionHost);
    }

    @Override
    public void setScenario(boolean status) {
        if(status){
            enable();
        } else {
            disable();
        }
        this.setActive(status);
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String[] getDescriptionHost() {
        return null;
    }


}
