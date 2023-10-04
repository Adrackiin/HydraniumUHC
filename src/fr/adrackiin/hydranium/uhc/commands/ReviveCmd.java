package fr.adrackiin.hydranium.uhc.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.uhc.game.Game;

public class ReviveCmd implements APCommandListener {

    private final APCommand command;

    public ReviveCmd() {
        this.command = new APCommand(
                "revive",
                new String[]{},
                "Ressuciter un joueur mort",
                new String[]{"<joueur>"},
                "hydranium.uhc.command.revive"
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
        if(!APICore.getPlugin().doesAPPlayerExists(args[0])){
            return "Ce joueur ne fait pas parti de l'uHC";
        }
        if(!Game.getGame().getUHCPlayer(args[0]).isDead()){
            return "Ce joueur n'est pas mort";
        }
        Game.getGame().getUHCPlayer(args[0]).revive();
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
