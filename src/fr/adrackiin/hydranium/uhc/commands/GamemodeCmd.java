package fr.adrackiin.hydranium.uhc.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import org.bukkit.GameMode;

public class GamemodeCmd implements APCommandListener {

    private final APCommand command;

    public GamemodeCmd(){
        this.command = new APCommand(
                "gamemode",
                new String[]{"gm"},
                "Changer de gamemode",
                new String[]{"- Inverser créatif / spectateur", "[1,3]"},
                "hydranium.uhc.command.gamemode"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] args){
        if(args.length > 0){
            switch(args[0]){
                case "1":
                case "3":
                    return 2;
            }
        }
        return 1;
    }

    @Override
    public String onCommand(String command, APPlayer pl, String[] args, short syntax){
        switch(syntax){
            case 1:
                if(pl.isGameMode(GameMode.SPECTATOR)){
                    pl.setGameMode(GameMode.CREATIVE);
                } else {
                    pl.setGameMode(GameMode.SPECTATOR);
                }
                break;
            case 2:
                if(args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("creative")){
                    pl.setGameMode(GameMode.CREATIVE);
                } else {
                    pl.setGameMode(GameMode.SPECTATOR);
                }
        }
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
