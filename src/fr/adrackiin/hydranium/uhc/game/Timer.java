package fr.adrackiin.hydranium.uhc.game;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.GameStateChangeEvent;
import fr.adrackiin.hydranium.uhc.events.SecondEvent;
import fr.adrackiin.hydranium.uhc.game.border.UHCBorder;
import fr.adrackiin.hydranium.uhc.game.settings.Settings;
import fr.adrackiin.hydranium.uhc.game.state.GameState;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class Timer implements Listener {

    private int timer = 0;
    private int invulnerability;
    private int finalHeal;
    private int pvp;
    private int border;
    private int meetup;
    private int finale;
    private boolean invulnerabilityAct = false;
    private boolean finalHealAct = false;
    private boolean pvpAct = false;
    private boolean borderAct = false;
    private boolean meetupAct = false;

    private final Settings settings = Game.getGame().getSettings();

    public Timer(){
        init();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onSecond(SecondEvent e){
        timer ++;
        timeStates();
    }

    public void stop(){
        HandlerList.unregisterAll(this);
    }

    public void start(){
        Bukkit.broadcastMessage(Prefix.uhc + "§7" + "Fin de l'invincibilité dans " + "§6" + invulnerability + "§7" + " secondes");
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    public void forceFinalHeal(){
        if(!finalHealAct) {
            timer = finalHeal - 11;
        }
    }

    public void forcePvp(){
        if(!pvpAct) {
            timer = pvp - 11;
        }
    }

    public void forceBorder(){
        if(!borderAct) {
            timer = border - 11;
        }
    }

    public int getTimer() {
        return timer;
    }

    public int getPvp() {
        return pvp;
    }

    public int getBorder() {
        return border;
    }

    public int getMeetup() {
        return meetup;
    }

    public int getFinale() {
        return finale;
    }

    private void timeStates(){
        if(!invulnerabilityAct){
            if(timer > invulnerability){
                Bukkit.getServer().getPluginManager().callEvent(new GameStateChangeEvent(GameState.MINING));
                invulnerabilityAct = true;
            } else {
                if (timer == invulnerability - 30 || timer == invulnerability - 15 || timer == invulnerability - 10 || timer >= invulnerability - 5 && timer != invulnerability) {
                    Bukkit.broadcastMessage(Prefix.uhc + "§7" + "Fin de l'invincibilité dans " + "§6" + (invulnerability - timer) + "§7" + " secondes");
                } else if (timer >= invulnerability) {
                    Bukkit.getServer().getPluginManager().callEvent(new GameStateChangeEvent(GameState.MINING));
                    invulnerabilityAct = true;
                }
            }
        }

        if(!finalHealAct){
            if(timer > finalHeal){
                Game.getGame().finalHeal();
                finalHealAct = true;
            } else {
                if (timer == finalHeal - 30 || timer == finalHeal - 15 || timer == finalHeal - 10 || timer >= finalHeal - 5 && timer != finalHeal) {
                    Bukkit.broadcastMessage(Prefix.uhc + "§7" + "Final Heal dans " + "§6" + (finalHeal - timer) + "§7" + " secondes");
                } else if (timer == finalHeal) {
                    Game.getGame().finalHeal();
                    finalHealAct = true;
                }
            }
        }
        if(!pvpAct){
            if(timer > pvp){
                Bukkit.getServer().getPluginManager().callEvent(new GameStateChangeEvent(GameState.PVP));
                pvpAct = true;
            } else {
                Game.getGame().getScoreboardManager().setPvp(pvp - timer);
                if (timer == pvp - 30 || timer == pvp - 15 || timer == pvp - 10 || timer >= pvp - 5 && timer != pvp) {
                    Bukkit.broadcastMessage(Prefix.uhc + "§7" + "Activation du PvP dans " + "§6" + (pvp - timer) + "§7" + " secondes");
                } else if (timer == pvp) {
                    Bukkit.getServer().getPluginManager().callEvent(new GameStateChangeEvent(GameState.PVP));
                    pvpAct = true;
                }
            }
        }
        if(!borderAct){
            if(timer > border){
                Bukkit.getServer().getPluginManager().callEvent(new GameStateChangeEvent(GameState.BORDER));
                borderAct = true;
            } else {
                Game.getGame().getScoreboardManager().setBorder(border - timer);
                if (timer == border - 30 || timer == border - 15 || timer == border - 10 || timer >= border - 5 && timer != border) {
                    Bukkit.broadcastMessage(Prefix.uhc + "§7" + "Début de la réduction de la bordure dans " + "§6" + (border - timer) + "§7" + " secondes");
                } else if (timer == border) {
                    Bukkit.getServer().getPluginManager().callEvent(new GameStateChangeEvent(GameState.BORDER));
                    borderAct = true;
                }
            }
        }
        if(!meetupAct && borderAct){
            if(timer > meetup){
                Bukkit.getServer().getPluginManager().callEvent(new GameStateChangeEvent(GameState.MEETUP));
                meetupAct = true;
            } else {
                if (Game.getGame().getSettings().getBorderType() == UHCBorder.Type.BORDERMOVE) {
                    Game.getGame().getScoreboardManager().setMeetup(meetup - timer);
                }
                if (timer == meetup) {
                    Bukkit.getServer().getPluginManager().callEvent(new GameStateChangeEvent(GameState.MEETUP));
                    meetupAct = true;
                }
            }
        }
        if(meetupAct){
            Game.getGame().getScoreboardManager().setFinalTime(timer);
        }
    }

    private void init(){
        invulnerability = settings.getInvulnerability();
        finalHeal = settings.getFinalHeal() * 60;
        pvp = settings.getPvp() * 60;
        border = settings.getBorder() * 60;
        meetup = border + (settings.getMeetup() * 60);
        finale = -1;
    }

    private void restart(){
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }
}
