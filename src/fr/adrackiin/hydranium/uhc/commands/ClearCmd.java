package fr.adrackiin.hydranium.uhc.commands;

import fr.adrackiin.api.api.command.ACommand;
import fr.adrackiin.api.api.player.APlayer;
import fr.adrackiin.api.api.text.ATextComponent;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collections;

public class ClearCmd extends ACommand {
    
    public ClearCmd(ConfigurationSection section){
        super(
                "clear",
                ATextComponent.getStringConfig(section.getConfigurationSection("description")),
                ATextComponent.getStringConfig(section.getConfigurationSection("usage")),
                Collections.emptyList(),
                "hydranium.uhc.command.clear"
        );
    }
    
    @Override
    public boolean onCommand(APlayer player, String label, String[] args){
        player.clearInventory();
        return true;
    }
    
}
