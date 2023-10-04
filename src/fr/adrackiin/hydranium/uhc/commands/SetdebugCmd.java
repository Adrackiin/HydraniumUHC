package fr.adrackiin.hydranium.uhc.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.game.Game;

public class SetdebugCmd implements APCommandListener {

    private final APCommand command;

    public SetdebugCmd(){
        this.command = new APCommand(
                "setdebug",
                new String[]{},
                "Mettre l'UHC en mode Débug",
                new String[]{},
                "hydranium.uhc.command.setdebug"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] strings){
        return 0;
    }

    @Override
    public String onCommand(String command, APPlayer pl, String[] args, short syntax){
        if(!pl.getUUID().toString().equals("43da311c-d869-4e88-9b78-f1d4fc193ed4")){
            pl.showHelpCommand();
            return null;
        }
        if(args[0].equalsIgnoreCase("true")){
            UHCCore.getPlugin().setDebug(true);
            Game.getGame().getSettings().setSize((short) 100);
        } else if(args[0].equalsIgnoreCase("false")){
            UHCCore.getPlugin().setDebug(false);
        }
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
