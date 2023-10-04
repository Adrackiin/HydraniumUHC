package fr.adrackiin.hydranium.uhc.game.scenarios;

import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;

public class FlatLand extends Scenario {

    public FlatLand() {
        super(
                "FlatLand",
                "Le monde est plat, les ressources s'obtiennent dans les forges",
                new String[]{"", "§7Le monde est plat", "§7les ressources s'obtiennent", "dans les forges"});
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



}
