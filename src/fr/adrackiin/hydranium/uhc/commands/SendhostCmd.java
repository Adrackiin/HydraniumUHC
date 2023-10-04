package fr.adrackiin.hydranium.uhc.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.exceptions.CanTakeTimeException;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.utils.PubSub;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import fr.adrackiin.hydranium.uhc.utils.minecraft.Players;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class SendhostCmd implements APCommandListener {

    private final APCommand command;

    public SendhostCmd(){
        this.command = new APCommand(
                "sendhost",
                new String[]{},
                "Envoyer un host dans l'UHC",
                new String[]{"<joueur>"},
                "hydranium.uhc.command.sendhost"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] args){
        if(args.length != 1){
            return -1;
        }
        return 0;
    }

    @Override
    public String onCommand(String command, APPlayer pl, String[] args, short syntax){
        OfflinePlayer target = Players.getOfflinePlayer(args[0]);
        if(Bukkit.getPlayer(target.getUniqueId()) != null){
            return "Ce joueur est déjà dans l'UHC";
        }
        Bukkit.getScheduler().runTaskAsynchronously(UHCCore.getPlugin(), ()-> {
            try {
                boolean isConnected = !APICore.getPlugin().getDataManager().get(target.getUniqueId(), "server").equals("not-connected");
                Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), ()-> {
                    if(isConnected){
                        pl.sendMessage(Prefix.uhc + "§aCe joueur va être envoyé dans l'host s'il a la permission");
                        PubSub.commandSendHost(args[0]);
                        return;
                    }
                    pl.sendMessage(Prefix.uhc + "§cCe joueur n'est pas connecté");
                });
            } catch(CanTakeTimeException ignored){}
        });
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
