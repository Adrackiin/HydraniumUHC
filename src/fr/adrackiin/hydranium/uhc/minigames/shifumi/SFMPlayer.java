package fr.adrackiin.hydranium.uhc.minigames.shifumi;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import org.bukkit.Bukkit;

import java.util.UUID;

public class SFMPlayer {

    private final UUID uuid;
    private UUID challenger;
    private Shifumi.Object choice;
    private int stop;

    public SFMPlayer(UUID uuid){
        this.uuid = uuid;
    }

    public void stopTimer(){
        Bukkit.getScheduler().cancelTask(stop);
    }

    public UUID getUUID(){
        return uuid;
    }

    public UUID getChallenger(){
        return challenger;
    }

    public void setChallenger(UUID challenger){
        this.challenger = challenger;
        stop = Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), ()-> {
            APICore.getPlugin().getAPPlayer(this.uuid).sendMessage(Prefix.minigame + "§cVotre shifumi contre " + APICore.getPlugin().getAPPlayer(this.challenger).getName() + " a expiré");
            APICore.getPlugin().getAPPlayer(this.challenger).sendMessage(Prefix.minigame + "§cVotre shifumi contre " + APICore.getPlugin().getAPPlayer(this.uuid).getName() + " a expiré");
            this.challenger = null;
        }, 10L*20L);
    }

    public Shifumi.Object getChoice(){
        return choice;
    }

    public void setChoice(Shifumi.Object choice){
        this.choice = choice;
        if(Game.getGame().getMiniGameManager().getShifumi().getSFMPlayer(this.challenger).getChoice() != null){
            Game.getGame().getMiniGameManager().getShifumi().startDuel(this.getUUID(), this.getChallenger());
        }
    }
}
