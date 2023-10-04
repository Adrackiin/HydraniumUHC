package fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.roles;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.DayEvent;
import fr.adrackiin.hydranium.uhc.events.NightEvent;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.WereWolves;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.events.DiscoverEvent;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.roles.other.Assassin;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.roles.other.WhiteWolf;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.roles.other.WildChildren;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.roles.villagers.*;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.roles.wolfs.FatherWolf;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.roles.wolfs.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.UUID;

public class RoleManager implements Listener {

    private final ArrayList<UUID> wolves = new ArrayList<>();
    private final ArrayList<UUID> villagers = new ArrayList<>();

    private final Assassin assassin;
    private final WhiteWolf whiteWolf;
    private final WildChildren wildChildren;
    private final Angel angel;
    private final Chaman chaman;
    private final Cupid cupid;
    private final Fox fox;
    private final LittleGirl littleGirl;
    private final Old old;
    private final Saving saving;
    private final Shower shower;
    private final Showy showy;
    private final Sisters sisters;
    private final Villager villager;
    private final Witch witch;
    private final FatherWolf fatherWolf;
    private final Wolf wolf;

    public RoleManager(){
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
        assassin = new Assassin();
        whiteWolf = new WhiteWolf();
        wildChildren = new WildChildren();
        angel = new Angel();
        chaman = new Chaman();
        cupid = new Cupid();
        fox = new Fox();
        littleGirl = new LittleGirl();
        old = new Old();
        saving = new Saving();
        shower = new Shower();
        showy = new Showy();
        sisters = new Sisters();
        villager = new Villager();
        witch = new Witch();
        fatherWolf = new FatherWolf();
        wolf = new Wolf();
        Roles.ASSASSIN.setClassRole(assassin);
        Roles.WHITE_WOLF.setClassRole(whiteWolf);
        Roles.WILD_CHILDREN.setClassRole(wildChildren);
        Roles.ANGEL.setClassRole(angel);
        Roles.CHAMAN.setClassRole(chaman);
        Roles.CUPID.setClassRole(cupid);
        Roles.FOX.setClassRole(fox);
        Roles.LITTLE_GIRL.setClassRole(littleGirl);
        Roles.OLD.setClassRole(old);
        Roles.SAVING.setClassRole(saving);
        Roles.SHOWER.setClassRole(shower);
        Roles.SHOWY.setClassRole(showy);
        Roles.SISTERS.setClassRole(sisters);
        Roles.VILLAGER.setClassRole(villager);
        Roles.WITCH.setClassRole(witch);
        Roles.FATHER_WOLF.setClassRole(fatherWolf);
        Roles.WOLF.setClassRole(witch);
    }

    @EventHandler
    public void onDay(DayEvent e) {
        if(!WereWolves.getWereWolves().isDiscovered()){
            return;
        }
        for(Roles role : WereWolves.getWereWolves().getRoles()){
            role.getClassRole().onDay(e);
        }
    }

    @EventHandler
    public void onNight(NightEvent e) {
        if(!WereWolves.getWereWolves().isDiscovered()){
            return;
        }
        for(Roles role : WereWolves.getWereWolves().getRoles()){
            role.getClassRole().onNight(e);
        }
    }

    public void onDiscover(DiscoverEvent e) {
        for(Roles role : WereWolves.getWereWolves().getRoles()){
            role.getClassRole().onDiscover(e);
        }
    }

    public ArrayList<UUID> getWolves() {
        return wolves;
    }

    public ArrayList<UUID> getVillagers() {
        return villagers;
    }

    public Assassin getAssassin() {
        return assassin;
    }

    public WhiteWolf getWhiteWolf() {
        return whiteWolf;
    }

    public WildChildren getWildChildren() {
        return wildChildren;
    }

    public Angel getAngel() {
        return angel;
    }

    public Chaman getChaman() {
        return chaman;
    }

    public Cupid getCupid() {
        return cupid;
    }

    public Fox getFox() {
        return fox;
    }

    public LittleGirl getLittleGirl() {
        return littleGirl;
    }

    public Old getOld() {
        return old;
    }

    public Saving getSaving() {
        return saving;
    }

    public Shower getShower() {
        return shower;
    }

    public Showy getShowy() {
        return showy;
    }

    public Sisters getSisters() {
        return sisters;
    }

    public Villager getVillager() {
        return villager;
    }

    public Witch getWitch() {
        return witch;
    }

    public FatherWolf getFatherWolf() {
        return fatherWolf;
    }

    public Wolf getWolf() {
        return wolf;
    }
}
