package fr.adrackiin.hydranium.uhc.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.channel.Channel;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.exceptions.ChannelNotFoundException;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.core.players.UHCPlayer;
import fr.adrackiin.hydranium.uhc.utils.PubSub;
import org.bukkit.Bukkit;

public class SethostCmd implements APCommandListener {

    private final APCommand command;

    public SethostCmd(){
        this.command = new APCommand(
                "sethost",
                new String[]{},
                "Gérer les hosts de l'UHC",
                new String[]{"add <joueur> - Ajouter un host", "remove <joueur> - Retirer un host"},
                "hydranium.uhc.command.sethost"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] args){
        if(args.length != 2){
            return -1;
        }
        switch(args[0].toLowerCase()){
            case "add":
                return 1;
            case "remove":
                return 2;
            default:
                return -1;
        }
    }

    @Override
    public String onCommand(String command, APPlayer pl, String[] args, short syntax){
        if(!APICore.getPlugin().doesAPPlayerExists(args[1]) || Bukkit.getPlayer(args[1]) == null){
            return "Ce joueur n'est pas connecté";
        }
        UHCPlayer target = Game.getGame().getUHCPlayer(args[1]);
        if(target == null){
            return "Ce joueur n'est pas connecté ou n'existe pas";
        }
        Channel host;
        try{
            host = APICore.getPlugin().getChannelManager().getChannel("channel.host");
        } catch(ChannelNotFoundException e){
            e.printStackTrace();
            return "Problème interne";
        }
        switch(syntax){
            case 1:
                if(target.getAPPlayer().hasPermission("hydranium.uhc.allowhost")){
                    if(!target.isHost()){
                        target.setHost(true);
                        host.sendMessage("§c" + target.getAPPlayer().getName() + "§7 est passé Host");
                        PubSub.addHost(target.getAPPlayer());
                    } else {
                        return "Ce joueur est déjà Host";
                    }
                } else {
                    return "Ce joueur n'est pas autoriser à être Host";
                }
                break;
            case 2:
                if(target.isHost()){
                    if(Game.getGame().getHost() == target.getAPPlayer().getUUID()){
                        return "Vous ne pouvez pas dé-host l'Host principale";
                    }
                    target.setHost(false);
                    host.sendMessage("§c" + target.getAPPlayer().getName() + "§7 n'est plus Host");
                    PubSub.removeHost(target.getAPPlayer());
                } else {
                    return "Ce joueur n'est pas Host";
                }
                break;
        }
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
