package fr.adrackiin.hydranium.uhc.game.scenarios.managers;

import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.scenarios.*;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.WereWolves;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ScenariosManager {

    private final Map<Class<?>, Scenario> allScenarios = new HashMap<>();

    public ScenariosManager(){
        addScenario(new CutClean());
        addScenario(new TimeBomb());
        addScenario(new KeepLighting());
        addScenario(new WereWolves());
        addScenario(new FireLess());
        addScenario(new FlatLand());
        addScenario(new SuperHeroes());
        addScenario(new VeinMiner());
        addScenario(new NoBow());
        addScenario(new NoRod());
        addScenario(new Pyrophobia());
        addScenario(new HasteyBoys());
        addScenario(new Timber());
        addScenario(new CorruptedWorld());
        addScenario(new SoftBow());
        addScenario(new SkyHigh());
        addScenario(new Kreinzinator());
        addScenario(new KillEffect());
        addScenario(new NoEnchant());
        addScenario(new Permakill());
        addScenario(new LavaLess());
        addScenario(new BloodEnchant());
        addScenario(new GoldenRetriever());
        addScenario(new FlowerPower());
    }

    public Scenario getScenario(Class<?> sc){
        return allScenarios.get(sc);
    }

    public Scenario getScenarioClass(Class<?> sc){
        return allScenarios.get(sc);
    }

    public void forceFFA(){
        Game.getGame().getSettings().setTeam((byte) 1);
    }

    public Set<Class<?>> getScenarios() {
        return allScenarios.keySet();
    }

    private void addScenario(Object scenario){
        ((Scenario)scenario).setSource(scenario);
        allScenarios.put(scenario.getClass(), (Scenario)scenario);
    }
}
