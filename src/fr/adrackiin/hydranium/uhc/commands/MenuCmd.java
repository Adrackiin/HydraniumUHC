package fr.adrackiin.hydranium.uhc.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.exceptions.APGuiNotFoundException;

public class MenuCmd implements APCommandListener {

    private final APCommand command;

    public MenuCmd(){
        this.command = new APCommand(
                "menu",
                new String[]{},
                "Ouvrir le menu Host",
                new String[]{},
                "hydranium.uhc.command.menu"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] args){
        return 0;
    }

    @Override
    public String onCommand(String command, APPlayer pl, String[] args, short syntax){
        try {
            APICore.getPlugin().getAPGuiManager().get("§cHost").getAPGui().open(pl);
        } catch(APGuiNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
