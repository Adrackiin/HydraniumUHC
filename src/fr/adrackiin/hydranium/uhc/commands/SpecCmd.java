package fr.adrackiin.hydranium.uhc.commands;

import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;

public class SpecCmd implements APCommandListener {

    public SpecCmd(){
    }

    @Override
    public int getSyntax(String[] strings){
        return 0;
    }

    @Override
    public String onCommand(String s, APPlayer apPlayer, String[] strings, short i){
        return null;
    }

    @Override
    public APCommand getCommand(){
        return null;
    }
}
