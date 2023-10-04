package fr.adrackiin.hydranium.uhc.minigames.shifumi;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.exceptions.APGuiNotFoundException;
import fr.adrackiin.hydranium.api.utils.APText;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;

public class ShifumiCmd implements APCommandListener {

    private final APCommand command;

    public ShifumiCmd(){
        this.command = new APCommand(
                "shifumi",
                new String[]{},
                "Défier un joueur au shifumi",
                new String[]{},
                "hydranium.uhc.minigame.shifumi"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] strings){
        return 0;
    }

    @Override
    public String onCommand(String command, APPlayer challengerPlayer, String[] args, short syntax){
        APPlayer challengedPlayer = APICore.getPlugin().getAPPlayer(args[0]);
        if(!(boolean) challengerPlayer.getSettings().get("shifumi")){
            return null;
        }
        if(!(boolean) challengedPlayer.getSettings().get("shifumi")){
            challengerPlayer.sendMessageBar("§cCe joueur n'a pas activé le shifumi");
            return null;
        }
        Shifumi shifumi = Game.getGame().getMiniGameManager().getShifumi();
        if(!shifumi.getSFMPlayers().contains(challengerPlayer.getUUID())){
            shifumi.getSFMPlayers().put(challengerPlayer.getUUID(), new SFMPlayer(challengerPlayer.getUUID()));
        }
        if(!shifumi.getSFMPlayers().contains(challengedPlayer.getUUID())){
            shifumi.getSFMPlayers().put(challengedPlayer.getUUID(), new SFMPlayer(challengedPlayer.getUUID()));
        }
        SFMPlayer challenged = shifumi.getSFMPlayer(challengedPlayer.getUUID());
        SFMPlayer challenger = shifumi.getSFMPlayer(challengerPlayer.getUUID());
        if(challenged.getChallenger() == challenger.getUUID()){
            challenged.stopTimer();
            challenger.stopTimer();
            challengedPlayer.teleport(shifumi.getPlayer1());
            challengerPlayer.teleport(shifumi.getPlayer2());
            try {
                APICore.getPlugin().getAPGuiManager().get("§cShifumi").getAPGui().open(challengedPlayer);
                APICore.getPlugin().getAPGuiManager().get("§cShifumi").getAPGui().open(challengerPlayer);
            } catch(APGuiNotFoundException e){
                e.printStackTrace();
            }
            return null;
        }
        challenger.setChallenger(challenged.getUUID());
        challengerPlayer.sendMessage(Prefix.minigame + "§aCe joueur a 10 secondes pour accepter votre duel");
        challengedPlayer.sendMessage(new APText(Prefix.minigame + "§7" + challengerPlayer.getName() + " vous a défié au shifumi. ").add(new APText("§a/shifumi" + challengerPlayer.getName() + " pour accepter").showText("§aAccepter").runCommand("/shifumi " + challengerPlayer.getName())));
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
