package fr.adrackiin.hydranium.uhc.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.core.players.UHCPlayer;

public class HelperCmd implements APCommandListener {

    private final APCommand command;

    public HelperCmd(){
        this.command = new APCommand(
                "helper",
                new String[]{},
                "Voir les demandes des joueurs",
                new String[]{},
                "hydranium.uhc.command.helper"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] args){
        return 0;
    }

    @Override
    public String onCommand(String command, APPlayer pl, String[] args, short syntax){
        UHCPlayer player = Game.getGame().getUHCPlayer(pl.getUUID());
        player.setHelper(!player.isHelper());
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
