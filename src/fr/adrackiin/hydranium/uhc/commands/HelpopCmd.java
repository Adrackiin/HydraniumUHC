package fr.adrackiin.hydranium.uhc.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.channel.Channel;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.exceptions.ChannelNotFoundException;
import fr.adrackiin.hydranium.api.utils.APText;

public class HelpopCmd implements APCommandListener {

    private final APCommand command;

    public HelpopCmd(){
        this.command = new APCommand(
                "helpop",
                new String[]{"h"},
                "Poser une question - Faire une demande",
                new String[]{"<message>"},
                "hydranium.uhc.command.helpop"
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
        Channel helpop;
        try {
            helpop = APICore.getPlugin().getChannelManager().getChannel("channel.helpop");
        } catch(ChannelNotFoundException e) {
            e.printStackTrace();
            return "§7Une erreur innatendue est survenue";
        }
        if(helpop.isEmpty()){
            return "§7Aucun membre du staff n'est disponible";
        }
        StringBuilder helpMessage = new StringBuilder();
        for(String arg : args){
            helpMessage.append(arg).append(" ");
        }
        helpop.sendMessage(new APText("§3" + pl.getName())
                .showText("§eSe téléporter")
                .runCommand("/tp " + pl.getName())
                .add(new APText("§7 » §6" + helpMessage)
                        .showText("Envoyer un message")
                        .suggestCommand("/msg " + pl.getName() + " ")));
        pl.sendMessage(helpop.getDisplayName() + "§aVotre demande a bien été envoyé: §e" + helpMessage.substring(0, helpMessage.length() - 1));
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
