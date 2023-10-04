package fr.adrackiin.hydranium.uhc.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.core.players.UHCPlayer;

public class PlayerCmd implements APCommandListener {

    private final APCommand command;

    public PlayerCmd(){
        this.command = new APCommand(
                "player",
                new String[]{},
                "Switcher entre joueur et host",
                new String[]{},
                "hydranium.uhc.command.player"
        ).setInvisible();
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] strings){
        return 0;
    }

    @Override
    public String onCommand(String command, APPlayer pl, String[] args, short syntax){
        if(!UHCCore.getPlugin().isDebug()){
            pl.showHelpCommand();
            return null;
        }
        UHCPlayer player = Game.getGame().getUHCPlayer(pl.getUUID());
        if(player.isPlaying()){
            player.setSpectator();
        } else if(player.isSpectator()){
            player.setPlaying();
        }
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
