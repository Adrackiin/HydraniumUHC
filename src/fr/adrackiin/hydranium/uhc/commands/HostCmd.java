package fr.adrackiin.hydranium.uhc.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;

public class HostCmd implements APCommandListener {

    private final APCommand command;

    public HostCmd(){
        this.command = new APCommand(
                "host",
                new String[]{"say"},
                "Envoyer un message en tant qu'host",
                new String[]{"<message>"},
                "hydranium.uhc.command.host"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] args){
        if(args.length == 0){
            return -1;
        }
        return 0;
    }

    @Override
    public String onCommand(String command, APPlayer pl, String[] args, short syntax){
        StringBuilder message = new StringBuilder();
        for(String arg : args){
            message.append(arg).append(" ");
        }
        APICore.getPlugin().getPlayerManager().sendMessageAll(Prefix.host + "§b" + message.substring(0, message.toString().length() - 1));
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
