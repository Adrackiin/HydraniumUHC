package fr.adrackiin.hydranium.uhc.commands;

import fr.adrackiin.api.api.command.ACommand;
import fr.adrackiin.api.api.player.APlayer;
import fr.adrackiin.api.api.text.ATextComponent;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.core.players.UHCPlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Arrays;

public class CoordsCmd extends ACommand {

    public CoordsCmd(ConfigurationSection section){
        super(
                "coords",
                ATextComponent.getStringConfig(section.getConfigurationSection("description")),
                ATextComponent.getStringConfig(section.getConfigurationSection("usage")),
                Arrays.asList("coord", "tc", "sc"),
                "hydranium.uhc.command.coords"
        );
    }
    
    @Override
    public boolean onCommand(APlayer player, String s, String[] strings){
        UHCPlayer player = Game.getGame().getUHCPlayer(pl.getUUID());
        if(player.getGameTeam() == null){
            return "§7Vous n'avez pas d'équipe";
        }
        if(!player.isPlaying()){
            return "§7Vous ne jouez pas l'UHC";
        }
        if(Game.getGame().hasGameStart()){
            player.getGameTeam().sendMessageTeam("§7Coordonnées de §c" + player.getAPPlayer().getName() + "§7 | x: §c" + player.getAPPlayer().getLocation().getBlockX() + "§7 | y: §c" + player.getAPPlayer().getLocation().getBlockY() + "§7 | z: §c" + player.getAPPlayer().getLocation().getBlockZ());
            return null;
        } else {
            return "§7L'UHC n'a pas encore démarré";
        }
        return false;
    }
}
