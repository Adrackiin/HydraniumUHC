package fr.adrackiin.hydranium.uhc.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.uhc.UHCCore;
import org.bukkit.Bukkit;

public class WelcomeCmd implements APCommandListener {

    private final APCommand command;

    public WelcomeCmd(){
        this.command = new APCommand(
                "welcome",
                new String[]{},
                "Envoyer les règles (Soon)",
                new String[]{},
                "hydranium.uhc.command.uhc"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] strings){
        return 0;
    }

    @Override
    public String onCommand(String command, APPlayer pl, String[] args, short syntax){
        APICore.getPlugin().getPlayerManager().sendTitleAll("§4Ceci est un UHC Test", "§4Des bugs peuvent être présent", 10, 60, 10);
        Bukkit.getScheduler().runTaskLater(UHCCore.getPlugin(), ()-> {
            APICore.getPlugin().getPlayerManager().sendTitleAll("§4N'hésitez pas", "§4à prévenir en cas de bugs (/bug <bug>)", 10, 60, 10);
            Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), ()-> APICore.getPlugin().getPlayerManager().executeCommandAll("rules"), 2 * 20L);
        }, 5 * 20L);
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
